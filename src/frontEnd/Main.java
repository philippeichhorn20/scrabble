package frontEnd;

import backEnd.basic.Profile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;


// @author mkolinsk

public class Main extends Application {
    public static Profile profile;
    private static Stage stg;

    /*
    *   This is the first scene the user sees when starting the application
    *   Here they can choose to either create a new profile or to log into an already existing one
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        stg = primaryStage;
        Connection connection = DriverManager.getConnection("jdbc:sqlite:src/resources/profilesdb");
        if (connection != null){
            DatabaseMetaData meta = connection.getMetaData();
            connection = DriverManager.getConnection("jdbc:sqlite:src/resources/profilesdb.db");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS profiles (\n" +
                    "name text,\n" +
                    "games integer,\n" +
                    "wins integer,\n" +
                    "points integer)");
        }
        profile = new Profile("");
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("screens/startingMenu.fxml"));
        primaryStage.setTitle("Scrabble Application");
        primaryStage.setScene(new Scene(root,1080,720));
        primaryStage.show();
    }

    /*
    *   This method is called whenever the user uses a button to go into another screen scene
    */
    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }
    public Profile getProfile() {
        return profile;
    }
    public void changeProfile(String name){
        profile = new Profile(name);
    }


    public static void main(String[] args) {
	    launch(args);
    }
}
