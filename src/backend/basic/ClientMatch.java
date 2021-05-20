package backend.basic;

import backend.basic.Tile.Tilestatus;
import backend.network.client.ClientProtocol;
import backend.network.messages.MessageType;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import java.io.IOException;
import java.util.ArrayList;

/*
  @peichhor
  this class represents a Player

 @method nextPlayer() sets the current player to the next number, deletes "currentMove" properties from scrabbleboard
 @method receiveShuffledTiles
 @method submitTilesOfClient submits the tiles and validates the, informes the player with a string about valid and invalid inputs
//TODO: delete the invalid input fields

 */
public class ClientMatch {

  private static final int round = 0;
  private final Player[] players;
  private final Player player;
  private ClientProtocol protocol;
  private final String from;
  private ScrabbleBoard scrabbleBoard;
  private int points = 0;
  private final boolean youLost = false;
  private int currentPlayer = 0;
  private int myNumber;
  private boolean isOver = false;
  private boolean youWon = false;
  private boolean waitingForShuffledTiles = false;
  private final boolean yourTurn = false;
  private int yourTurnNum;
  private String gameEvents = "";


  public ClientMatch(ClientProtocol protocol, Player[] players, String from, Player player) {
    this.player = player;
    this.protocol = protocol;
    this.players = players;
    this.from = from;
  }

  public ClientMatch(String from, Player player) {
    this.player = player;
    this.from = from;
    this.players = new Player[4];
    this.players[0] = player;
  }

  /*
  @method
  places the tile on the scrabbleboard and sends the info to server
   */


  /*
  @method
   */

  public static int getRound() {
    return round;
  }

  public static Tile[] tileArrayToList(ArrayList<Tile> tiles) {
    Tile[] tiles1 = new Tile[tiles.size()];
    for (int x = 0; x < tiles1.length; x++) {
      tiles1[x] = tiles.get(x);
    }
    return tiles1;
  }

  public void yourTurn() {
    yourTurnNum = currentPlayer;
    scrabbleBoard.nextTurn();
    this.player.getTimer().nextPlayer();
    newGameEvent("It is now your turn");
  }

  public void nextPlayer() {
    int notActivePlayers = 0;
    int nextPlayer = 4 % (currentPlayer + 1);
    do {
      notActivePlayers++;
      nextPlayer = 4 % (nextPlayer + 1);
    } while (notActivePlayers < 4 && players[nextPlayer] != null);

    if (players[nextPlayer].checkTimer()) {
      currentPlayer = nextPlayer;
    }
    this.player.getTimer().nextPlayer();
    scrabbleBoard.nextTurn();
    newGameEvent("It is now " + players[currentPlayer].getName() + "'s turn");
  }

  // Method is called, when player decides to not do anything this turn
  public void pass() throws IOException {
    protocol.sendToServer(new PassMessage(MessageType.PASS,"server"));
  }

  public String submitTilesOfClient() throws IOException {
    String[][] result = scrabbleBoard.submitTiles();
    boolean isValid = scrabbleBoard.inputValudation(result);
    String resultString = "";
    if (isValid) {
      resultString += "the following valid words were added:\n";
      for (int i = 0; i < result[0].length; i++) {
        resultString += result[0][i] + ", explanation: " + result[1][i] + "\n";
      }
      this.points = scrabbleBoard.getPoints();
      resultString += "they were worth a whopping" + scrabbleBoard.getPoints()
          + " ! You are now up to " + this.points + " points.";
      this.protocol.sendToServer(
          new PlaceTilesMessage(player.name, tileArrayToList(scrabbleBoard.newTilesOfCurrentMove)));
      scrabbleBoard.nextTurn();
    } else {
      resultString += "what the heck do you mean by ";
      for (int i = 0; i < result[0].length; i++) {
        if (result[1][i] == "") {
          resultString += result[0][i] + ", ";
        }
      }
    }
    return resultString;
  }

  public void thirtySecondsAlert() {
//TODO
  }

  public void oneMinuteAlert() {
//TODO
  }

  public void playFeedBackIntegration(boolean successfulMove) {
    if (successfulMove) {
      scrabbleBoard.nextTurn();
    } else {
      removeChangedTiles();
    }
  }

  private void removeChangedTiles() {
    for (int x = 0; x < this.scrabbleBoard.newTilesOfCurrentMove.size(); x++) {
      Tile tile = this.scrabbleBoard.newTilesOfCurrentMove.get(x);
      tile.setStatus(Tilestatus.ONPLAYERRACK);
      tile.setXY(0, 0);
      this.player.putBackOnRack(tile);
    }
  }

  public void placeTilesOfOtherPlayers(Tile[] tiles) {
    for (int x = 0; x < tiles.length; x++) {
      this.scrabbleBoard.placeTile(tiles[x], tiles[x].getX(), tiles[x].getY());
      //nextTurn only clears the edited lists
      this.scrabbleBoard.nextTurn();
    }

  }

  //extended getter and setterr

  public void shuffleTiles(Tile[] oldTiles, Tile[] saveTiles) throws IOException {
    waitingForShuffledTiles = true;
    protocol.sendToServer(new ShuffleTilesMessage(from, oldTiles, saveTiles));
  }

  public void addPointsToPlayer(int points) {
    this.players[this.currentPlayer].addPoints(points);
  }

  public void receiveShuffleTiles(ReceiveShuffleTilesMessage message) {
    player.updateRack(message.getRack());
    waitingForShuffledTiles = false;
  }

  public void endMatch() {
    protocol.disconnect();
  }

  public void youWon() {
    this.youWon = true;
  }

  public void youLost() {
    this.youWon = true;
  }

  public void isOver() {
    isOver = true;
  }

  //getter and setter

  public int getMyNumber() {
    return myNumber;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public Player[] getPlayers() {
    return players;
  }

  public ClientProtocol getProtocol() {
    return protocol;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public ScrabbleBoard getScrabbleBoard() {
    return scrabbleBoard;
  }

  public void setScrabbleBoard(ScrabbleBoard scrabbleBoard) {
    this.scrabbleBoard = scrabbleBoard;
  }

  public Player getPlayer() {
    return player;
  }

  public void addProtocol(ClientProtocol cp) {
    this.protocol = cp;
  }

  //statics

  public String getFrom() {
    return from;
  }

  public void newGameEvent(String eventString) {
    this.gameEvents += "";
    this.gameEvents += "\n";
  }

}
