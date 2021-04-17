package frontEnd.screens.controllers;

import frontend.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

/*
* @author nilschae
* */
public class ManageProfileController {
  @FXML private Button BT_SaveChanges;
  @FXML private Button BT_ResetScore;
  @FXML private Button BT_DeleteProfile;
  @FXML private Button BT_GoBack;
  @FXML private Label LB_ProfileName;
  @FXML private Label LB_ProfileColor;
  @FXML private Label LB_TotalPoints;
  @FXML private Label LB_Level;
  @FXML private Label LB_EXP;
  @FXML private TextField TF_ProfileName;
  @FXML private TextField TF_ProfileColor;
  @FXML private ProgressBar PB_EXP;

  private boolean setUpDone = false;

  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      String name = frontend.Main.profile.getName();
      String color = Main.profile.getColor();
      name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
      TF_ProfileName.setText(name);
      TF_ProfileColor.setText(color);
      setUpDone = true;
    }
  }


}
