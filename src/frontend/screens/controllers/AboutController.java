package frontend.screens.controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

import frontend.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AboutController {

		
		@FXML private Button backButton;


		@FXML
		public void goBack(ActionEvent e) throws IOException {
			Main m = new Main();
			m.changeScene("screens/settingsScreen.fxml");
		}	

		@FXML TextFlow rulesTextFlow;  
		
		// gets the text from here and adds it to the RuleScreen
	 public void initialize(URL url, ResourceBundle rb) {
	    Text text = new Text();
	    rulesTextFlow.getChildren().add(text);
	    

		}
	
}


