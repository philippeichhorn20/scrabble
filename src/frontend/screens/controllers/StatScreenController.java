package frontend.screens.controllers;

import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Controller for the statistics screen. User career statistics are shown here
 *
 * @author mkolinsk
 * @version 1.0
 */
public class StatScreenController {

  @FXML private Text userTitle;
  @FXML private Text games;
  @FXML private Text wins;
  @FXML private Text points;
  @FXML private Text avgPoints;
  @FXML private Button backButton;

  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }

  /**
   * Sets up the statistics on the screen.
   *
   * @param e MouseEvent
   * @throws IOException Exception
   */
  public void setStage(MouseEvent e) throws IOException {
    String name = Main.profile.getName();
    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    userTitle.setText(name);
    games.setText(Integer.toString(Main.profile.getGames()));
    wins.setText(Integer.toString(Main.profile.getWins()));
    points.setText(String.valueOf(Main.profile.getPoints()));
    if (Main.profile.getGames() == 0) {
      avgPoints.setText("0");
    } else {
      avgPoints.setText(String.valueOf(Main.profile.getPoints() / Main.profile.getGames()));
    }
  }
}
