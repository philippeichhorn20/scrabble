package frontend.screens.controllers;

import frontend.Main;
import frontend.screens.controllers.SettingsScreenController;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javax.print.DocFlavor.URL;

/**
 * Controller for about screen. Info about the team and the game
 *
 * @author vivanova
 */
public class AboutController {

	@FXML
	private Button backButton;

	@FXML
	public void goBack(ActionEvent e) throws IOException {
		Main m = new Main();
		m.changeScene("screens/settingsScreen.fxml");
	}

	@FXML
	TextFlow rulesTextFlow;

	// gets the text from here and adds it to the RuleScreen
	public void setText(URL url, ResourceBundle rb) {
		Text text = new Text();
		rulesTextFlow.getChildren().add(text);
		text.setText("\"\r\n" + "Scrabble Version 1.0\r\n" + "\r\n" + "This is a message from our team to you. \r\n"
				+ "\r\n" + "We are happy to represent our first self-made Scrabble Game. \r\n" + "\r\n"
				+ "We tried making it as user-friendly as possible, added music and animations, implemented Easy and Hard AI, \r\n"
				+ "so you can play\r\n" + "even when your friends are away.\r\n" + "\r\n"
				+ "We focused not only on the \"Normal\" User but also on the Java-Geeks that are eager to get into programming. "
				+ "That's why our code is open-source and can be found on GitLab. WIth the code being open-source you can change it and tweak it to your likings. "
				+ "We have also commented on our every step of the code so it is easier to understand and get into. \r\n"
				+ "\r\n" + "Advantages for you if you use our Scrabble application:\r\n"
				+ "    1) We don't sell your data to 3rd parties like Facebook or Google\r\n"
				+ "    2) We don't even collect your data\r\n"
				+ "    3) You can expand your English vocabulary and Java skills\r\n"
				+ "    4) Have some quality time\r\n" + "\r\n" + "Hope you enjoy!\r\n" + "\r\n"
				+ "Disclaimer: Music is not copy-righted. It can be found on \"https://freemusicarchive.org/\".\r\n"
				+ "\r\n"
				+ "                                                                                                                 Sincerely, your 14 Team\r\n"
				+ "                                                                                                                 Prakltikum Software Engineering\r\n"
				+ "\r\n"
				+ "                                                                                                                 Authors: Michal, Nils, Philipp, Jann, Viktoria");

	}
}
