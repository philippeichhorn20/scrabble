package backend.basic;

import backend.basic.Tile.Tilestatus;
import backend.network.client.ClientProtocol;
import backend.network.messages.MessageType;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.text.TextMessage;
import backend.network.messages.tiles.PassMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import frontend.screens.controllers.GameScreenController;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This class performs the game actions on the Client Side.
 *
 * @author peichhor
 * @description
 */
public class ClientMatch {

  private static final int round = 0; //keeps track of the rounds, that have been played
  private final Player player;
  private final String from;  // a String which is used to identifiy the player in Messages
  private final boolean youLost = false;
  private final boolean yourTurn = false;
  Timer timer = new Timer(this);
  private Player[] players;   //stores the up to 4 players who are participating in the game
  private ClientProtocol protocol;
  private ScrabbleBoard scrabbleBoard;
  private int currentPlayer = 0;
  private int myNumber;
  private boolean isOver = false;
  private boolean youWon = false;
  private boolean waitingForShuffledTiles = false;
  private int yourTurnNum;
  private String gameEvents = "";    // stores the gameEvents that will be visible to the player
  private Tile[] newTilesToBeAdded;   //stores Tiles to be accessed from Controller
  private boolean invalidMove = true;
  private GameScreenController gameScreenController;
  private boolean dropTiles = false;
  private Tile[] newTilesOnRack;
  private ArrayList<String> textMessages = new ArrayList<>();
  private boolean startingTiles;


  /**
   * sets up the ClientsMatch if the players are already in the lobby.
   *
   * @param protocol the protocol of the client
   * @param players  the array of players in the game
   * @param from     the String that references the player in server messages
   * @param player   the player instance of the player that holds this match
   * @author peichhor
   */
  public ClientMatch(ClientProtocol protocol, Player[] players, String from, Player player) {
    super();
    this.player = player;
    this.protocol = protocol;
    this.players = players;
    this.from = from;
    this.scrabbleBoard = new ScrabbleBoard();
  }

  /**
   * sets up the ClientsMatch without initializing the players yet.
   *
   * @param from   the String that references the player in server messages
   * @param player the player instance of the player that holds this match
   */
  public ClientMatch(String from, Player player) {
    super();
    this.player = player;
    this.from = from;
    this.players = new Player[4];
    this.players[0] = player;
    this.scrabbleBoard = new ScrabbleBoard();
  }

  /**
   * yourTurn this method resets temporary variables of the scrabble board it resets the timer of
   * the current turn, but keeps overall time running informes the player, that it is his turn.
   */
  public void yourTurn() {
    yourTurnNum = currentPlayer;
    scrabbleBoard.nextTurn();
    this.writeTextMessages("Make your move now!");
  }

  /**
   * called when GameTurnMessage is received. If it is his turn, it calls yourTurn, otherwise it
   * just informs the player about the new turn
   *
   * @param nowTurn the number in the array of the player with current turn
   */

  public void turnTaken(int nowTurn) {
    timer.setTimerTo(nowTurn);
    currentPlayer = nowTurn;
    if (players[currentPlayer].getName()
        .equals(GameInformation.getInstance().getProfile().getName())) {
      yourTurn();
    } else {
      this.gameScreenController.showServerMessage(
          "It is now " + players[currentPlayer].getName().substring(0, 1).toUpperCase()
              + players[currentPlayer].getName().substring(1).toLowerCase() + "'s turn!", 5);
    }
  }


  /**
   * checks, weather tiles were placed. If so, send them as Array to server. Else: calls pass
   * method
   */

  public void sendPlacedTilesToServer() {
    if (this.scrabbleBoard.newTilesOfCurrentMove.size() != 0) {
      Tile[] tiles = new Tile[this.scrabbleBoard.newTilesOfCurrentMove.size()];
      for (int x = 0; x < this.scrabbleBoard.newTilesOfCurrentMove.size(); x++) {
        tiles[x] = this.scrabbleBoard.newTilesOfCurrentMove.get(x);
        System.out.println(tiles[x].getValue());
      }
      try {
        ClientProtocol protocol = GameInformation.getInstance().getClientmatch().getProtocol();
        protocol.sendToServer(new PlaceTilesMessage(this.player.getName(), tiles));
      } catch (IOException e) {
        this.gameScreenController
            .showServerMessage("Your message could not be send, please try again", 3);
      }
    } else {
      if (!this.gameScreenController.currPlayerText.getText().equals("Your Turn")) {
        //do nothing
      } else {
        try {
          this.pass();
        } catch (IOException ioe) {
          this.gameScreenController
              .showServerMessage("Your message could not be send, please try again", 3);
        }
      }

    }

  }

  /**
   * sends a chat message over the client protocol.
   *
   * @param textMessage the content of the message
   */
  public void sendChatMessage(String textMessage) {
    System.out.println("Sending Relay Message to Server");
    try {
      GameInformation.getInstance().getClientmatch().getProtocol()
          .sendToServer(new TextMessage(MessageType.RELAY, this.player.getName(), textMessage));
    } catch (IOException e) {
      System.err.println("Could not send Relay Message to Server");
    }
  }

  /**
   * this method finds the next player in players array and initializes it in the current turn
   * variable.
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
    scrabbleBoard.nextTurn();
  }

  /**
   * Method is called, when player decides to not do anything this turn. It informes the servermatch
   * about his choice
   *
   * @throws IOException if sending was not sucessful
   */
  public void pass() throws IOException {
    protocol.sendToServer(new PassMessage("server"));
  }

