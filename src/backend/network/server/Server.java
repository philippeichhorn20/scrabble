package backend.network.server;

import backend.basic.ClientMatch;
import backend.basic.ServerMatch;
import backend.network.messages.Message;
import backend.network.messages.connection.ShutDownMessage;
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
  private boolean nextMessageOnlyForHost = false;
  private boolean nextMessageAdditionalForHost = false;
  private final HashMap<String, ServerProtocol> clients = new HashMap<>(); // map with serverprotocols of clients
  private final HashMap<Integer, String> objecIDMap = new HashMap<>(); // map with owners of object ids
  ServerMatch serverMatch;
  ClientMatch match;


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
  public synchronized void addClient(String name, ServerProtocol protocol) {
    this.clients.put(name, protocol);
    System.out.println(name + " : new client has been added");
  }

  /*@return give back a set of clients which are connected to the server*/
  public synchronized Set<String> getClientNames() {
    Set<String> clientNames = this.clients.keySet();
    return new HashSet<String>(clientNames); // create a new list, because keySet modifications also modify map
  }

  /* add id of an object to the given client
  * @param id the id of the object
  * @param clientName the name of the client who own the object of the id*/
  public synchronized void  addIDToClient(Integer id, String clientName){
    objecIDMap.put(id, clientName);
  }

  public synchronized String getOwner(Integer id){
    return objecIDMap.get(id);
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

/*  public void updateHostGame(Message message) {
    switch(message.getMessageType()) {
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
        //this.match = new ClientMatch(this, message1.getPlayers(), "server",
        //    new Player("ToDo", "TodO", Playerstatus.WAIT));
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
        TimeAlertMessage timeAlertMessage = (TimeAlertMessage) message;
        switch (timeAlertMessage.getAlertType()) {
          case TIME_OVER:
            match.nextPlayer();
            break;
          case TIMER_STARTED:
            match.getPlayer().setTimerPersonalTimerToZero();
            break;
          case ONE_MINUTE_LEFT:
            match.oneMinuteAlert();
            break;
          case THIRTY_SECONDS_LEFT:
            match.thirtySecondsAlert();
            break;
        }
        break;

      case TIME_SYNC:
        //it nulls the timer
        this.match.getPlayer().setTimerToZero();
        break;

      default:
        break;
      }
    }

    public void setNextMessageOnlyForHost() {
      this.nextMessageOnlyForHost = true;
    }

    public void setNextMessageAdditionalForHost() {
      this.nextMessageAdditionalForHost = true;
    }*/
}
