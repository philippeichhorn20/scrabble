package frontend.screens.controllers;


import frontend.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/*
 * @author vivanova
 * @version 1.0
 */
/*
   
    This is a controller for the rules screen. Here the user is able to get to know the rules of the game.
    
    There is a back button included, that leads  back to  the game. 
 */

public class RuleScreenController {
	
	@FXML private Button backButton;


	@FXML
	public void goBack(ActionEvent e) throws IOException {
		Main m = new Main();
		m.changeScene("screens/mainMenu.fxml");
	}	

	@FXML TextFlow rulesTextFlow;  
	
	// gets the text from here and adds it to the RuleScreen
 public void initialize(URL url, ResourceBundle rb) {
    Text text = new Text();
    rulesTextFlow.getChildren().add(text);
    

	}
}
