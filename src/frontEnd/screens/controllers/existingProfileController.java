package frontEnd.screens.controllers;

import backEnd.basic.Profile;
import frontEnd.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.*;
import java.util.Locale;

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
    @FXML
    private Button nextButton;
    @FXML
    private Button lastPageButton;

    public String profile;
    private boolean first = true;


    public void goBack(ActionEvent e) throws IOException {
        Main m = new Main();
        m.changeScene("screens/startingMenu.fxml");
    }
    public void nextPage(ActionEvent e) throws IOException {
        clear();
        lastPageButton.setVisible(true);
        nextButton.setVisible(false);
        Button[] buttonNames = {button00, button01, button02, button03, button10, button11, button12, button13, button20, button21, button22, button23, button30, button31, button32, button33};
        String jdbcUrl = "jdbc:sqlite:/IntelliJ/scrabble14-master/scrabble14-master/src/resources/profilesdb.db";
        int i = 0;
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT rowid, name FROM profiles";

            Statement stmt = connection.createStatement();

            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                if (i < 16) {
                    buttonNames[i].setText(" ");

                }
                else{
                    String name = result.getString("name");
                    name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                    buttonNames[i-16].setText(name);

                }
                i++;

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void lastPage(ActionEvent e) throws IOException {
        loadProfiles();
        lastPageButton.setVisible(false);
    }
    public void load(MouseEvent e) throws IOException {
        if (first){
            loadProfiles();
            lastPageButton.setVisible(false);
            first = false;
        }
    }
    public void showStats(ActionEvent e) throws IOException {
        Button button = (Button) e.getSource();
        Main m = new Main();
        m.changeProfile(button.getText().toLowerCase());
        System.out.println(button.getText());
        System.out.println(Main.profile.getGames());
        System.out.println(Main.profile.getPoints());
        m.changeScene("screens/statScreen.fxml");




    }
    private void loadProfiles(){
        //checks if there are more than 16 profiles in the database. If so, a button appears so that all the profiles can be loaded.


        Button[] buttonNames = {button00, button01, button02, button03, button10, button11, button12, button13, button20, button21, button22, button23, button30, button31, button32, button33};
        String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";
        int i = 0;
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT rowid, name FROM profiles";

            Statement stmt = connection.createStatement();

            ResultSet result = stmt.executeQuery(sql);
            while (result.next()) {
                if (i >= 16){
                    nextButton.setVisible(true);
                    break;

                }
                String name = result.getString("name");
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                buttonNames[i].setText(name);
                i++;
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    private void clear(){
        Button[] buttonNames = {button00, button01, button02, button03, button10, button11, button12, button13, button20, button21, button22, button23, button30, button31, button32, button33};
        for (int i = 0;i<16;i++){
            buttonNames[i].setText(" ");
        }
    }




}
