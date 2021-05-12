package frontend.screens.controllers;

import frontend.Main;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
  
  public static void main(String[] args){
	    MediaPlayer player = new MediaPlayer( new Media(uriString));
	    player.play();
	    Media media = null;
	    try {
	      media = new Media(getClass().getResource("/music/hero.mp3").toURI().toString());
	    } catch (URISyntaxException e) {
	      e.printStackTrace();
	    } 
	}
}
