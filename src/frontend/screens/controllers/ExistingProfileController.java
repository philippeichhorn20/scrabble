package frontend.screens.controllers;

import animatefx.animation.Flash;
import frontend.Main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Controller for the existing profile screen, where the user can log into an existing profile.
 *
 * @author mkolinsk
 */
public class ExistingProfileController {
  @FXML private Button button00;
  @FXML private Button button01;
  @FXML private Button button02;
  @FXML private Button button03;
  @FXML private Button button10;
  @FXML private Button button11;
  @FXML private Button button12;
  @FXML private Button button13;
  @FXML private Button backButton;
  @FXML private ImageView nextButton;
  @FXML private ImageView lastPageButton;
  @FXML private Text scrabbleText;

  public String profile;
  private boolean first = true;
  private static int currentPage = 0;
  private int totalPages;
  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/startingMenu.fxml");
  }

  /**
   * Leads to the next page of profiles when clicked on.
   *
   * @param e Click
   */
  public void nextPage(MouseEvent e) {
    clear();
    currentPage++;
    lastPageButton.setVisible(true);
    //nextButton.setVisible(false);
    Button[] buttonNames = {
      button00, button01, button02, button03, button10, button11, button12, button13
    };
    String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";
    int i = 0;
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "SELECT rowid, name FROM profiles";

      Statement stmt = connection.createStatement();

      ResultSet result = stmt.executeQuery(sql);
      while (result.next()) {
        if (i < currentPage*8) {
          //buttonNames[i].setText(" ");
        }
        else if (i >= (currentPage+1)*8){
          break;
        }else {
          String name = result.getString("name");
          name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
          buttonNames[i - currentPage*8].setText(name);
        }
        i++;
      }

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  /**
   * Goes to the previous page of profiles when clicked on.
   *
   * @param e Click
   */
  public void lastPage(MouseEvent e) {

    currentPage--;
    Button[] buttonNames = {
        button00, button01, button02, button03, button10, button11, button12, button13
    };
    String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";
    int i = 0;
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "SELECT rowid, name FROM profiles";

      Statement stmt = connection.createStatement();

      ResultSet result = stmt.executeQuery(sql);
      while (result.next()) {
        if (i < currentPage*8) {
          //buttonNames[i].setText(" ");
        }
        else if (i >= (currentPage+1)*8){
          break;
        }else {
          String name = result.getString("name");
          name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
          buttonNames[i - currentPage*8].setText(name);
        }
        i++;
      }

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    if(currentPage == 0){
      lastPageButton.setVisible(false);
    }
  }

  /**
   * Loads the profiles onto the screen.
   *
   * @param e Click
   * @throws IOException IOException
   */
  public void load(MouseEvent e) throws IOException {
    if (first) {
      loadProfiles();
      lastPageButton.setVisible(false);
      first = false;
    }
  }

  /**
   * Player clicks on a profile and is transferred into the main menu.
   *
   * @param e Click
   * @throws IOException IOException
   */
  public void goMainMenu(ActionEvent e) throws IOException {
    Button button = (Button) e.getSource();
    Main m = new Main();
    m.changeProfile(button.getText().toLowerCase());
    m.changeScene("screens/mainMenu.fxml");
  }

  /**
   * If the amount of existing profiles exceeds the 16 buttons shown on the screen, a button shows *
   * up which leads the user to another page of profiles.
   */
  private void loadProfiles() {

    Button[] buttonNames = {
      button00, button01, button02, button03, button10, button11, button12, button13
    };
    String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";
    int i = 0;
    int j = 0;
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "SELECT rowid, name FROM profiles";

      Statement stmt = connection.createStatement();

      ResultSet result = stmt.executeQuery(sql);
      //calculates total number of profiles
      while (result.next()){
        j++;
      }
      totalPages = j/8+1;
      result = stmt.executeQuery(sql);
      while (result.next()) {
        if (i >= 8) {
          nextButton.setVisible(true);
          break;
        }
        String name = result.getString("name");
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        buttonNames[i].setText(name);
        i++;
      }

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  private void clear() {
    Button[] buttonNames = {
      button00, button01, button02, button03, button10, button11, button12, button13
    };
    for (int i = 0; i < 8; i++) {
      buttonNames[i].setText(" ");
    }
  }

  public void animate(MouseEvent e) {
    new Flash((Button) e.getSource()).play();
  }
}
