package frontEnd.screens.controllers;

import backend.basic.Profile;
import frontend.Main;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

/*
* @author nilschae
* @version 1.0
* @descritption A controller to manage different settings of a profile
* */
public class ManageProfileController {
  @FXML private Button BT_Save;
  @FXML private Button BT_ResetScore;
  @FXML private Button BT_DeleteProfile;
  @FXML private Button BT_GoBack;
  @FXML private Label LB_ProfileName;
  @FXML private Label LB_ProfileColor;
  @FXML private Label LB_Wins;
  @FXML private Label LB_Games;
  @FXML private Label LB_Points;
  @FXML private TextField TF_ProfileName;
  @FXML private TextField TF_ProfileColor;


  private boolean setUpDone = false;
  Profile currentProfile;

  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      currentProfile = Main.profile;
      String name = currentProfile.getName();
      String color = currentProfile.getColor();

      name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

      TF_ProfileName.setText(name);
      TF_ProfileColor.setText(color);

      LB_Games.setText("Games: " + currentProfile.getGames());
      LB_Points.setText("Points: " + currentProfile.getPoints());
      LB_Wins.setText("Wins: " + currentProfile.getWins());

      setUpDone = true;
    }
  }

  public void save() {
    String newName = TF_ProfileName.getText();
    String newColor = TF_ProfileColor.getText();

    currentProfile.setName(newName, currentProfile.getId());
    currentProfile.setColor(newColor);

    Alert successfulSaved = new Alert(AlertType.INFORMATION);
    successfulSaved.setTitle("Successful saved!");
    successfulSaved.setContentText("Your name and color has been saved as:\n"
        + "name: " + newName + "color: " + newColor);

  }

  public void resetScore() {
    Alert areYouSure = new Alert(AlertType.CONFIRMATION);
    areYouSure.setTitle("Confirm Reset");
    areYouSure.setContentText("When you continue you will lose all your progress!\n"
        + "If you want to reset your score please continue");

    Optional<ButtonType> result = areYouSure.showAndWait();

    if(result.get() == ButtonType.OK) {
      currentProfile.setWins(0,currentProfile.getId());
      currentProfile.setGames(0,currentProfile.getId());
      currentProfile.setPoints(0,currentProfile.getId());
    }

    LB_Games.setText("Games: " + currentProfile.getGames());
    LB_Points.setText("Points: " + currentProfile.getPoints());
    LB_Wins.setText("Wins: " + currentProfile.getWins());
  }

  public void goBack() throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }

}
