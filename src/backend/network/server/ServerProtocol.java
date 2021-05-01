package backend.network.server;

import backend.network.messages.connection.ConnectionRefusedMessage;
import backend.network.messages.connection.GetIDMessage;
import backend.network.messages.Message;
import backend.network.messages.MessageType;
import backend.network.messages.connection.SendIDMessage;
import backend.network.tools.IDGeneratorBasic;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/*A server protocol which tells the server how to transfer messages to the client
* @author nilschae
* @version 1.0 */
public class ServerProtocol extends Thread{
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Server server;
  private String clientName;
  private boolean running = true;

  /*A Constructor which connects a client with the server
  * @param client the client that is being communicated with
  * @param server the server which manages the communication*/
  ServerProtocol(Socket client, Server server) {
    this.socket = client;
    this.server = server;
    try {
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
      sendInitialObjects(); // send all defined objects to new client
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void sendInitialObjects() throws IOException{

  }

  /* Send a message to the client of this server protocol
  * @param message the message which is sent*/
  public void sendToClient(Message message) throws IOException {
    this.out.writeObject(message);
    out.flush();
    out.reset();
  }

  /* close streams and socket */
  public void disconnect() {
    running = false;
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void run() {
    Message message;
    try {
      message = (Message) in.readObject();
      if (message.getMessageType() == MessageType.CONNECT){
        String from = message.getFrom();
        if (server.userExistsP(from)){
          Message connectionRefused = new ConnectionRefusedMessage("host",
              "Username already connected to the server!");
          out.writeObject(connectionRefused);
          out.flush();
          out.reset();
          disconnect();
        } else {
          this.clientName = from;
          server.addClient(from,this);
        }

      } else { // first message of client have to be connection message
        disconnect();
      }

      while (running) {
        message = (Message) in.readObject();
        int id = 0;

        switch (message.getMessageType()) {

          case GET_ID:
            id = IDGeneratorBasic.createID();
            SendIDMessage idMessage = new SendIDMessage("Host", id + "", ((GetIDMessage)message).getTmpId());
            sendToClient(idMessage);
            server.addIDToClient(id, clientName);
            break;

          case DISCONNECT:
            server.removeClient(message.getFrom());
            running = false;
            disconnect();
            break;

          default:
            break;
        }
      }
    } catch (IOException e) {
      running = false;
      if (socket.isClosed()){
        System.out.println("Socket closed.Name of disconnected Client: " +  this.clientName);
      } else {
        try {
          socket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    } catch (ClassNotFoundException e2){
      System.out.println(e2.getMessage());
      e2.printStackTrace();
    }
  }
}
