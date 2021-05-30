package backend.network.client;

import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;
import backend.network.messages.connection.ConnectMessage;
import backend.network.messages.connection.ConnectionRefusedMessage;
import backend.network.messages.connection.DisconnectMessage;
import backend.network.messages.game.GameStartMessage;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.points.SendPointsMessage;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.text.TextMessage;
import backend.network.messages.tiles.GetNewTilesMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.messages.tiles.SendStartRackMessage;
import backend.network.messages.time.TimeAlertMessage;
import frontend.Main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client protocol is responsible for reading the messages that were sent from the server and
 * interpreting them in the right way so that the game works.
 */
public class ClientProtocol extends Thread {

  private String username;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private ClientMatch match;
  private String historyMess;
  private boolean messChange = false;
  private Player clientPlayer;
  private boolean startingTiles;
  private Tile[] startingRack;
  private boolean running = true;
  private Message lastMessage = new Message(MessageType.GAME_LOOSE, "");

  /**
   * Constructor.
   *
   * @param ip The ip needed for the client socket.
   * @param port The port needed for the client socket.
   * @param username Client's username.
   * @param match Clientmatch.
   */
  public ClientProtocol(String ip, int port, String username, ClientMatch match) {
    try {
      this.username = username;
      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.clientPlayer =
          new Player(
              username,
              Main.profile.getColor(),
              Main.profile.getGames(),
              Main.profile.getWins(),
              Playerstatus.WAIT);

      this.out.writeObject(new ConnectMessage(this.username, clientPlayer));
      out.flush();
      out.reset();
      this.match = match;

    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.out.println(
          "Could not establish connection to "
              + ip
              + ":"
              + port
              + ".\n"
              + "Please make sure the ip is correct and the server is online");
    }
  }

  /**
   * Getter for boolean that tells the client match if the client has gotten his starting tiles.
   *
   * @return boolean
   */
  public boolean isStartingTiles() {
    return startingTiles;
  }

  /**
   * Setter for starting tiles, sets to true if the game has started and the message with starting
   * tiles came through.
   *
   * @param startingTiles boolean.
   */
  public void setStartingTiles(boolean startingTiles) {
    this.startingTiles = startingTiles;
  }

  /**
   * Getter of the starting tiles.
   *
   * @return starting tiles.
   */
  public Tile[] getStartingRack() {
    return startingRack;
  }

  /**
   * Setter of the starting tiles.
   *
   * @param startingRack starting tiles.
   */
  public void setStartingRack(Tile[] startingRack) {
    this.startingRack = startingRack;
  }

  /**
   * Returns whether the socket is connected correctly.
   *
   * @return boolean.
   */
  public boolean isStable() {
    return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
  }

