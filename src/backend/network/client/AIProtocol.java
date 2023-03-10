package backend.network.client;

import backend.ai.PlayerAI;
import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;
import backend.network.messages.connection.ConnectMessage;
import backend.network.messages.connection.DisconnectMessage;
import backend.network.messages.game.GameStartMessage;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.tiles.GetNewTilesMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Protocol for Ai's.
 *
 * @author nilschae
 */
public class AIProtocol extends Thread implements Serializable {

  private String username;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private PlayerAI ai;

  private boolean running = true;
  private Message lastMessage = new Message(MessageType.GAME_LOOSE, "");

  /**
   * Constructor for an Ai protocol.
   *
   * @param ip to connect.
   * @param port to connect.
   * @param username of the ai.
   * @param playerAi player which then can be stored.
   */
  public AIProtocol(String ip, int port, String username, PlayerAI playerAi) {
    try {
      this.username = username;
      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());
      this.ai = playerAi;

      ConnectMessage connectMessage = new ConnectMessage(this.username, playerAi);
      connectMessage.setMessageType(MessageType.CONNECT_AI);
      this.out.writeObject(connectMessage);
      out.flush();
      out.reset();
      System.out.println("Local Port (Client): " + this.clientSocket.getLocalPort());

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

  public boolean isStable() {
    return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
  }

  /** process the incoming messages from the server. */
  public void run() {
    while (running) {
      try {
        Message message = (Message) in.readObject(); // the message input from the server

        if (message != null && lastMessage != null && !lastMessage.equals(message)) {
          switch (message.getMessageType()) {
            case GAME_INFO:
              ai.handleStartGame(((LobbyInformationMessage) message).getPlayers());
              break;

            case GAME_START:
              ai.handleGameStartMessage(((GameStartMessage) message).getTiles());
              break;

            case GAME_TURN:
              ai.handleGameTurnMessage(((GameTurnMessage) message).getNowTurn());
              break;

            case GET_NEW_TILES:
              ai.acceptNewTiles(((GetNewTilesMessage) message).getTiles());
              break;

            case PLACE_TILES:
              ai.placeTilesFromServer(((PlaceTilesMessage) message).getTiles());
              break;
            case PLAY_FEEDBACK:
              PlayFeedbackMessage feedbackMessage = (PlayFeedbackMessage) message;
              if (!feedbackMessage.isSuccessfulMove()) {
                ai.acceptNewTiles(ai.getLastTilesSent().toArray(new Tile[0]));
                ai.handleTurn();
              } else if (feedbackMessage.isSuccessfulMove()) {
                ai.flushTried();
                ai.validWordConfirmation();
              }
              break;

            default:
              break;
          }
          lastMessage = message;
        } else {
          System.out.println("Dublicate message catched");
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

  public boolean isRunning() {
    return running;
  }

  /** Send messages from client to server. */
  public void sendToServer(Message message) throws IOException {
    this.out.writeObject(message);
    out.flush();
    out.reset();
  }
}
