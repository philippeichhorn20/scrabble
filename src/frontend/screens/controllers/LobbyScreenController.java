package frontend.screens.controllers;

import animatefx.animation.Pulse;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/*
@author jawinter
    TODO:
    This is an empty controller for the lobby screen. Here the user should be able to choose between hosting and joining a game, or starting a tutorial.
 */
public class LobbyScreenController {


  @FXML
  private VBox joinGameView;
  @FXML
  private ImageView lobbyView;
  @FXML
  private VBox startGameView;

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
  public void openStartGameView(ActionEvent e)  {
    lobbyView.setVisible(false);
    joinGameView.setVisible(false);
    startGameView.setVisible(true);
  }

  //Allows player to enter a code to join hosting player's server
  public void openJoinGameView(ActionEvent e) {
    lobbyView.setVisible(false);
    startGameView.setVisible(false);
    joinGameView.setVisible(true);
  }
  // Method responsible for animations
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }

}
