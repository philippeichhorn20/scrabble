package frontend.screens.controllers;

import animatefx.animation.Pulse;
import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Lobby;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.network.client.ClientProtocol;
import backend.network.server.Server;
import backend.network.server.ServerSettings;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/*
@author jawinter
    This is the controller for the lobby screen. Here the user should be able to choose between hosting and joining a game, or starting a tutorial.
 */
public class LobbyScreenController {


  @FXML
  private VBox joinGameView;
  @FXML
  private ImageView lobbyView;
  @FXML
  private VBox startGameView;
  @FXML
  private TextField hostIP;
  @FXML
  private TextField adressIP;
  @FXML
  private Button startGameButton;

  boolean isHost = true;

  // Method determines button clicked and changes to desired scene
  @FXML
  public void changeScene(ActionEvent e) throws IOException {
    Main m = new Main();
    Button trigger = (Button) e.getSource();
    String scene = "screens/";
    switch (trigger.getId()) {
      case "startGameButton":
        startLobby(e);
        break;
      case "playTutorial":
        scene += "gameScreen.fxml";
        break;
      case "playNetworkGame":
        scene += "gameScreen.fxml";
        break;
      case "playLocalGame":
        scene += "gameScreen.fxml";
        break;
      case "backButton":
        isHost = true;
        scene += "mainMenu.fxml";
        break;

    }
    m.changeScene(scene);
  }

  //Makes settings visible to hosting player.
  public void openStartGameView(ActionEvent e) {
    System.out.println("openStartGameView called");
    lobbyView.setVisible(false);
    joinGameView.setVisible(false);
    startGameView.setVisible(true);
    if(isHost){
      startLobby(e);
    }

  }



  //Allows player to enter a code to join hosting player's server
  public void openJoinGameView(ActionEvent e) {
      System.out.println("openJoinGameView called");
    lobbyView.setVisible(false);
    startGameView.setVisible(false);
    joinGameView.setVisible(true);
  }

  public void startLobby(ActionEvent e) {
      System.out.println("start Lobby called");
    isHost = true;
    Player host = new Player(Main.profile.getName(),Main.profile.getColor(),Playerstatus.WAIT);
    Main.lobby = new Lobby(
        host);

    Server server = new Server();
    Runnable r = new Runnable(){
      public void run(){
        server.listen();
      }
    };
    new Thread(r).start();

    Main.lobby.setServer(server);
    hostIP.setText(Main.lobby.getIp());

    ClientProtocol clientProtocol = new ClientProtocol(hostIP.getText(),ServerSettings.port,Main.profile.getName(), new ClientMatch(Main.profile.getName(), new Player(Main.profile.getName(), "", Playerstatus.WAIT)));
    clientProtocol.start();

    clientProtocol.getMatch().addProtocol(clientProtocol);
    GameInformation gameInformation = GameInformation.getInstance();
    gameInformation.setProfile(Main.profile);
    gameInformation.setHost(host);
    ClientMatch cm = new ClientMatch(Main.profile.getName(),host);
    gameInformation.setClientmatch(cm);


  }

  //Method switches to playboard and starts game.
  public void startGame(ActionEvent e) {
    System.out.println("Start match triggered");
    Main.lobby.newMatch();
  }

  //Method connects joining player to lobby or server of hosting player.
  public void enterLobby(ActionEvent e) {
    System.out.println("enter Lobby called");
    boolean validIP = true;

    if(validIP) {
      ClientProtocol cp = new ClientProtocol(adressIP.getText(), ServerSettings.port,Main.profile.getName(), new ClientMatch(Main.profile.getName(), new Player(Main.profile.getName(),"", Playerstatus.WAIT)));
      cp.start();
      cp.getMatch().addProtocol(cp);
    }
    isHost = false;
    openStartGameView(e);
    startGameButton.setVisible(false);

  }


  // Method responsible for animations
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }

}



