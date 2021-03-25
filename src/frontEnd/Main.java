import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


// @author mkolinsk

public class Main extends Application {

    private static Stage stg;

    /*
        This is the first scene the user sees when starting the application
        Here they can choose to either create a new profile or to log into an already existing one
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        stg = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("resources/startingMenu.fxml"));
        primaryStage.setTitle("Scrabble Application");
        primaryStage.setScene(new Scene(root,1080,720));
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);3
    }

    public static void main(String[] args) {
	    launch(args);
    }
}
