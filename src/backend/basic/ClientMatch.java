package backend.basic;

import backend.basic.Tile.Tilestatus;
import backend.network.client.ClientProtocol;
import backend.network.messages.MessageType;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.text.TextMessage;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import frontend.screens.controllers.GameScreenController;
import java.io.IOException;

/*
  @peichhor
  @description This class performs the game actions on the Client Side.
 */
public class ClientMatch {

  private static final int round = 0; //keeps track of the rounds, that have been played
  private Player[] players;   //stores the up to 4 players who are participating in the game
  Timer timer = new Timer();
  private final Player player;
  private ClientProtocol protocol;
  private final String from;  // a String which is used to identifiy the player in Messages
  private ScrabbleBoard scrabbleBoard;
  private final boolean youLost = false;
  private int currentPlayer = 0;
  private int myNumber;
  private boolean isOver = false;
  private boolean youWon = false;
  private boolean waitingForShuffledTiles = false;
  private final boolean yourTurn = false;
  private int yourTurnNum;
  private String gameEvents = "";    // stores the gameEvents that will be visible to the player
  private Tile[] newTilesToBeAdded;   //stores Tiles to be accessed from Controller
  private boolean invalidMove = true;
  private GameScreenController gameScreenController;
  private boolean dropTiles = false;
  private Tile[] newTilesOnRack;


  public ClientMatch(ClientProtocol protocol, Player[] players, String from, Player player) {
    super();
    this.player = player;
    this.protocol = protocol;
    this.players = players;
    this.from = from;
    this.scrabbleBoard = new ScrabbleBoard();
  }

  public ClientMatch(String from, Player player) {
    super();
    this.player = player;
    this.from = from;
    this.players = new Player[4];
    this.players[0] = player;
    this.scrabbleBoard = new ScrabbleBoard();
  }

  public static int getRound() {
    return round;
  }

  /*
  @method yourTurn
  this method resets temporary variables of the scrabble board
  it resets the timer of the current turn, but keeps overall time running
  informes the player, that it is his turn
   */
  public void yourTurn() {
    yourTurnNum = currentPlayer;
    scrabbleBoard.nextTurn();
    this.getTimer().nextPlayer();
    this.gameScreenController.showServerMessage("It is now your turn, you have 10 minutes to make your move", 3);
  }

  /*
@method yourTurn
called when GameTurnMessage is received. If it is his turn, it calls yourTurn,
otherwise it just informs the player about the new turn
 */
  public void turnTaken(int nowTurn){
    currentPlayer = nowTurn;
    if(players[currentPlayer].getName().equals(GameInformation.getInstance().getProfile().getName())){
      yourTurn();
    }else{
      this.gameScreenController.showServerMessage("It is now " + players[currentPlayer].getName()+ "'s turn!", 5);
    }
  }


  /*
  @method sendPlacedTilesToServer
  checks, weather tiles were placed. If so, send them as Array to server. Else: calls pass method
   */
  public void sendPlacedTilesToServer(){
    if(this.scrabbleBoard.newTilesOfCurrentMove.size() != 0){
      Tile[] tiles = new Tile[this.scrabbleBoard.newTilesOfCurrentMove.size()];
      for(int x = 0; x < this.scrabbleBoard.newTilesOfCurrentMove.size(); x++){
        tiles[x] = this.scrabbleBoard.newTilesOfCurrentMove.get(x);
      }
      try{
        GameInformation.getInstance().getClientmatch().getProtocol().sendToServer(new PlaceTilesMessage(this.player.getName(), tiles));
      }catch(IOException e){
        this.gameScreenController.showServerMessage("Your message could not be send, please try again",3);
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

  /*
  @method
  this method finds the next player in players array
   */
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
  }

  // Method is called, when player decides to not do anything this turn
  public void pass() throws IOException {
    protocol.sendToServer(new PassMessage("server"));
  }


  /*
  @method thirtySecondsAlert, oneMinuteAlert
  informs client about the remaining time
   */
  public void thirtySecondsAlert() {
    newGameEvent(players[currentPlayer].getName()+ " has 30 seconds left");
  }

  public void oneMinuteAlert() {
    newGameEvent(players[currentPlayer].getName()+ " has 60 seconds left");
  }


  /*
  @method playFeedBackIntegration
  method checks if move was successful and informes the player about it
 */
  public void playFeedBackIntegration(PlayFeedbackMessage message) {
    if (message.isSuccessfulMove()) {
      this.gameScreenController.showServerMessage(message.getFeedback(),3);
      scrabbleBoard.nextTurn();
    } else {
      System.out.println("You FUCKED UP");
      this.gameScreenController.showServerMessage(message.getFeedback(),3);
      removeChangedTiles();
    }
  }

  /*
@method removeChangedTiles
puts all the tiles back on the players rack*/
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

  /*
  @method placeTilesOfOtherPlayers
  tells the gamescreencontroller to place tiles on the visual (Screen) and
  abstract (scrabbleBoard instance) representation of the scrabbleboard
   */
  public void placeTilesOfOtherPlayers(Tile[] tiles) {
    this.newTilesToBeAdded = tiles;
    for (int x = 0; x < tiles.length; x++) {
      this.scrabbleBoard.placeTile(tiles[x], tiles[x].getX(), tiles[x].getY());
      this.scrabbleBoard.nextTurn();
    }
  }

  //extended getter and setterr
/*
@method shuffleTiles
sends tiles to server top shuffle
 */
  public void shuffleTiles(Tile[] oldTiles) throws IOException {
    waitingForShuffledTiles = true;
    protocol.sendToServer(new ShuffleTilesMessage(this.player.getName(), oldTiles));
  }


  public void addPointsToPlayer(int points) {
    this.players[this.currentPlayer].addPoints(points);
  }

  /*
  @method shuffleTiles
  adds the shuffled tiles to the Game
   */
  public void receiveShuffleTiles(Tile[] tiles) {
    System.out.println("receiveShuffleTiles "+ tiles.length);
    newTilesOnRack = tiles;
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
    //this.gameEvents = eventString;
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


  public Tile[] getNewTilesOnRack() {
    if(newTilesOnRack == null){
      return null;
    }else if(newTilesOnRack.length ==0){
      return null;
    }
    return newTilesOnRack;
  }

  public void clearNewTilesOnRack(){
    this.newTilesOnRack = null;
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

  /*
  performs the actions when the Game_Over message is received
   */

  public void endGame(){

  }
}
