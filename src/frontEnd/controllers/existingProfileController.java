import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class existingProfileController {
    @FXML
    private Button button00;
    @FXML
    private Button button01;
    @FXML
    private Button button02;
    @FXML
    private Button button03;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button13;
    @FXML
    private Button button20;
    @FXML
    private Button button21;
    @FXML
    private Button button22;
    @FXML
    private Button button23;
    @FXML
    private Button button30;
    @FXML
    private Button button31;
    @FXML
    private Button button32;
    @FXML
    private Button button33;
    @FXML
    private BorderPane bp;
    @FXML
    private Button backButton;

    public void goBack(ActionEvent e) throws IOException {
        Main m = new Main();
        m.changeScene("resources/startingMenu.fxml");
    }

    public void load(MouseEvent e) throws IOException {
        Button[] buttonNames = {button00,button01,button02,button03,button10,button11,button12,button13,button20,button21,button22,button23,button30,button31,button32,button33};
        String jdbcUrl = "jdbc:sqlite:/scrabble14-master/scrabble14-master/src/resources/profilesdb.db"";
        int i = 0;
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT * FROM profiles";

            Statement stmt = connection.createStatement();

            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                buttonNames[i].setText(result.getString("name"));
                i++;
            }
            }
        catch (SQLException sqle) {
           sqle.printStackTrace();
        }

    }




}
