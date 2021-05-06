package frontend.screens.controllers;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.Text;

import frontend.Main;
import javafx.fxml.FXML;
import javafx.scene.text.TextFlow;

/*
    TODO:
    This is a controller for the rules screen. Here the user is able to get to know the rules of the game.
    
    There is a back button included, that leads to back the game. 
 */
public class RuleScreenController {

	@FXML private Button backButton;

	public void goBack(ActionEvent e) throws IOException {
		Main m = new Main();
		m.changeScene("screens/gameScreen.fxml");
	}	
 

	
}
