package frontend.screens.controllers;

import frontend.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/*
 * @author vivanova
 * @version 1.0
 */

/*
   This is an empty controller for the settings screen. Here the user should be able to change multiple settings concerning the game.
   (Ideas: sound, different backgrounds, dictionary used)
   
   Till now music and volume key implemented. !!!DICTIONARIES TO BE ADDED!!
*/
public class SettingsScreenController {
	@FXML
	private Button backButton;
	@FXML
	private static Slider volumeKey;
	@FXML
	private RadioButton radioButton;

	public void goBack(ActionEvent e) throws IOException {
		Main m = new Main();
		m.changeScene("screens/mainMenu.fxml");
	}

	// The user should be able to change the volume from here
	public static void music(String[] args) throws URISyntaxException {
		
		String path = "src\\lib\\music\\Somewhere_Off_Jazz_Street.mp3";
		Media media = new Media(path);
		MediaPlayer player = new MediaPlayer(media);

		media = new Media(new File(path).toURI().toString());
		player.setVolume(40);
		player.play();
		player.setAutoPlay(true);

		volumeKey.setValue(player.getVolume() * 100);
		volumeKey.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {

				player.setVolume(volumeKey.getValue() / 100);
			}
		});

		RadioButton radioButton = new RadioButton("Music On/Off");

		radioButton.setOnAction(event -> {
			if (radioButton.isSelected()) {
				player.pause();
			} else {
				player.play();
			}
		});

		// Dictionaries to be changed/added
	}
	/*public static void main(String[] args) {
		String basePath = new File("").getAbsolutePath();
		System.out.println(basePath);
					
	}*/
}
