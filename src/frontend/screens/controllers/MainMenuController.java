package frontend.screens.controllers;

import animatefx.animation.Pulse;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the main menu screen. Nothing too interesting here, just a screen that leads to
 * other screens.
 *
 * @author mkolinsk
 */
public class MainMenuController {

  @FXML
  private Button playButton;
  @FXML
  private Button manageButton;
  @FXML
  private Button goSettings;

  /**
   * animates the buttons.
   *
   * @param e when hovered with a mouse.
   */
  public void animate(MouseEvent e) {
    Button b = (Button) e.getSource();
    new Pulse(b).play();
  }

  /**
   * opens the lobby screen.
   *
   * @param e click on the button
   * @throws IOException when source for the screen is wrong.
   */

  public void goPlay(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/lobbyScreen.fxml");
  }

  /**
   * opens the statistics screen.
   *
   * @param e click on the button
   * @throws IOException when source for the screen is wrong.
   */

  public void goManage(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/statScreen.fxml");
  }

  /**
   * opens the settings screen.
   *
   * @param e click on the button
   * @throws IOException when source for the screen is wrong.
   * @author vivanova
   */

  public void openSettings(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/settingsScreen.fxml");
  }

  /**
   * opens the rule screen.
   *
   * @param e click on the button
   * @throws IOException when source for the screen is wrong.
   */

  public void goRule(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/ruleScreen.fxml");
  }
}
