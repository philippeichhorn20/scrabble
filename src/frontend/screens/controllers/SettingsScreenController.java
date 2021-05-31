package frontend.screens.controllers;

import frontend.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import backend.basic.GameInformation;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;

/**
 * @author jawinter This is an empty controller for the settings screen. Here the user should be
 * able to change multiple settings concerning the game. (Ideas: sound, different backgrounds,
 * dictionary used)
 */
public class SettingsScreenController {

  @FXML
  private RadioButton radioButton;
  @FXML
  private TextArea textArea;
  @FXML
  private Button aboutButton;

  public static MediaPlayer player;

  /**
   * Handles click on back button
   *
   * @param e
   * @throws IOException
   */
  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }

  /**
   * Change scene to about
   * @param e click
   * @throws IOException
   */
  public void goAbout(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/about.fxml");
  }

  /**
   * Handles click on radio Button
   *
   * @param e click
   * @throws IOException
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

  /**
   * Sets up settings look
   */
  public void initialize() {

    player = GameInformation.getInstance().getMusic().getPlayer();
//		player.setVolume(0.3);
//		volumeKey.setValue(player.getVolume() * 100);
//		volumeKey.valueProperty().addListener(new InvalidationListener() {
//
//			@Override
//			public void invalidated(Observable observable) {
//
//				player.setVolume(volumeKey.getValue() / 100);
//			}
//
//		});
  }


}
