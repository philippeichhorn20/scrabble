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
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
@author jawinter
@description Superclass for AIs
 */
public class PlayerAI extends Player {

  protected Brain brain;
  protected String name;
  int myNumber = -1;
  protected Tile[] tilesOnHand = new Tile[7];
  protected AIProtocol aiProtocol;
  private ArrayList<PossibleWord> triedWords = new ArrayList<PossibleWord>();
  private ArrayList<Tile> lastTilesSent = new ArrayList<Tile>();


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

  public void sendShuffleMessage(Tile[] oldTiles) {
    try {
      aiProtocol.sendToServer(new ShuffleTilesMessage(this.name, oldTiles));
      aiProtocol.sendToServer(new PassMessage(this.name));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
  to let next player take turn
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

  /*
  resets tried words
   */
  public void flushTried() {
    this.triedWords.clear();
  }

  public void setBrainBoard(ScrabbleBoard board) {
    this.brain.setScrabbleboard(board);
  }

  /*
    Initalize playerList when game starts
     */
  public void handleStartGame(Player[] players) {
    for (int i = 0; i < players.length; i++) {
      if (players[i] != null && players[i].getName().equals(this.name)) {
        this.myNumber = i;
      }
    }
  }

  /*
  Receive first tiles and put them on rack of AI
   */
  public void handleGameStartMessage(Tile[] tiles) {
    for (Tile t : tiles) {
      if (t.isJoker()) {
        t.setLetter(getRandomLetter());
      }
    }
    this.tilesOnHand = tiles;
  }

  /*
  Recognizes whether it is AI's turn. Triggers handleTurn.
   */
  public void handleGameTurnMessage(int nowTurn) {
    if (nowTurn == myNumber) {
      handleTurn();
    }
  }

  /*
  Handles turn. Different ai handle this one differently.
   */
  public void handleTurn() {

    //If placetiles or shuffleTiles dont forget to set them null
  }

  /*
  Method is called and given new Tiles which were put on board. Brain needs ne tiles to know
  how to play
   */
  public void updateScrabbleboard(Tile[] tiles) {
    brain.updateScrabbleboard(tiles);
  }

  /*
  When AI receives new tiles this will add it to tilesOnHand
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

  /*
  Remove the tile which is needed for building the board, but is from the board
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

  /*
  When AI wants to shuffle
   */
  public void requestNewTiles() throws IOException {
    Message shuffle = new ShuffleTilesMessage(name, this.tilesOnHand);
    aiProtocol.sendToServer(shuffle);
  }

  /*
  When AI places word
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

  /*
  When others place words
   */
  public void placeTilesFromServer(Tile[] word) {
    brain.updateScrabbleboard(word);
    brain.getScrabbleBoard().nextTurn();
  }

  /*
  When AI doesnt want is lazy
   */
  public void pass() throws IOException {
    Message pass = new PassMessage(name);
    aiProtocol.sendToServer(pass);
  }

  /*
  AI is not smart enough to deal with blanks. Therefore this method helps getting a random char and
  returns it.
   */
  public static char getRandomLetter() {
    int random = (int) (Math.random() * 25 + 65);
    return (char) random;
  }

  public AIProtocol getAiProtocol() {
    return aiProtocol;
  }

  /*
  Method removes tiles from hand after being placed on board. Tiles to remove are given in parameter
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

  public void validWordConfirmation() {
    Tile[] place = this.lastTilesSent.toArray(new Tile[0]);
    updateScrabbleboard(place);
    this.lastTilesSent = new ArrayList<Tile>();
  }
}
