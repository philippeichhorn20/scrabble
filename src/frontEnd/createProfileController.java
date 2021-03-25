import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;

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

    boolean nonewNameAdded = true;

    String jdbcUrl = "jdbc:sqlite:/E:\\IntelliJ\\scrabbleFront\\src\\resources/profilesdb.db";

    public void checkName(ActionEvent e) throws IOException {
        check();
        if (nonewNameAdded) {
            String newName = username.getText();
            newName = newName.strip();
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl);
                String addSql = "INSERT INTO profiles VALUES('" + newName + "');";
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(addSql);
                /*
                    @TODO
                    When class profile exists, implement it as such so the profile gets locked in to the user
                    also change startingMenu.fxml to the mainMenu.fxml once it exists
                 */
                Main m = new Main();
                m.changeScene("resources/startingMenu.fxml");
                System.out.println("entered " + newName + "into the system");
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            } catch (IOException iE){
                iE.printStackTrace();
            }
        }

    }

    public void goBack(ActionEvent e) throws IOException {
        Main m = new Main();
        m.changeScene("resources/startingMenu.fxml");
    }



    private void check() {
        String name = username.getText();
        name = name.strip();
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT * FROM profiles";
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                if (name.equals(result.getString("name"))) {
                    nameText.setVisible(false);
                    wrongText.setText("A profile with that name already exists!");
                    nonewNameAdded = false;
                    connection.close();
                    break;
                }

            }
            connection.close();
            } catch(SQLException e){
                System.out.println("Error connecting to SQL database");
                e.printStackTrace();
            }

        }
    }

