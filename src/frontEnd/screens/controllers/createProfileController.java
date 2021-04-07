package frontEnd.screens.controllers;

import frontEnd.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;

//@author mkolinsk

public class createProfileController {
    @FXML
    private TextField username;
    @FXML
    private Button enterButton;
    @FXML
    private Text nameText;
    @FXML
    private Text wrongText;
    @FXML
    private Button backButton;

    boolean noNewNameAdded;
    String jdbcUrl = "jdbc:sqlite:/IntelliJ/scrabble14-master/scrabble14-master/src/resources/profilesdb.db";

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
                stmt.setString(1,newName);
                stmt.setInt(2,0);
                stmt.setInt(3,0);
                stmt.setInt(4,0);
                stmt.executeUpdate();
                /*
                    @TODO
                    When class profile exists, implement it as such so the profile gets locked in to the user
                    also change startingMenu.fxml to the mainMenu.fxml once it exists
                 */
                Main m = new Main();
                m.changeProfile(newName);
                m.changeScene("screens/statScreen.fxml");
                System.out.println(m.getProfile().getName());
                m.getProfile().setWins(234,m.getProfile().getId());
                System.out.println(m.getProfile().getWins());

                System.out.println("entered " + newName + " into the system");
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            } catch (IOException iE) {
                iE.printStackTrace();
            }
        }

    }

    public void goBack(ActionEvent e) throws IOException {
        Main m = new Main();
        m.changeScene("screens/startingMenu.fxml");
    }


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
                    nameText.setVisible(false);
                    wrongText.setText("A profile with that name already exists!");
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

