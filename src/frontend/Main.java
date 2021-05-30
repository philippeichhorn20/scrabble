package frontend;

import animatefx.animation.FadeIn;
import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Lobby;
import backend.basic.Profile;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main class which you start the application with.
 *
 * @author mkolinsk
 * @version 1.0
 */
public class Main extends Application {
  /** The profile the user is currently on; starts with an empty profile on the starting screen. */
  public static Profile profile;

  private static Stage stg;

  public static Lobby lobby;

  public static ClientMatch clientMatch;

  /**
   * Starts the application, creates an empty database if not already created.
   *   
   * @param primaryStage The stage which is shown on the screen
   * @throws Exception exception
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    stg = primaryStage;
    Connection connection = DriverManager.getConnection("jdbc:sqlite:src/resources/profilesdb");
    if (connection != null) {
      DatabaseMetaData meta = connection.getMetaData();
      connection = DriverManager.getConnection("jdbc:sqlite:src/resources/profilesdb.db");
      Statement stmt = connection.createStatement();
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS profiles (\n"
              + "              name text,\n"
              + "              games integer,\n"
              + "              wins integer,\n"
              + "              points integer,\n"
              + "              color text)");
    }
    profile = new Profile("");
    primaryStage.setResizable(false);
    Parent root = FXMLLoader.load(getClass().getResource("screens/startingMenu.fxml"));
    primaryStage.getIcons().add(new Image("frontend/screens/resources/gameIcon.png"));
    primaryStage.setTitle("Scrabble");
    primaryStage.setScene(new Scene(root, 1080, 720));
    GameInformation.getInstance().startMusic();
    primaryStage.show();
    
  }

  /**
   * Changes the scene.
   *
   * @param fxml name of the screen file
   * @throws IOException If the screen file doesn't exist
   */
  public void changeScene(String fxml) throws IOException {
    Parent pane = FXMLLoader.load(getClass().getResource(fxml));
    stg.getScene().setRoot(pane);
  }

  @Override
  public void stop() {
    if (GameInformation.getInstance().getServermatch() != null) {
      if (GameInformation.getInstance().getServermatch().getServer() != null) {
        GameInformation.getInstance().getServermatch().getServer().stopServer();
      }
    }
  }

  /**
   * Changes the scene but with an animation. The animation is fixed for now, feel free to add new
   * methods with different animation or change this one.
   *
   * @param fxml name of the screen file
   * @throws IOException If the screen file doesn't exist
   */
  public void changeSceneAnimate(String fxml) throws IOException {
    Parent pane = FXMLLoader.load(getClass().getResource(fxml));
    new FadeIn(pane).play();
    stg.getScene().setRoot(pane);
  }

  public Profile getProfile() {
    return profile;
  }

  /**
   * Changes the used profile.
   *
   * @param name Name of the profile.
   */
  public void changeProfile(String name) {
    profile = new Profile(name);
  }

  public static Stage getStg() {
    return stg;
  }

  public static void main(String[] args) {
    launch(args);
    
  }
}