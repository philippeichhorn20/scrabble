package backend.network.client;

import backend.basic.ClientMatch;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.network.messages.Message;
import backend.network.messages.connection.ConnectMessage;
import backend.network.messages.connection.ConnectionRefusedMessage;
import backend.network.messages.connection.DisconnectMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.SendPointsMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProtocol extends Thread{
  private String username;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private ClientMatch match;
  private boolean running = true;

  public ClientProtocol(String ip, int port, String username) {

    try {
      this.username = username;
      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());

      this.out.writeObject(new ConnectMessage(this.username));
      out.flush();
      out.reset();
      System.out.println("Local Port (Client): " + this.clientSocket.getLocalPort());
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.out.println("Could not establish connection to " + ip + ":" + port + ".\n"
          + "Please make sure the ip is correct and the server is online");
    }
  }

  public boolean isStable(){
    return (clientSocket != null) && (clientSocket.isConnected()) && !(clientSocket.isClosed());
  }

  /*process the incoming messages from the server*/
  public void run() {
    while (running) {
      try {
        Message message = (Message) in.readObject(); // the message input from the server

        switch (message.getMessageType()) {
          case CONNECTION_REFUSED:
            ConnectionRefusedMessage connectionRefusedMessage = (ConnectionRefusedMessage)message;
            disconnect();
            break;

          case SERVER_SHUTDOWN:
            disconnect();
            break;

          case SEND_ID:
            //TODO At game controller there must be a methode which receive ID's
            // for example for a tile
            break;

          case GAME_START:
            //TODO At game controller there must be a methode which show
            // the player the start of game
            break;

          case GAME_TURN:
            if (this.match.getMyNumber() == this.match.getCurrentPlayer() + 1) {
              this.match.nextPlayer();
            } else {

            }
            break;

          case GAME_WAIT:
            //TODO At game controller there must be a methode which show
            // that the player have to wait because of another players turn

            //redundant in my view, happens with GAME_TURN already
            break;

          case GAME_OVER:
            //TODO At game controller there must be a methode which show
            // the player that the game is over
            this.match.isOver();
            break;

          case GAME_WIN:
            //TODO At game controller there must be a methode which show
            // that the player won
            this.match.youWon();
            break;

          case GAME_LOOSE:
            //TODO At game controller there must be a methode which show
            // that the player lost
            this.match.youLost();
            break;

          case GAME_PLACEMENT:
            //TODO At game controller there must be a methode which show
            // the player the placement

            //redundant, by sending out the Player info, this info can be taken from Game Lobby
            break;

          case GAME_INFO:
            LobbyInformationMessage message1 = (LobbyInformationMessage) message;
            this.match = new ClientMatch(this, message1.getPlayers(), "server",
                new Player("ToDo", "TodO", 0,
                    Playerstatus.WAIT));
            break;
          case SEND_POINTS:
            //TODO At game controller there must be a methode which add
            // points to the player statistics
            SendPointsMessage message2 = (SendPointsMessage) message;
            this.match.addPointsToPlayer(message2.getPoints());
            break;

          case SEND_RACK_POINTS:
            //TODO At game controller there must be a methode which
            // calculate the points left on the rack

            //Why and also when?
            break;

          case PLACE_TILES:
            PlaceTilesMessage message3 = (PlaceTilesMessage) message;
            match.placeTilesOfOtherPlayers(message3.getTiles());
            break;

          case RECEIVE_SHUFFLE_TILES:
            match.receiveShuffleTiles((ReceiveShuffleTilesMessage) message);
            break;

          case TIME_ALERT:
            //TODO At game controller there must be a methode which
            // alert the player that the time is over soon
            break;

          case TIME_SYNC:
            //TODO At game controller there must be a methode which
            // synchronize the time left the player have, with the server
            break;

          default:
            break;
        }
      } catch (ClassNotFoundException | IOException e) {
        break;
      }
    }
  }

  /*Disconnect the client from the server*/
  public void disconnect(){
    running = false;
    try {
      if (!clientSocket.isClosed()){
        this.out.writeObject(new DisconnectMessage(this.username));
        clientSocket.close(); // close streams and socket
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  /*Send messages from client to server*/
  public void sendToServer(Message message) throws IOException {
    this.out.writeObject(message);
    out.flush();
    out.reset();
  }
}
