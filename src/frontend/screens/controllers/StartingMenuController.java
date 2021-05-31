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

  @FXML
  private Button newProfileButton;
  @FXML
  private Button existProfileButton;


  /**
   * Opens the new profile screen.
   *
   * @param e Click on the button.
   * @throws IOException When the source for the screen is wrong.
   */

  public void createNew(ActionEvent e) throws IOException {

    Main m = new Main();
    m.changeScene("screens/createProfile.fxml");
  }

  /**
   * Opens the existing profile screen.
   *
   * @param e Click on the button.
   * @throws IOException When the source for the screen is wrong.
   */

  public void logInto(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/existingProfile.fxml");
  }

  /**
   * Animates the buttons.
   *
   * @param e when hovered over.
   */
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }
}
