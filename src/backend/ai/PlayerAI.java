package backend.ai;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.network.client.AIProtocol;
import backend.network.messages.Message;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import java.io.IOException;

/*
@author jawinter
@description Superclass for AIs
 */
public class PlayerAI {

  protected Brain brain;
  protected String name;
  protected Player[] playerList;
  protected Tile[] tilesOnHand;
  protected AIProtocol aiProtocol;

  public PlayerAI(String name) {
    this.name = name;
    this.brain = new Brain(new ScrabbleBoard());
  }

  public Tile[] getTiles() {
    return this.tilesOnHand;
  }

  public void setTiles(Tile[] tiles) {
    for (Tile t : tiles) {
      if (t.isJoker()) {
        t.setLetter(getRandomLetter());
      }
    }
    this.tilesOnHand = tiles;
  }

  public Brain getBrain() {
    return brain;
  }

  public void setBrainBoard(ScrabbleBoard board) {
    this.brain.setScrabbleboard(board);
  }

  /*
    Initalize playerList when game starts
     */
  public void handleStartGame(Player[] players) {
    this.playerList = players;
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
    if (playerList[nowTurn].equals(name)) {
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
    int count = 0;
    for (Tile t : this.tilesOnHand) {
      if (t.equals(null)) {
        if (tiles[count].isJoker()) {
          tiles[count].setLetter(getRandomLetter());
        }
        t = tiles[count++];
      }
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
    Message place = new PlaceTilesMessage(name, tilesToPlay);
    aiProtocol.sendToServer(place);
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

}
