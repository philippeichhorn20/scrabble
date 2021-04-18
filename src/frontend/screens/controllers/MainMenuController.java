package frontend.screens.controllers;

import animatefx.animation.Pulse;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Controller for the main menu screen. Nothing too interesting here, just a screen that leads to
 * other screens.
 *
 * @author mkolinsk
 */
public class MainMenuController {

  @FXML private Button playButton;
  @FXML private Button manageButton;
  @FXML private Button settingsButton;
  @FXML private Text userName;
  private boolean setUpDone = false;

  public void animate(MouseEvent e) {
    Button b = (Button) e.getSource();
    new Pulse(b).play();
  }

  public void animateText(MouseEvent e){
    new Pulse(userName).play();
  }

  public void goPlay(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/lobbyScreen.fxml");
  }

  public void goManage(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/manageProfile.fxml");
  }

  public void goSettings(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/settingsScreen.fxml");
  }
  public void goStats(MouseEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/statScreen.fxml");
  }
  public void goRule(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/ruleScreen.fxml");
  }

  /**
   * Sets up the user profile name in the upper left corner.
   *
   * @param e MouseEvent
   */
  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      String name = Main.profile.getName();
      name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
      userName.setText(name);
      setUpDone = true;
    }
  }
}
