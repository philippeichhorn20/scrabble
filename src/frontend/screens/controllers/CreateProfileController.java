package frontend.screens.controllers;

import animatefx.animation.ZoomIn;
import frontend.Main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controller for the create profile screen. Here the user can create a new profile. The class
 * checks if the profile is already in the database; if it isn't the profile is created successfully
 *
 * @author mkolinsk
 */
public class CreateProfileController {
  @FXML private TextField username;
  @FXML private Button enterButton;
  @FXML private Text nameText;
  @FXML private Text wrongText;
  @FXML private Button backButton;

  boolean noNewNameAdded;
  String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";

  /**
   * If the name is an unique name it is entered into the database.
   *
   * @param e Click
   * @throws IOException Exception
   */
  public void checkName(ActionEvent e) throws IOException {
    check();
    if (noNewNameAdded) {
      String newName = username.getText();
      newName = newName.strip();
      newName = newName.toLowerCase();
      try {
        Connection connection = DriverManager.getConnection(jdbcUrl);
        String addSql = "INSERT INTO profiles VALUES(?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(addSql);
        stmt.setString(1, newName);
        stmt.setInt(2, 0);
        stmt.setInt(3, 0);
        stmt.setInt(4, 0);
        stmt.executeUpdate();
        Main m = new Main();
        m.changeProfile(newName);
        m.changeScene("screens/statScreen.fxml");
        System.out.println("entered " + newName + " into the system");
      } catch (SQLException | IOException sqlE) {
        sqlE.printStackTrace();
      }
    }
  }

  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/startingMenu.fxml");
  }

  /** Checks if the profile with the given name already exists in the database. */
  private void check() {
    String name = username.getText();
    name = name.strip();
    name = name.toLowerCase();
    noNewNameAdded = true;
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "SELECT * FROM profiles";
      Statement stmt = connection.createStatement();
      ResultSet result = stmt.executeQuery(sql);
      while (result.next()) {
        if (name.equals(result.getString("name"))) {
          wrongText.setText("A profile with that name already exists!");
          new ZoomIn(wrongText).play();
          noNewNameAdded = false;
          connection.close();
          break;
        }
      }

      connection.close();
    } catch (SQLException e) {
      System.out.println("Error connecting to SQL database");
      e.printStackTrace();
    }
  }
}
