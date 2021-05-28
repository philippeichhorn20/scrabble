package backend.basic;

import backend.basic.Tile.Tilestatus;
import backend.network.client.ClientProtocol;
import backend.network.messages.MessageType;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.text.TextMessage;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import frontend.screens.controllers.GameScreenController;
import java.io.IOException;

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
  private Player[] players;
  Timer timer = new Timer();
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
  private Tile[] newTilesToBeAdded;
  private boolean invalidMove;
  private GameScreenController gameScreenController;
  private boolean dropTiles = false;


  public ClientMatch(ClientProtocol protocol, Player[] players, String from, Player player) {
    super();
    this.player = player;
    this.protocol = protocol;
    this.players = players;
    this.from = from;
    this.scrabbleBoard = new ScrabbleBoard();
    //this.timer.start();
  }

  public ClientMatch(String from, Player player) {
    super();
    this.player = player;
    this.from = from;
    this.players = new Player[4];
    this.players[0] = player;
    this.scrabbleBoard = new ScrabbleBoard();
    //this.timer.start();
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



  public void yourTurn() {
    yourTurnNum = currentPlayer;
    scrabbleBoard.nextTurn();
    this.getTimer().nextPlayer();
    this.gameScreenController.showServerMessage("It is now your turn, you have 10 minutes to make your move", 3);
  }

  public void turnTaken(int nowTurn){
    currentPlayer = nowTurn;
    System.out.println("Player with turn: " + players[currentPlayer].getName() + " | " + nowTurn);
    if(players[currentPlayer].getName().equals(GameInformation.getInstance().getProfile().getName())){
      yourTurn();
    }else{
      this.gameScreenController.showServerMessage("It is now " + players[currentPlayer].getName()+ "'s turn!", 5);
    }
  }

  public void sendPlacedTilesToServer(){
    System.out.println("sending "+this.scrabbleBoard.newTilesOfCurrentMove.size()+" tiles to server");
    if(this.scrabbleBoard.newTilesOfCurrentMove.size() != 0){
      Tile[] tiles = new Tile[this.scrabbleBoard.newTilesOfCurrentMove.size()];
      for(int x = 0; x < this.scrabbleBoard.newTilesOfCurrentMove.size(); x++){
        tiles[x] = this.scrabbleBoard.newTilesOfCurrentMove.get(x);
        System.out.println("value at client" + tiles[x].getValue());
      }
      try{
        GameInformation.getInstance().getClientmatch().getProtocol().sendToServer(new PlaceTilesMessage(this.player.getName(), tiles));
        System.out.println("sent");
      }catch(IOException e){
        this.gameScreenController.showServerMessage("Your message could not be send, please try again",3);
        System.out.println("couldnt send your tiles to server");
      }
    }else{
      try {
        this.pass();
      }catch (IOException ioe){
        this.gameScreenController.showServerMessage("Your message could not be send, please try again", 3);
      }
    }

  }
  public void sendHistoryMessage(String from,String mess){
    System.out.println("ClientMatch History Message");
    try{
      GameInformation.getInstance().getClientmatch().getProtocol().sendToServer(new HistoryMessage(from,mess));
    }catch (IOException e){
      System.out.println("whoops");
    }
  }
  /**
   * @author vivanova
   */
  public void sendChatMessage(String textMessage) {
	  System.out.println("Sending Relay Message to Server"); 
	  try {
		GameInformation.getInstance().getClientmatch().getProtocol().sendToServer(new TextMessage(MessageType.RELAY,this.player.getName(),textMessage));
	} catch (IOException e) {
		System.err.println("Could not send Relay Message to Server");
	}
  }

  public void nextPlayer() {
    int notActivePlayers = 0;
    int nextPlayer = 4 % (currentPlayer + 1);
    do {
      notActivePlayers++;
      nextPlayer = 4 % (nextPlayer + 1);
    } while (notActivePlayers < 4 && players[nextPlayer] != null);

    if (this.checkTimer()) {
      currentPlayer = nextPlayer;
    }
    this.getTimer().nextPlayer();
    scrabbleBoard.nextTurn();
 //   newGameEvent("It is now " + players[currentPlayer].getName() + "'s turn");
  }

  // Method is called, when player decides to not do anything this turn
  public void pass() throws IOException {
    protocol.sendToServer(new PassMessage("server"));
  }


  /*
  public String submitTilesOfClient(String from) throws IOException {
    String[][] result = scrabbleBoard.submitTiles(from);
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
          new PlaceTilesMessage(player.name, tileArrayToList(this.scrabbleBoard.newTilesOfCurrentMove)));
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

*/

  public void thirtySecondsAlert() {
    newGameEvent(players[currentPlayer].getName()+ " has 30 seconds left");
  }

  public void oneMinuteAlert() {
    newGameEvent(players[currentPlayer].getName()+ " has 60 seconds left");
  }

  public void playFeedBackIntegration(PlayFeedbackMessage message) {
    if (message.isSuccessfulMove()) {
      this.gameScreenController.showServerMessage(message.getFeedback(),7);
      scrabbleBoard.nextTurn();
      //GameInformation.getInstance().getGsc().endTurnB();
    } else {
      this.gameScreenController.showServerMessage(message.getFeedback(),3);
      //scrabbleBoard.nextTurn();
      removeChangedTiles();
    }
  }

  private void removeChangedTiles() {
    this.dropTiles();
    for (int x = 0; x < this.scrabbleBoard.newTilesOfCurrentMove.size(); x++) {
      Tile tile = this.scrabbleBoard.newTilesOfCurrentMove.get(x);
      tile.setStatus(Tilestatus.ONPLAYERRACK);
      tile.setXY(0, 0);
      this.player.putBackOnRack(tile);
      this.invalidMove = true;
    }
  }

  public void placeTilesOfOtherPlayers(Tile[] tiles) {
    this.newTilesToBeAdded = tiles;
    for (int x = 0; x < tiles.length; x++) {
      this.scrabbleBoard.placeTile(tiles[x], tiles[x].getX(), tiles[x].getY());
      System.out.print("- " + tiles[x].getLetter());
      this.scrabbleBoard.nextTurn();
    }
  }

  //extended getter and setterr

  public void shuffleTiles(Tile[] oldTiles, Tile[] saveTiles) throws IOException {
    waitingForShuffledTiles = true;
    protocol.sendToServer(new ShuffleTilesMessage(from, oldTiles));
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

  public boolean isOver() {
    return isOver;

  }
  public void setOver(boolean b){
    this.isOver = b;
  }

  //getter and setter

  public void setPlayers(Player[] players){
    this.players = players;
  }

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
    this.gameScreenController.newHistoryMessage(eventString);
  }

  public String getCurrentPlayerName() {return this.players[this.currentPlayer].getName();}

  public Tile[] getNewTilesToBeAdded() {
    return newTilesToBeAdded;
  }

  public void dropNewTiles(){
    newTilesToBeAdded = null;
  }
  public boolean hasNewTiles(){
    if(newTilesToBeAdded == null){
      return false;
    }else{
      return true;
    }

  }

  public void setTimerToZero() {
    this.timer.setTimerOverall(0);
  }

  public boolean checkTimer() {
    return this.timer.getTimerCurrentPlayer() > 0;
  }

  public void setTimerPersonalTimerToZero() {
    this.timer.setTimerCurrentPlayer(0);
  }



  public Timer getTimer() {
    return this.timer;
  }

  public void setInvalidMove(boolean invalidMove) {
    this.invalidMove = invalidMove;
  }

  public boolean isInvalidMove() {
    return invalidMove;
  }

  public boolean dropTiles(){
    return this.dropTiles;
  }

  public void setDropTiles(boolean dropTiles) {
    this.dropTiles = dropTiles;
  }

  public void setGameScreenController(
      GameScreenController gameScreenController) {
    this.timer.setGameScreenController(gameScreenController);
    this.gameScreenController = gameScreenController;
  }

  public GameScreenController getGameScreenController() {
    return gameScreenController;
  }
}
