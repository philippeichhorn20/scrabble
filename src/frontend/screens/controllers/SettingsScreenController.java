package frontend.screens.controllers;

import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/*
   TODO:
   This is an empty controller for the settings screen. Here the user should be able to change multiple settings concerning the game.
   (Ideas: sound, different backgrounds, dictionary used)
*/
public class SettingsScreenController {
  @FXML private Button backButton;

  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }
}
