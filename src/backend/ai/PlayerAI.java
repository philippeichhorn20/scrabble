package backend.ai;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.network.client.AIProtocol;
import backend.network.messages.Message;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import frontend.screens.controllertools.LetterSetHolder;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @auhtor jawinter Superclass for the AIs. All Ai players are extended from this class, which is
 * extended from player. The handleTurn method is the only thing, which should be customized
 * according to the AIs difficulty
 */
public class PlayerAI extends Player {

  protected Brain brain;
  protected String name;
  int myNumber = -1;
  protected Tile[] tilesOnHand = new Tile[7];
  protected AIProtocol aiProtocol;
  private ArrayList<PossibleWord> triedWords = new ArrayList<PossibleWord>();
  private ArrayList<Tile> lastTilesSent = new ArrayList<Tile>();

  /**
   * Constructor which you can modify to set different color for AIs
   *
   * @param name the name is needed for creating a unique AI player
   */
  public PlayerAI(String name) {
    super(name, "#d3d3d3", 0, 0, Playerstatus.AI);
    this.name = name;
    this.brain = new Brain(new ScrabbleBoard());
  }

  public Tile[] getTiles() {
    return this.tilesOnHand;
  }

  public void setTiles(Tile[] tiles) {
    this.tilesOnHand = tiles;
  }