  /**
   * informs client about the remaining time on his clock (60 Seconds).
   */
  public void thirtySecondsAlert() {
    writeTextMessages("60 seconds left");
  }

  /**
   * informs client about the remaining time on his clock (30 Seconds).
   */
  public void oneMinuteAlert() {
    writeTextMessages("60 seconds left");
  }


  /**
   * method checks if move was successful and informs the player about it. It performs the game
   * moves depending on the input.
   *
   * @param message keeps information about weather the move was sucessful, as well as a String to
   *                explain it to the player
   */
  public void playFeedBackIntegration(PlayFeedbackMessage message) {
    if (message.isSuccessfulMove()) {
      this.textMessages.addAll(message.getFeedback());
      scrabbleBoard.nextTurn();
      invalidMove = false;
    } else if (message.getFeedback() == null) {
      this.gameScreenController.showServerMessage("please place the tiles properly", 3);
    } else {
      System.out.println("removing tiles");
      System.out.println(this.textMessages.size());
      System.out.println(message.getFeedback());
      this.textMessages.addAll(message.getFeedback());
      removeChangedTiles();
    }
  }

  /**
   * puts all the tiles back on the players rack.
   */
  private void removeChangedTiles() {
    for (int x = 0; x < this.scrabbleBoard.newTilesOfCurrentMove.size(); x++) {
      Tile tile = this.scrabbleBoard.newTilesOfCurrentMove.get(x);
      tile.setStatus(Tilestatus.ONPLAYERRACK);
      tile.setXY(0, 0);
      this.player.putBackOnRack(tile);
      this.invalidMove = true;
    }
    this.setDropTiles(true);
  }

  /**
   * tells the gamescreencontroller to place tiles on the visual (Screen) and abstract
   * (scrabbleBoard instance) representation of the scrabbleboard.
   *
   * @param tiles the tiles that were placed
   */
  public void placeTilesOfOtherPlayers(Tile[] tiles) {
    this.newTilesToBeAdded = tiles;
    for (Tile tile : tiles) {
      this.scrabbleBoard.placeTile(tile, tile.getX(), tile.getY());
      this.scrabbleBoard.nextTurn();
    }
  }

  //extended getter and setterr

  /**
   * sends tiles to server top shuffle.
   *
   * @param oldTiles the old tiles which will be replaced by new ones
   * @throws IOException wenn senden nich erfolgreich werden
   */
  public void shuffleTiles(Tile[] oldTiles) throws IOException {
    waitingForShuffledTiles = true;
    protocol.sendToServer(new ShuffleTilesMessage(this.player.getName(), oldTiles));
  }

  /**
   * adds the shuffled tiles to the Game.
   *
   * @param tiles the new tiles received from the server
   */
  public void receiveShuffleTiles(Tile[] tiles) {
    newTilesOnRack = tiles;
    waitingForShuffledTiles = false;
  }

  /**
   * adds points to the overall point count of the player instance, which hold this client match.
   *
   * @param points the points of the players current move
   */
  public void addPointsToPlayer(int points) {
    this.players[this.currentPlayer].addPoints(points);
  }

  /**
   * adds a message to ann array which is read by the gamescreen thread. The thread will then make
   * it visible to the client on the interface.
   *
   * @param textMessage the content of the message
   */
  public void writeTextMessages(String textMessage) {
    this.textMessages.add(textMessage);
  }

  /**
   * disconnects the player from the match at the end.
   */
  public void endMatch() {
    protocol.disconnect();
  }

  /**
   * returns if or not the game is over.
   *
   * @return weather or not the game is over
   */
  public boolean isOver() {
    return isOver;

  }

  //getter and setter

  public void setOver(boolean b) {
    this.isOver = b;
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

  public void setPlayers(Player[] players) {
    this.players = players;
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


  public String getCurrentPlayerName() {
    return this.players[this.currentPlayer].getName();
  }

  public Tile[] getNewTilesToBeAdded() {
    return newTilesToBeAdded;
  }

  public void dropNewTiles() {
    newTilesToBeAdded = null;
  }

  public boolean hasNewTiles() {
    return newTilesToBeAdded != null;

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


  /**
   * gets the tiles that were new and therefore added to the rack.
   *
   * @return the tiles as an array
   */
  public Tile[] getNewTilesOnRack() {
    if (newTilesOnRack == null) {
      return null;
    } else if (newTilesOnRack.length == 0) {
      return null;
    }
    return newTilesOnRack;
  }


  public ArrayList<String> getTextMessages() {
    return textMessages;
  }

  public void setTextMessages(ArrayList<String> textMessages) {
    this.textMessages = textMessages;
  }

  public GameScreenController getGameScreenController() {
    return gameScreenController;
  }

  public void setGameScreenController(
      GameScreenController gameScreenController) {
    this.timer.setGameScreenController(gameScreenController);
    this.gameScreenController = gameScreenController;
  }

  public void clearNewTilesOnRack() {
    this.newTilesOnRack = null;
  }

  public Timer getTimer() {
    return this.timer;
  }

  public boolean isInvalidMove() {
    return invalidMove;
  }

  public void setInvalidMove(boolean invalidMove) {
    this.invalidMove = invalidMove;
  }

  public boolean dropTiles() {
    return this.dropTiles;
  }

  public void setDropTiles(boolean dropTiles) {
    this.dropTiles = dropTiles;
  }

  public boolean isStartingTiles() {
    return startingTiles;
  }

  public void setStartingTiles(boolean startingTiles) {
    this.startingTiles = startingTiles;
  }
}
