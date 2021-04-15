package frontend.screens.controllers;

import animatefx.animation.Pulse;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the starting menu. Nothing interesting here, just a screen leading to other more
 * important screens.
 *
 * @author mkolinsk
 */
public class StartingMenuController {

  // @author mkolinsk

  @FXML private Button newProfileButton;
  @FXML private Button existProfileButton;

  public void createNew(ActionEvent e) throws IOException {
    newProfile();
  }

  private void newProfile() throws IOException {

    Main m = new Main();
    m.changeScene("screens/createProfile.fxml");
  }

  public void logInto(ActionEvent e) throws IOException {
    logIn();
  }

  private void logIn() throws IOException {
    Main m = new Main();
    m.changeScene("screens/existingProfile.fxml");
  }

  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }
}
