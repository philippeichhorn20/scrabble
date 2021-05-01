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
