package frontend.screens.controllers;

import frontend.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
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

	@FXML
	private Button backButton;
	@FXML
	private TextArea textArea;
	@FXML BorderPane borderPane1;
	@FXML BorderPane borderPane2;

	@FXML
	public void goBack(ActionEvent e) throws IOException {
		Main m = new Main();
		m.changeScene("screens/mainMenu.fxml");
	}

	@FXML
	TextFlow rulesTextFlow;

	// gets the text from here and adds it to the RuleScreen
	public void initialize(URL url, ResourceBundle rb) {
		Text text = new Text();
		rulesTextFlow.getChildren().add(text);

	}

	public void image() {
		textArea.setText("\"Items: Scrabble Board, Tiles, Racks, Bag of Tiles 1. &#10;Before the start - Before the game starts, the players agree on a resource (dictionary or other word lists) which is used to decide if a word is valid or not. The next step is for the users to decide in which order they want to play. Either a random approach is used or the players vote on an order. When an order is selected each player draws seven tiles and places them on their rack. &#10;&#10;2. Make a play - A player makes a play by putting a valid word on the board. The first word");
		borderPane1.setStyle("-fx-background-image: @resources/wooden-bg.jpg;");
		borderPane2.setStyle("-fx-background-image: @resources/wooden-bg.jpg;");
	}
}
