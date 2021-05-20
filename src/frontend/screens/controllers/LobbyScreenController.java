package frontend.screens.controllers;

import animatefx.animation.Pulse;
import backend.basic.Lobby;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.network.client.ClientProtocol;
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

  // Method determines button clicked and changes to desired scene
  @FXML
  public void changeScene(ActionEvent e) throws IOException {
    Main m = new Main();
    Button trigger = (Button) e.getSource();
    String scene = "screens/";
    switch (trigger.getId()) {
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
        scene += "mainMenu.fxml";
        break;
    }
    m.changeScene(scene);
  }

  //Makes settings visible to hosting player.
  public void openStartGameView(ActionEvent e) {
    lobbyView.setVisible(false);
    joinGameView.setVisible(false);
    startGameView.setVisible(true);
    startLobby(e);
  }

  //Allows player to enter a code to join hosting player's server
  public void openJoinGameView(ActionEvent e) {
    lobbyView.setVisible(false);
    startGameView.setVisible(false);
    joinGameView.setVisible(true);
  }

  public void startLobby(ActionEvent e) {
    Main.lobby = new Lobby(
        new Player(Main.profile.getName(), Main.profile.getColor(), Playerstatus.WAIT));
    hostIP.setText(Main.lobby.getIp());
    ClientProtocol clientProtocol = new ClientProtocol(hostIP.getText(),ServerSettings.port,Main.profile.getName());
  }

  //Method switches to playboard and starts game.
  public void startGame(ActionEvent e) throws IOException {
    Main.lobby.newMatch();
    Main m = new Main();
    m.changeScene("screens/gameScreen.fxml");
  }

  //Method connects joining player to lobby or server of hosting player.
  public void enterLobby(ActionEvent e) {
    boolean validIP = true;
    if(validIP) {
      ClientProtocol cp = new ClientProtocol(adressIP.getText(), ServerSettings.port,Main.profile.getName());
    }
    openStartGameView(e);
    Main.lobby.newMatch();

  }

  // Method responsible for animations
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }

}

