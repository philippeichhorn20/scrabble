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

/**
 * A controller which shows the rule screen.
 *
 * @author vivanova
 */
public class RuleScreenController {

	@FXML
	private Button backButton;
	@FXML
	private TextArea textArea;
	@FXML
	BorderPane borderPane1;
	@FXML
	BorderPane borderPane2;

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

	/** set the image. */
	public void image() {

		textArea.setWrapText(true);

		textArea.setText("\"\r\n" + "Items: Scrabble Board, Tiles, Racks, Bag of Tiles \r\n" + "\r\n"

				+ "1. BEFORE THE START - Before the game starts, the players agree on a resource (dictionary or other word lists) which is used to decide if a word is valid or not. "
				+ "The next step is for the users to decide in which order they want to play. Either a random approach is used or the players vote on an order. When an order is selected each player draws seven tiles and places them on their rack. \r\n"
				+ "\r\n"
				+ "2. MAKE A PLAY - A player makes a play by putting a valid word on the board. The first word has to be played through the middle of the board (star) and it must be at least two letters long. "
				+ "After that a valid move is made by using one or more tiles to place a word on the board. This new word may use an already existing word or must join with the cluster of tiles already on the board. Each turn a player has 3 options: \r\n"
				+ "● pass \r\n"
				+ "● exchange one or more tiles for an equal number from the bag, scoring nothing. (at least 7 tiles must remain in the bag after the exchange)\r\n"
				+ "● play at least on tile on the board, adding the value of all words formed to the player’s cumulative score. \r\n"
				+ "A word can only be played as a continuous string of letters reading from left to right or top to bottom. The main word must either use the letters of one or more previously played words or else have at least one of its tiles horizontally or vertically "
				+ "adjacent to an already played word. If any words other than the main word are formed by the play, they are scored as well and are subject to the same criteria of acceptability. If a blank tile (joker) is played the player has to define for what letter the blank tile stands for and it remains to stand for this letter throughout the game. The blank tile scores zero points. After making a play, the player announces the score for that play and draws tiles from the bag to replenish their rack to seven tiles. If there are not enough tiles in the bag to do so, the player takes all the remaining tiles. \r\n"
				+ "The game is ended by any of these causes: One player plays every tile on their rack, and there are no tiles remaining in the bag (regardless of the tiles on the opponent's rack). At least six successive scoreless turns have occurred"
				+ " and either player decides toend the game. A player uses more than 10 minutes of overtime.\r\n"
				+ "\r\n" + "\r\n" + "" + "" + "3. SCORING                      \r\n" + "\r\n"
				+ "Double Letter(DLS) are indicated with a light blue Tile. Triple Letter(TLS) with a dark blue Tile. Double Word (DWS) with a pink Tile and Triple Word (TWS) with red Tiles.\r\n"
				+ "The score for any play is determined this way:Each new word formed in a play is scored separately, and then those scores areadded up. The value of each tile is indicated on the tile, and blank tiles are worthzero points."
				+ "The main word (defined as the word containing every played letter) is scored. Theletter values of the tiles are added up, and tiles placed on Double Letter Score(DLS) and Triple Letter Score (TLS) squares are doubled or tripled in value,respectively. "
				+ ""
				+ "Tiles placed on Double Word Score (DWS) or Triple Word Score(TWS) squares double or triple the value of the word(s) that include those tiles,respectively. In particular, the center square (H8) is considered a DWS, and the firstplay is doubled in value.Premium squares apply only when newly placed tiles cover them. Any subsequentplays do not count those premium squares.If a player covers both letter and word premium squares with a single word, theletter premium(s) is/are calculated first, followed by the word premium(s).If seven tiles have been laid on the board in one turn after all of the words formedhave been scored, 50 bonus points are added. (Bingo or Bonus)");
		borderPane1.setStyle("-fx-background-image: @resources/wooden-bg.jpg;");
		borderPane2.setStyle("-fx-background-image: @resources/wooden-bg.jpg;");
	}
}
