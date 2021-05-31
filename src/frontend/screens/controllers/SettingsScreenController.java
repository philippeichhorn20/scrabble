package frontend.screens.controllers;

import backend.basic.GameInformation;
import frontend.screens.controllers.AboutController;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.media.MediaPlayer;

/**
 * controller for the setting screen.
 *
 * @auhtor vivanova
 */
public class SettingsScreenController {
  @FXML private Button backButton;
  @FXML private static Slider volumeKey;
  @FXML private RadioButton radioButton;
  @FXML private TextArea textArea;
  @FXML private Button aboutButton;

  public static MediaPlayer player;

  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }

  public void goAbout(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/about.fxml");
  }

  /**
   * A radio button which activate/deactivate the music.
   *
   * @param e an event.
   * @throws IOException normally don't happen.
   */
  public void radioButton(ActionEvent e) throws IOException {

    player = GameInformation.getInstance().getMusic().getPlayer();
    boolean isSelected = radioButton.isSelected();

    if (isSelected == true) {
      player.play();
    } else {
      player.pause();
    }
  }

  /** initilize setting screen controller. */
  @FXML
  public void initialize() {

    player = GameInformation.getInstance().getMusic().getPlayer();

  }
}
