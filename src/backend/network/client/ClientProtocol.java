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
 * A protocol for a client.
 *
 * @author nilschae
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
   * A constructer for a clientprotokol.
   *
   * @param ip to connect.
   * @param port to connect.
   * @param username of the owner of the clientprotocol.
   * @param match a clientmatch.
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
      System.out.println("Local Port (Client): " + this.clientSocket.getLocalPort());
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

  public boolean isStartingTiles() {
    return startingTiles;
  }

  public void setStartingTiles(boolean startingTiles) {
    this.startingTiles = startingTiles;
  }

  public Tile[] getStartingRack() {
    return startingRack;
  }

  public void setStartingRack(Tile[] startingRack) {
    this.startingRack = startingRack;
  }

  public boolean isStable() {
    return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
  }

  /** The run methode of the server which receive the messages. */
  public void run() {
    while (running) {
      try {
        Message message = (Message) in.readObject(); // the message input from the server

        if (message != null && lastMessage != null && !lastMessage.equals(message)) {
          switch (message.getMessageType()) {
            case CONNECTION_REFUSED:
              System.out.println("Connection closed, since connection refused");
              ConnectionRefusedMessage connectionRefusedMessage =
                  (ConnectionRefusedMessage) message;
              System.out.println(connectionRefusedMessage.getErrorMessage());
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
              break;

            case GAME_LOOSE:
              // TODO At game controller there must be a methode which show
              // that the player lost
              this.match.getGameScreenController().showServerMessage("You Loose", 10);
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
              for (Player p : GameInformation.getInstance().getClientmatch().getPlayers()) {
                System.out.print(p + " ");
              }
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
              this.match.receiveShuffleTiles(message3.getTiles());
              this.match.setStartingTiles(true);
              Main m = new Main();
              m.changeScene("screens/gameScreen.fxml");
              break;

            case SEND_RACK_POINTS:
              // TODO At game controller there must be a methode which
              // calculate the points left on the rack

              // Why and also when?
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

            case TEXT:
              TextMessage textMessage = (TextMessage) message;
              String sender = textMessage.getFrom();
              String text = textMessage.getText();
              GameInformation.getInstance().getChat().fillTextArea(sender, text);
              break;

            case HISTORY:
              HistoryMessage historyMessage = (HistoryMessage) message;
              historyMess = historyMessage.getMessage();
              messChange = true;
              break;

            case SEND_START_RACK:
              SendStartRackMessage ssrMessage = (SendStartRackMessage) message;
              match.setStartingTiles(true);
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

  /** Disconnect the client from the server. */
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

  public String getHistoryMessage() {
    return historyMess;
  }

  public boolean messageChanged() {
    return messChange;
  }

  public void messageRead() {
    messChange = false;
  }

  public boolean isRunning() {
    return running;
  }

  /**
   * Send messages from client to server.
   *
   * @param message which will be send.
   */
  public void sendToServer(Message message) throws IOException {
    this.out.writeObject(message);
    out.flush();
    out.reset();
  }

  public ClientMatch getMatch() {
    return this.match;
  }
}
