package backend.network.client;

import backend.network.messages.connection.ConnectMessage;
import backend.network.messages.connection.ConnectionRefusedMessage;
import backend.network.messages.connection.DisconnectMessage;
import backend.network.messages.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProtocol extends Thread{
  private String username;
  private Socket clientSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
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
            //TODO At game controller there must be a methode which show
            // that the player have the turn
            break;

          case GAME_WAIT:
            //TODO At game controller there must be a methode which show
            // that the player have to wait because of another players turn
            break;

          case GAME_OVER:
            //TODO At game controller there must be a methode which show
            // the player that the game is over
            break;

          case GAME_WIN:
            //TODO At game controller there must be a methode which show
            // that the player won
            break;

          case GAME_LOOSE:
            //TODO At game controller there must be a methode which show
            // that the player lost
            break;

          case GAME_PLACEMENT:
            //TODO At game controller there must be a methode which show
            // the player the placement
            break;

          case SEND_POINTS:
            //TODO At game controller there must be a methode which add
            // points to the player statistics
            break;

          case SEND_RACK_POINTS:
            //TODO At game controller there must be a methode which
            // calculate the points left on the rack
            break;

          case PLACE_TILES:
            //TODO At game controller there must be a methode which
            // place tiles on the board of the player
            break;

          case RECEIVE_SHUFFLE_TILES:
            //TODO At game controller there must be a methode which
            // add the shuffled tiles to the player rack
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