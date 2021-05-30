package backend.network.server;

import backend.basic.GameInformation;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.network.messages.Message;
import backend.network.messages.MessageType;
import backend.network.messages.connection.ConnectionRefusedMessage;
import backend.network.messages.connection.GetIDMessage;
import backend.network.messages.connection.SendIDMessage;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.text.TextMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.SendStartRackMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import backend.network.tools.IDGeneratorBasic;
import frontend.Main;
import frontend.screens.controllers.GameScreenController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/*A server protocol which tells the server how to transfer messages to the client
 * @author nilschae
 * @version 1.0 */
public class ServerProtocol extends Thread {

  private final Socket socket;
  private final Server server;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private String clientName;
  private boolean running = true;
  private Player[] players;
  private Message lastMessage = new Message(MessageType.GAME_WIN, "");


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

  private void sendInitialObjects() throws IOException {

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
  /*
  Message message;
    try {
      message = (Message) in.readObject();
      if (message.getMessageType() == MessageType.CONNECT) {
        System.out.println("new User attempts to connect...");
        String from = message.getFrom();
        if (server.userExistsP(from)) {
          System.out.println("unsuccessful since already in lobby...");
          Message connectionRefused = new ConnectionRefusedMessage("host",
              "Username already connected to the server!");
          out.writeObject(connectionRefused);
          out.flush();
          out.reset();
          disconnect();
        } else {
          System.out.println("successful");
          this.clientName = from;
          Player player = ((ConnectMessage) message).getPlayer();
          server.addClient(player, this);
          if (GameInformation.getInstance().getServermatch().addPlayer(player)) {
            System.out.print("player in game lol: ");
            for (Player p : GameInformation.getInstance().getServermatch().getPlayers()) {
              if (p != null) {
                System.out.print(p.getName() + " ");
              }
            }
          } else {
            Message connectionRefused = new ConnectionRefusedMessage("server",
                "Lobby is full!");
          }
        }

      } else if (message.getMessageType() == MessageType.CONNECT_AI) {
        System.out.println("new AI attempts to connect...");
        String from = message.getFrom();
        if (server.userExistsP(from)) {
          System.out.println("unsuccessful since already in lobby...");
          Message connectionRefused = new ConnectionRefusedMessage("host",
              "Username already connected to the server!");
          out.writeObject(connectionRefused);
          out.flush();
          out.reset();
          disconnect();
        } else {
          System.out.println("successful");
          this.clientName = from;
          PlayerAI aiPlayer = (PlayerAI) ((ConnectMessage)message).getPlayer();
          server.addClient(aiPlayer, this);
        }
   */// i copied this from the master branch will probably need to paste this instead of the code below
  public void run() {
    Message message;
    try {
      message = (Message) in.readObject();
      if (message.getMessageType() == MessageType.CONNECT) {
        System.out.println("new User attempts to connect...");
        String from = message.getFrom();
        if (server.userExistsP(from)) {
          System.out.println("unsuccessful since already in lobby...");
          Message connectionRefused = new ConnectionRefusedMessage("host",
              "Username already connected to the server!");
          out.writeObject(connectionRefused);
          out.flush();
          out.reset();
          disconnect();
        } else {
          System.out.println("successful");
          this.clientName = from;
          server.addClient(from, this);
          Player clientPlayer = new Player(from, Main.profile.getColor(),Main.profile.getGames(), Main.profile
              .getWins(), Playerstatus.WAIT);
          if (GameInformation.getInstance().getServermatch().addPlayer(clientPlayer)) {
            System.out.print("player in game lol: ");
            for(Player p: GameInformation.getInstance().getServermatch().getPlayers()){
              if(p!= null){
                System.out.print(p.getName()+" ");
              }
            }
          } else {
            Message connectionRefused = new ConnectionRefusedMessage("server",
                "Lobby is full!");
          }
        }

      } else { // first message of client have to be connection message
        System.out.println("server disconnected");
        disconnect();
      }
      System.out.println("server connection established successfully, now running");
      while (running) {
        message = (Message) in.readObject();
        int id = 0;
        if (message != null && lastMessage != null && !lastMessage.equals(message)) {
          switch (message.getMessageType()) {
            case GET_ID:
              System.out.println("ID's are gone. Ask Nils for questions");
              break;

            case DISCONNECT:
              System.out.println("removing player");
              server.removeClient(message.getFrom());
              running = false;
              GameInformation.getInstance().getServermatch().removePlayer(message.getFrom());
              disconnect();
              break;

            case PLACE_TILES:
              System.out.println("receiving tiles");
              PlaceTilesMessage placeTilesMessage = (PlaceTilesMessage) message;
              GameInformation.getInstance().getServermatch().placeTiles(
                  placeTilesMessage.getTiles(), placeTilesMessage.getFrom());
              break;

            case SHUFFLE_TILES:
              // TODO At game controller there must be a methode which shuffle tiles
              // and return the shuffled tiles
              ShuffleTilesMessage shuffleTilesMessage = (ShuffleTilesMessage) message;
              this.server.serverMatch.shuffleTilesOfPlayer(message.getFrom(), shuffleTilesMessage.getToShuffleTiles());
              break;

            case GAME_TURN:
              if (server.serverMatch.getPlayerName().equals(message.getFrom())) {
                server.serverMatch.nextPlayer();
              }
              break;

            case GAME_OVER:
              server.serverMatch.gameOver();
              break;

            case SEND_POINTS:
              // TODO At game controller there must be a methode which add the points received from
              // the racks
              // at the end of a game to the statistics

              // does it automatically after game move
              break;

            // Pass leads to the server telling next player it's his turn.
            case PASS:
              server.serverMatch.incrementPointlessTurns();
              server.serverMatch.nextPlayer();

              break;

            /** @author vivanova */
            // Server receives a Relay message from Client
            case RELAY:
              // Server sends the message to everyone
              TextMessage textMessage = (TextMessage) message;
              if (textMessage.getText() != null) {
                System.out.println("Sending Text Message");
            	  server.sendToAll(
                    // with a new Flag that means it has to be rendered in the chat area
                    new TextMessage(MessageType.TEXT,textMessage.getFrom(), textMessage.getText()));
              }
              break;
            case HISTORY:
              HistoryMessage historyMessage = (HistoryMessage) message;
              server.sendToAllBut(historyMessage.getFrom(),historyMessage);
              break;
            case SEND_START_RACK:
              SendStartRackMessage ssrMessage = (SendStartRackMessage) message;
              for (String s:server.getClientNames()){
                server.sendOnlyTo(s,new SendStartRackMessage(ssrMessage.getFrom(),server.serverMatch.drawNewTiles(7)));
              }
              break;

            default:
              break;
          }
          lastMessage = message;
        }
      }
    } catch (IOException e) {
      running = false;
      if (socket.isClosed()) {
        System.out.println("Socket closed. Name of disconnected Client: " + this.clientName);
        GameScreenController.AlertBox.display("Could not connect", "please chose a different name and reconnect");
      } else {
        try {
          socket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    } catch (ClassNotFoundException e2) {
      System.out.println(e2.getMessage());
      e2.printStackTrace();
    }
  }


  public Server getServer() {
    return server;
  }
}
