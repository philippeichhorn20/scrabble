package frontend.screens.controllers;

import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/*
@author jawinter
    TODO:
    This is an empty controller for the lobby screen. Here the user should be able to choose between hosting and joining a game, or starting a tutorial.
 */
public class LobbyScreenController {

  @FXML
  private Button playTutorial;
  @FXML
  private Button playNetworkGame;
  @FXML
  private Button playLocalGame;

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
    }
    m.changeScene(scene);
  }

}
