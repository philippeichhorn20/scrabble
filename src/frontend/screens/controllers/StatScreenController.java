package frontend.screens.controllers;

import backend.basic.Profile;
import frontend.Main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/*
 * Controller for the statistics screen. User career statistics are shown here
 *
 * @author jawinter
 * @version 1.1
 */
public class StatScreenController {

  String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";

  @FXML
  private Text userTitle;
  @FXML
  private TextField profileNickname;
  @FXML
  private TextField profileColor;
  @FXML
  private Text games;
  @FXML
  private Text wins;
  @FXML
  private Text points;
  @FXML
  private Text avgPoints;

  // Fills texts with according values of player logged in.
  public void setStage(MouseEvent e) throws IOException {
    String name = Main.profile.getName();
    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    userTitle.setText(name);
    games.setText(Integer.toString(Main.profile.getGames()));
    wins.setText(Integer.toString(Main.profile.getWins()));
    points.setText(String.valueOf(Main.profile.getPoints()));
    profileNickname.setText(name);
    profileColor.setText(Main.profile.getColor());
    if (Main.profile.getGames() == 0) {
      avgPoints.setText("0");
    } else {
      avgPoints.setText(String.valueOf(Main.profile.getPoints() / Main.profile.getGames()));
    }
  }

  // Changes name and color to typed value
  public void saveChanges(ActionEvent e) {
    String newColor = profileColor.getText();
    String newName = profileNickname.getText();
    if (Main.profile.checkName(newName)) {
      Main.profile.setColor(newColor);
      Main.profile.setName(newName, Main.profile.getId());

      Alert successfulSaved = new Alert(AlertType.INFORMATION);
      successfulSaved.setTitle("Successful saved!");
      successfulSaved.setContentText("Your name and color has been saved as:\n"
          + "name: " + newName + "color: " + newColor);

      Optional<ButtonType> confirm = successfulSaved.showAndWait();
      userTitle.setText(Main.profile.getName());
      profileNickname.setText(Main.profile.getName());
      profileColor.setText(Main.profile.getColor());
    } else {
      Alert nameExists = new Alert(AlertType.ERROR);
      nameExists.setTitle("Change of name failed");
      nameExists
          .setContentText("The typed name: " + newName + " already exists. Please use another!");
      Optional<ButtonType> proceed = nameExists.showAndWait();
    }
  }

  //Resets all statistics to zero.
  public void resetStatistics(ActionEvent e) {
    Alert areYouSure = new Alert(AlertType.CONFIRMATION);
    areYouSure.setTitle("Confirm Reset");
    areYouSure.setContentText("When you continue you will lose all your progress!\n"
        + "If you want to reset your score please continue");

    Optional<ButtonType> result = areYouSure.showAndWait();

    if (result.get() == ButtonType.OK) {
      Profile player = Main.profile;
      player.setGames(0, player.getId());
      player.setPoints(0, player.getId());
      player.setWins(0, player.getId());
      avgPoints.setText(String.valueOf(0));
    }
  }

  public void deleteProfile(ActionEvent e) throws SQLException, IOException {
    Connection connection = DriverManager.getConnection(jdbcUrl);
    String deleteSql = "DELETE FROM profiles WHERE ROWID=" + Main.profile.getId();
    PreparedStatement pstm = connection.prepareStatement(deleteSql);
    pstm.executeUpdate();
    Main m = new Main();
    m.changeScene("screens/startingMenu.fxml");
  }

  //Method to go back to main menu.
  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }
}