  /**
   * Requests to shuffle tiles on hand
   *
   * @param oldTiles give the tiles you dont want anymore
   */
  public void sendShuffleMessage(Tile[] oldTiles) {
    try {
      aiProtocol.sendToServer(new ShuffleTilesMessage(this.name, oldTiles));
      aiProtocol.sendToServer(new PassMessage(this.name));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Message allows to let next player now its his turn
   */
  public void sendPassMessage() {
    try {
      aiProtocol.sendToServer(new PassMessage(this.name));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Brain getBrain() {
    return brain;
  }

  public ArrayList<PossibleWord> getTriedWords() {
    return triedWords;
  }

  /**
   * Resets tried words. Only need to be saved for one turn.
   */
  public void flushTried() {
    this.triedWords.clear();
  }

  public void setBrainBoard(ScrabbleBoard board) {
    this.brain.setScrabbleboard(board);
  }

  /**
   * Initalize myNumber which is used for determining if AIs turn is now
   *
   * @param players list with players to assign the number inside AI
   */
  public void handleStartGame(Player[] players) {
    for (int i = 0; i < players.length; i++) {
      if (players[i] != null && players[i].getName().equals(this.name)) {
        this.myNumber = i;
      }
    }
  }

  /**
   * Method accepts first tiles
   *
   * @param tiles first rack given by server
   */
  public void handleGameStartMessage(Tile[] tiles) {
    for (Tile t : tiles) {
      if (t.isJoker()) {
        t.setLetter(getRandomLetter());
      }
    }
    this.tilesOnHand = tiles;
  }

  /**
   * Method decides whether it is AIs turn or not by comparing nowTurn with myNumber
   *
   * @param nowTurn number of player whos turn it is
   */
  public void handleGameTurnMessage(int nowTurn) {
    if (nowTurn == myNumber) {
      handleTurn();
    }
  }

  /**
   * This method should be overwritten by different difficulties of AIs
   */
  public void handleTurn() {
  }

  /**
   * Method is called and given new Tiles which were put on board. Brain needs new tiles to know how
   * to play.
   *
   * @param tiles tiles that were put on board
   */
  public void updateScrabbleboard(Tile[] tiles) {
    brain.updateScrabbleboard(tiles);
  }

  /**
   * Method accepts new tiles and fills rack
   *
   * @param tiles new tiles to put in rack
   */
  public void acceptNewTiles(Tile[] tiles) {
    int indexCounter = 0;
    for (int i = 0; i < this.tilesOnHand.length; i++) {
      if (tilesOnHand[i] == null) {
        if (indexCounter < tiles.length && tiles[indexCounter].isJoker()) {
          tiles[indexCounter].setLetter(getRandomLetter());
          tiles[indexCounter].setValue(getValueForLetter(tiles[indexCounter]));
        }
        if (indexCounter < tiles.length) {
          tilesOnHand[i] = tiles[indexCounter++];
        }
      }
    }
  }

  /**
   * This is needed to let the AI only send the tiles it puts on the board, which are not alraedy
   * situated there
   *
   * @param tilesToPlay  tiles to form the word which AI wants to place
   * @param possibleWord is the possibleWord and holds the basetile
   * @return tile array without the base tile
   */
  public Tile[] removeBaseTile(Tile[] tilesToPlay, PossibleWord possibleWord) {
    if (this.brain.getScrabbleBoard().getScrabbleBoard()[8][8].hasTile()) {
      Tile avoid = possibleWord.getBaseTile();
      int avoidIndex = -1;
      for (int i = 0; i < tilesToPlay.length; i++) {
        if (tilesToPlay[i].equals(avoid)) {
          avoidIndex = i;
        }
      }
      Tile[] tilesToPlaceOnBoard = new Tile[tilesToPlay.length - 1];
      int j = 0;
      for (int i = 0; i < tilesToPlay.length; i++) {
        if (i == avoidIndex) {
          i++;
        }
        if (j < tilesToPlaceOnBoard.length) {
          tilesToPlaceOnBoard[j] = tilesToPlay[i];
        }
        j++;
      }
      return tilesToPlaceOnBoard;
    } else {
      return tilesToPlay;
    }
  }

  /**
   * Method is used to place tiles on the board
   *
   * @param tilesToPlay tiles to be laid on board
   * @throws IOException
   */
  public void placeTiles(Tile[] tilesToPlay) throws IOException {
    brain.getScrabbleBoard().nextTurn();
    Message place = new PlaceTilesMessage(name, tilesToPlay);
    aiProtocol.sendToServer(place);
    for (int i = 0; i < tilesToPlay.length; i++) {
      this.lastTilesSent.add(tilesToPlay[i]);
    }
    removeUsedTilesFromHand(tilesToPlay);
  }

  /**
   * Updates the srabbleboard the brain uses to calculate
   *
   * @param word tile array which was placed
   */
  public void placeTilesFromServer(Tile[] word) {
    brain.updateScrabbleboard(word);
    brain.getScrabbleBoard().nextTurn();
  }

  /**
   * Pass message is used if AI should not place a word
   *
   * @throws IOException
   */
  public void pass() throws IOException {
    Message pass = new PassMessage(name);
    aiProtocol.sendToServer(pass);
  }

  /**
   * Method avoids joker, because AI can't handle them yet
   *
   * @return a random char between capital A and Z
   */
  public static char getRandomLetter() {
    int random = (int) (Math.random() * 25 + 65);
    return (char) random;
  }

  public AIProtocol getAiProtocol() {
    return aiProtocol;
  }

  /**
   * Method makes place for ne tiles, which will be sent by server
   *
   * @param tilesRemove
   */
  public void removeUsedTilesFromHand(Tile[] tilesRemove) {
    for (int j = 0; j < tilesRemove.length; j++) {
      for (int i = 0; i < this.tilesOnHand.length; i++) {
        if (tilesOnHand[i] != null && tilesRemove[j].equals(tilesOnHand[i])) {
          tilesOnHand[i] = null;
          break;
        }
      }
    }
  }

  public void setAiProtocol(AIProtocol aiProtocol) {
    this.aiProtocol = aiProtocol;
  }

  /**
   * Calculates value for a given letter
   *
   * @param t Tile which needs evaluation
   * @return int wiht points of a word
   */
  public int getValueForLetter(Tile t) {
    Tile[] tileSet = LetterSetHolder.getInstance().getTileSet();
    for (int i = 0; i < tileSet.length; i++) {
      if (tileSet[i].getLetter() == t.getLetter()) {
        return tileSet[i].getValue();
      }
    }
    return -1;
  }

  public ArrayList<Tile> getLastTilesSent() {
    return lastTilesSent;
  }

  /**
   * When AI placed correct word this updates the AIs board too and resets lastTilesSent as it is
   * not needed anymore
   */
  public void validWordConfirmation() {
    Tile[] place = this.lastTilesSent.toArray(new Tile[0]);
    updateScrabbleboard(place);
    this.lastTilesSent = new ArrayList<Tile>();
  }
}