  /** Processes the incoming messages from the server. */
  public void run() {
    while (running) {
      try {
        Message message = (Message) in.readObject(); // the message input from the server

        if (message != null && lastMessage != null && !lastMessage.equals(message)) {
          switch (message.getMessageType()) {
            case CONNECTION_REFUSED:
              ConnectionRefusedMessage connectionRefusedMessage =
                  (ConnectionRefusedMessage) message;
              disconnect();
              break;

            case SERVER_SHUTDOWN:
              disconnect();
              break;

            case SEND_ID:
              // TODO At game controller there must be a methode which receive ID's
              // for example for a tile
              break;

            case GAME_TURN:
              GameTurnMessage turnMessage = (GameTurnMessage) message;
              GameInformation.getInstance().getClientmatch().turnTaken(turnMessage.getNowTurn());
              break;

            case PLAY_FEEDBACK:
              PlayFeedbackMessage message6 = (PlayFeedbackMessage) message;
              this.match.playFeedBackIntegration(message6);
              break;

            case GAME_OVER:
              // TODO At game controller there must be a methode which show
              // the player that the game is over
              this.match.getGameScreenController().showServerMessage("Game over", 10);
              this.match.setOver(true);
              break;

            case GAME_WIN:
              // TODO At game controller there must be a methode which show
              // that the player won
              this.match
                  .getGameScreenController()
                  .showServerMessage(
                      "Congrats, you won with " + this.match.getPlayer().getScore() + " points!",
                      10);
              this.match.youWon();
              break;

            case GAME_LOOSE:
              // TODO At game controller there must be a methode which show
              // that the player lost
              this.match.getGameScreenController().showServerMessage("You Loose", 10);
              this.match.youLost();
              break;

            case GAME_PLACEMENT:
              // TODO At game controller there must be a methode which show
              // the player the placement

              // redundant, by sending out the Player info, this info can be taken from Game Lobby
              break;

              // initialized the game with the lobby information
            case GAME_INFO:
              LobbyInformationMessage message1 = (LobbyInformationMessage) message;
              GameInformation.getInstance().getClientmatch().setPlayers(message1.getPlayers());
              GameInformation.getInstance().setPlayers(message1.getPlayers());

              break;

            case SEND_POINTS:
              // TODO At game controller there must be a methode which add
              // points to the player statistics
              SendPointsMessage message2 = (SendPointsMessage) message;
              this.match.writeTextMessages(
                  message2.getFrom().substring(0, 1).toUpperCase()
                      + message2.getFrom().substring(1).toLowerCase()
                      + " got "
                      + message2.getPoints()
                      + " points with his latest move");
              this.match.addPointsToPlayer(message2.getPoints());
              break;

            case GAME_START:
              // TODO At game controller there must be a methode which add
              // points to the player statistics
              GameStartMessage message3 = (GameStartMessage) message;
              this.match.getPlayer().updateRack(message3.getTiles());
              this.match.getTimer().start();
              Main m = new Main();
              m.changeScene("screens/gameScreen.fxml");
              break;

            case PLACE_TILES:
              PlaceTilesMessage message4 = (PlaceTilesMessage) message;
              GameInformation.getInstance()
                  .getClientmatch()
                  .placeTilesOfOtherPlayers(message4.getTiles());
              break;

            case RECEIVE_SHUFFLE_TILES:
              ReceiveShuffleTilesMessage receiveShuffleTilesMessage =
                  (ReceiveShuffleTilesMessage) message;
              if (receiveShuffleTilesMessage.getFrom().equals("")) {
                match.writeTextMessages("not enough tiles in bag to shuffle");
              }
              match.receiveShuffleTiles(receiveShuffleTilesMessage.getRack());
              break;

            case GET_NEW_TILES:
              GetNewTilesMessage getNewTilesMessage = (GetNewTilesMessage) message;
              match.receiveShuffleTiles(getNewTilesMessage.getTiles());
              break;

            case TIME_ALERT:
              TimeAlertMessage timeAlertMessage = (TimeAlertMessage) message;
              switch (timeAlertMessage.getAlertType()) {
                case TIME_OVER:
                  match
                      .getGameScreenController()
                      .showServerMessage("Your time is up, moving on.", 5);
                  break;
                case TIMER_STARTED:
                  match.setTimerPersonalTimerToZero();
                  break;
                case ONE_MINUTE_LEFT:
                  match.oneMinuteAlert();
                  break;
                case THIRTY_SECONDS_LEFT:
                  match.thirtySecondsAlert();
                  break;
                default:
                  break;
              }

              break;

            case TIME_SYNC:
              // it nulls the timer
              this.match.setTimerToZero();
              break;
              //author @vivanova
            case TEXT:
              TextMessage textMessage = (TextMessage) message;
              String sender = textMessage.getFrom();
              String text = textMessage.getText();
              GameInformation.getInstance().getChat().fillTextArea(sender, text);
              /* Call Text Box to add new Text */
              break;
            case HISTORY:
              HistoryMessage historyMessage = (HistoryMessage) message;
              historyMess = historyMessage.getMessage();
              messChange = true;
              break;
            case SEND_START_RACK:
              SendStartRackMessage ssrMessage = (SendStartRackMessage) message;
              setStartingTiles(true);
              setStartingRack(ssrMessage.getTiles());
              break;
            default:
              break;
          }
          lastMessage = message;
        } else {
          System.out.println("Duplicate message caught");
        }
      } catch (ClassNotFoundException | IOException e) {
        break;
      }
    }
  }

  /**
   * Disconnects the player from the server.
   */
  public void disconnect() {
    running = false;
    try {
      if (!clientSocket.isClosed()) {
        this.out.writeObject(new DisconnectMessage(this.username));
        clientSocket.close(); // close streams and socket
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter of the new history message.
   *
   * @return history message string.
   */

  public String getHistoryMessage() {
    return historyMess;
  }

  /**
   * Returns whether a new history message arrived.
   *
   * @return boolean.
   */

  public boolean messageChanged() {
    return messChange;
  }

  /**
   * Sets the new message as read - ergo false.
   *
   */
  public void messageRead() {
    messChange = false;
  }

  /**
   * Returns whether the client protocol is still running.
   *
   * @return boolean
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Sends messages from client to server.
   *
   * @param message the message being sent.
   * @throws IOException if out is messed up.
   */
  public void sendToServer(Message message) throws IOException {
    this.out.writeObject(message);
    out.flush();
    out.reset();
  }

  /**
   * Getter of this protocol's client match.
   *
   * @return ClientMatch.
   */
  public ClientMatch getMatch() {
    return this.match;
  }
}
