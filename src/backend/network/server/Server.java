package backend.network.server;

import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.basic.ServerMatch;
import backend.network.messages.Message;
import backend.network.messages.connection.ShutDownMessage;
import frontend.Main;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * @author nilschae
 * @version 1.0
 * @description A Server which manage the transfer of data between users
 *
 * */
public class Server {

  private ServerSocket serverSocket;

  public boolean isRunning() {
    return running;
  }

  private boolean running;
  private boolean newPlayerAdded = false;
  private final HashMap<String, ServerProtocol> clients = new HashMap<>(); // map with serverprotocols of clients
  ServerMatch serverMatch;


  /*
   * remove clients together with the matching protocol
   * @param clientName the name of the client who gets removed*/
  public synchronized void removeClient(String clientName) {
    this.clients.remove(clientName);
  }

  /* look up if the user already exist in the clients hashmap
   * @param name the name of the user which get looked up
  * @return if the user exist true if not false  */
  public synchronized boolean userExistsP(String name){
    return this.clients.containsKey(name);
  }

  /*Add a client to the clients hashmap with a given serverprotocol
  * @param name the name of the client to add
  * @param protocol the server protocol which gets added with the name */
  public synchronized void addClient(Player player, ServerProtocol protocol) {
    this.clients.put(player.getName(), protocol);
    //@TODO linecomment
    if(player.getStatus()!=Playerstatus.AI) {
      GameInformation.getInstance().addPlayer(player);
      newPlayerAdded = true;
      System.out.println(player.getName() + " : new client has been added");
    }
  }

  public void playerAdded(){
    newPlayerAdded=false;
  }
  public boolean newPlayer(){
    return newPlayerAdded;
  }

  /*@return give back a set of clients which are connected to the server*/
  public synchronized Set<String> getClientNames() {
    Set<String> clientNames = this.clients.keySet();
    return new HashSet<String>(clientNames); // create a new list, because keySet modifications also modify map
  }

  // setup server +  listen to connection requests from clients
  public void listen() {
    running = true;
    try {
      serverSocket = new ServerSocket(ServerSettings.port);
      System.out.println("Server runs");
      /* open streams */
      while (running) {
        Socket clientSocket = serverSocket.accept();

        /* starting new client thread */
        ServerProtocol clientConnectionThread = new ServerProtocol(clientSocket, this);
        clientConnectionThread.start();
      }


    } catch (BindException e){
      e.printStackTrace();
    }catch (IOException e) {
      if (serverSocket != null && serverSocket.isClosed()){
        System.out.println("Server stopped.");
      } else {
        e.printStackTrace();
      }
    }
  }

  /*Send a message to a list of clients
  * @param clientNames names of the clients which receive the message
  * @param message the message which have to be sent*/
  private synchronized void sendTo(List<String> clientNames, Message message){
    List<String> cFails = new ArrayList<String>();
    for (String cName : clientNames) {
      try {
        System.out.println("message sending to " + cName);
        System.out.println("exists: " + clients.containsKey(cName));
        if(!clients.containsKey(cName)){
          System.out.println(cName+" does not exist in client array");
        }
        ServerProtocol c = clients.get(cName);
        c.sendToClient(message);

      } catch (IOException e) {
        e.printStackTrace();
        cFails.add(cName); // notice to remove
        continue;
      }
    }

    for (String c : cFails) {
      System.out.println("Client " + c + " removed (because of send failure).");
      removeClient(c);
    }
  }


  /* send a message to all clients which are connected to the server
   * @param message the message which gets send to all clients*/
  public void sendToAll(Message message) {
    sendTo(new ArrayList<String>(getClientNames()), message);
  }

  /* send a message to one clients which is connected to the server
   * @param message the message which gets send to all clients
   * @param recipient the client who the message is send to
   */
  public void sendOnlyTo(String recepient, Message message) {
    ArrayList<String> list = new ArrayList<>();
    list.add(recepient);
    sendTo(list, message);
  }

  /* sends a message to all clients except one client
   * @param name the name of the client who don't get the message
   * @param message the message which gets send*/
  public void sendToAllBut(String name, Message message) {
    synchronized (this.clients) {
      Set<String> senderList = getClientNames();
      senderList.remove(name);
      sendTo(new ArrayList<String>(senderList), message);
    }

  }

  /* stops the server and send a ShutDownMessage to every client*/
  public void stopServer() {
    running = false;
    sendToAll(new ShutDownMessage("Server"));

    if (!serverSocket.isClosed()) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(0);
      }
    }

  }


  public ServerMatch getServerMatch() {
    return serverMatch;
  }

  public void setServerMatch(ServerMatch serverMatch) {
    this.serverMatch = serverMatch;
  }

  public ServerSocket getServerSocket() {
    return serverSocket;
  }

  public void setServerSocket(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }
}
