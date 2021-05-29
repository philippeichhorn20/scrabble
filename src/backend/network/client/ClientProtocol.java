package backend.network.client;

import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Player;
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
import backend.network.messages.time.TimeAlertMessage;
import frontend.Main;
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
  private String historyMess;
  private boolean messChange = false;



  private boolean running = true;
  private Message lastMessage = new Message(MessageType.GAME_LOOSE, "");

  public ClientProtocol(String ip, int port, String username, ClientMatch match) {
    try {
      this.username = username;
      this.clientSocket = new Socket(ip, port);
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in = new ObjectInputStream(clientSocket.getInputStream());

      this.out.writeObject(new ConnectMessage(this.username));
      out.flush();
      out.reset();
      System.out.println("Local Port (Client): " + this.clientSocket.getLocalPort());
      this.match = match;

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
              System.out.println("send id received");
              // TODO At game controller there must be a methode which receive ID's
              // for example for a tile
              break;

            case GAME_TURN:
              GameTurnMessage turnMessage = (GameTurnMessage) message;
              GameInformation.getInstance().getClientmatch().turnTaken(turnMessage.getNowTurn());
              break;

            case PLAY_FEEDBACK:
              System.out.println("play feedback message received");
              PlayFeedbackMessage message6 = (PlayFeedbackMessage) message;
              this.match.playFeedBackIntegration(message6);
              break;

            case GAME_OVER:
              // TODO At game controller there must be a methode which show
              // the player that the game is over
              this.match.getGameScreenController().showServerMessage("Game over",10);
              this.match.setOver(true);
              break;

            case GAME_WIN:
              // TODO At game controller there must be a methode which show
              // that the player won
              this.match.getGameScreenController().showServerMessage("Congrats, you won with "+ this.match.getPlayer().getScore()+" points!", 10);
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
              System.out.println("Game info message received");
              LobbyInformationMessage message1 = (LobbyInformationMessage) message;
              GameInformation.getInstance().getClientmatch().setPlayers(message1.getPlayers());
              GameInformation.getInstance().setPlayers(message1.getPlayers());
              for(Player p : GameInformation.getInstance().getClientmatch().getPlayers()) {
                System.out.print(p+" ");
              }
              break;
            case SEND_POINTS:
              // TODO At game controller there must be a methode which add
              // points to the player statistics
              SendPointsMessage message2 = (SendPointsMessage) message;
              this.match.newGameEvent(message2.getFrom()+" got "+ message2.getPoints()+ " points with his latest move");
              System.out.println("this guy got points: "+ message2.getPoints());
              this.match.addPointsToPlayer(message2.getPoints());
              break;

            case GAME_START:
              System.out.println("game start message received");
              // TODO At game controller there must be a methode which add
              // points to the player statistics
              GameStartMessage message3 = (GameStartMessage) message;
              this.match.getPlayer().updateRack(message3.getTiles());
              this.match.getTimer().start();
              Main m = new Main();
              m.changeScene("screens/gameScreen.fxml");
              break;

            case SEND_RACK_POINTS:
              // TODO At game controller there must be a methode which
              // calculate the points left on the rack

              // Why and also when?
              break;

            case PLACE_TILES:
              System.out.println("received tiles");
              PlaceTilesMessage message4 = (PlaceTilesMessage) message;
              GameInformation.getInstance().getClientmatch().placeTilesOfOtherPlayers(message4.getTiles());
              break;

            case RECEIVE_SHUFFLE_TILES:
              System.out.println("received tiles");
              ReceiveShuffleTilesMessage receiveShuffleTilesMessage = (ReceiveShuffleTilesMessage) message;
              if(receiveShuffleTilesMessage.getFrom().equals("")){
                System.out.println("not enough tiles to shuffle");
                match.newGameEvent("not enough tiles in bag to shuffle");
              }
              match.receiveShuffleTiles(receiveShuffleTilesMessage.getRack());
              break;

            case GET_NEW_TILES:
              System.out.println("Get new Tiles");
              GetNewTilesMessage getNewTilesMessage = (GetNewTilesMessage) message;
              System.out.println("new tiles first: "+ getNewTilesMessage.getTiles()[0].getLetter());
              match.receiveShuffleTiles(getNewTilesMessage.getTiles());
              break;

            case TIME_ALERT:
              TimeAlertMessage timeAlertMessage = (TimeAlertMessage) message;
              switch (timeAlertMessage.getAlertType()) {
                case TIME_OVER:
                  match.getGameScreenController().showServerMessage("Your time is up, moving on.", 5);
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
              }
              break;

            case TIME_SYNC:
              // it nulls the timer
              this.match.setTimerToZero();
              break;
              /**
               *@author vivanova
               */
            case TEXT: 
            	TextMessage textMessage = (TextMessage)message;
            	String sender = textMessage.getFrom();
            	String text = textMessage.getText();
            	GameInformation.getInstance().getChat().fillTextArea(sender, text);
            	/**
            	 * Call Text Box to add new Text
            	 */
            	
            	
            	break;
            case HISTORY:
              HistoryMessage hMessage = (HistoryMessage) message;
              historyMess = hMessage.getMessage();
              messChange = true;

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
  public String getHistoryMessage(){
    return historyMess;
  }
  public boolean messageChanged(){
    return messChange;
  }
  public void messageRead(){
    messChange = false;
  }
  public boolean isRunning() {
    return running;
  }
  /*Send messages from client to server*/
  public void sendToServer(Message message) throws IOException {
    this.out.writeObject(message);
    out.flush();
    out.reset();
  }

  public ClientMatch getMatch() {
    return this.match;
  }
}
