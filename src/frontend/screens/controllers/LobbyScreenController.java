package frontend.screens.controllers;

import animatefx.animation.Flash;
import animatefx.animation.Pulse;
import backend.ai.EasyAI;
import backend.ai.HardAI;
import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.basic.ServerMatch;
import backend.basic.WordCheckDb;
import backend.network.client.ClientProtocol;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.server.Server;
import backend.network.server.ServerSettings;
import backend.tutorial.TutorialInformation;
import backend.tutorial.TutorialMatch;
import frontend.Main;
import frontend.screens.controllertools.LetterSetHolder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller for the lobby screen. Here you can start a lobby, join an existing one, or play a
 * tutorial game.
 *
 * @author jawinter
 */
public class LobbyScreenController {

  @FXML
  private VBox joinGameView;
  @FXML
  private ImageView lobbyView;
  @FXML
  private VBox startGameView;
  @FXML
  private TextField hostIP;
  @FXML
  private TextField adressIP;
  @FXML
  private Button startGameButton;
  @FXML
  private Button loadLibraryButton;
  @FXML
  private Text player1Name;
  @FXML
  private Text player2Name;
  @FXML
  private Text player3Name;
  @FXML
  private Text player4Name;
  @FXML
  private ImageView player1Icon;
  @FXML
  private ImageView player2Icon;
  @FXML
  private ImageView player3Icon;
  @FXML
  private ImageView player4Icon;
  @FXML
  private Label statPlayer1Name;
  @FXML
  private Label statPlayer1Games;
  @FXML
  private Label statPlayer1Wins;
  @FXML
  private Label statPlayer2Name;
  @FXML
  private Label statPlayer2Games;
  @FXML
  private Label statPlayer2Wins;
  @FXML
  private Label statPlayer3Name;
  @FXML
  private Label statPlayer3Games;
  @FXML
  private Label statPlayer3Wins;
  @FXML
  private Label statPlayer4Name;
  @FXML
  private Label statPlayer4Games;
  @FXML
  private Label statPlayer4Wins;

  boolean isHost = true;

  private Server server;

  /**
   * Function that determines which button was clicked and changes scene accordingly.
   *
   * @param e Click
   * @throws IOException If source for scene is wrong.
   */
  public void changeScene(ActionEvent e) throws IOException {
    Main m = new Main();
    Button trigger = (Button) e.getSource();
    String scene = "screens/";
    switch (trigger.getId()) {
      case "startGameButton":
        startLobby(e);
        break;
      case "playTutorial":
        scene += "gameScreen.fxml";
        break;
      case "playNetworkGame":
        scene += "gameScreen.fxml";
        break;
      case "playLocalGame":
        scene += "gameScreen.fxml";
        break;
      case "backButton":
        isHost = true;
        scene += "mainMenu.fxml";
        break;
      default:
        break;
    }
    m.changeScene(scene);
  }

  /**
   * Makes the starting game view visible.
   *
   * @param e Click
   */
  public void openStartGameView(ActionEvent e) {
    lobbyView.setVisible(false);
    joinGameView.setVisible(false);
    startGameView.setVisible(true);
    if (isHost) {
      startLobby(e);
    }
  }

  /**
   * Allows player to enter a code to join hosting player's server.
   *
   * @param e Click
   */
  public void openJoinGameView(ActionEvent e) {
    lobbyView.setVisible(false);
    startGameView.setVisible(false);
    joinGameView.setVisible(true);

    if (this.server != null) {
      server.stopServer();
      GameInformation.getInstance().setPlayers(new Player[4]);
    }
  }

  /**
   * Function that starts the lobby and a thread that updates the status of the lobby.
   *
   * @param e Click.
   */
  public void startLobby(ActionEvent e) {
    if (server != null) {
      server.stopServer();
      GameInformation.getInstance().setPlayers(new Player[4]);
    }
    isHost = true;

    server = new Server();
    server.setServerMatch(new ServerMatch(server));
    GameInformation.getInstance().setServermatch(server.getServerMatch());
    Runnable r =
        new Runnable() {
          public void run() {
            server.listen();
          }
        };
    new Thread(r).start();
    Thread lob =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                while (server.isRunning()) {

                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException ie) {
                    ie.printStackTrace();
                  }
                  if (server.newPlayer()) {
                    Player[] correctPlayer = new Player[4];

                    for (int i = 0; i < 4; i++) {
                      Player p = GameInformation.getInstance().getPlayers()[i];
                      if (p != null && p.getStatus() == Playerstatus.AI) {
                        Player aiPlayer =
                            new Player(
                                GameInformation.getInstance().getPlayers()[i].getName(),
                                GameInformation.getInstance().getPlayers()[i].getColor(),
                                GameInformation.getInstance().getPlayers()[i].getGames(),
                                GameInformation.getInstance().getPlayers()[i].getWins(),
                                Playerstatus.WAIT);

                        correctPlayer[i] = aiPlayer;

                      } else {
                        correctPlayer[i] = GameInformation.getInstance().getPlayers()[i];
                      }
                    }

                    server.sendToAllBut(
                        Main.profile.getName(),
                        new LobbyInformationMessage(Main.profile.getName(), correctPlayer));
                    server.playerAdded();
                  }

                  Platform.runLater(
                      new Runnable() {
                        @Override
                        public void run() {

                          Player[] players = GameInformation.getInstance().getPlayers();

                          if (players[0] != null
                              && !player1Name
                              .getText()
                              .toLowerCase()
                              .equals(players[0].getName())) {
                            String name =
                                players[0].getName().substring(0, 1).toUpperCase()
                                    + players[0].getName().substring(1).toLowerCase();
                            player1Name.setText(name);
                            player1Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                            statPlayer1Name.setText(name);
                            statPlayer1Games.setText("" + players[0].getGames());
                            statPlayer1Wins.setText("" + players[0].getWins());
                            new Flash(player1Name).play();
                            new Flash(player1Icon).play();
                          } else if (players[0] == null) {
                            setPlayer1NotExist();
                          }
                          if (players[1] != null
                              && !player2Name
                              .getText()
                              .toLowerCase()
                              .equals(players[1].getName())) {
                            String name =
                                players[1].getName().substring(0, 1).toUpperCase()
                                    + players[1].getName().substring(1).toLowerCase();
                            player2Name.setText(name);
                            statPlayer2Name.setText(name);
                            statPlayer2Games.setText("" + players[1].getGames());
                            statPlayer2Wins.setText("" + players[1].getWins());
                            new Flash(player2Name).play();
                            player2Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                            new Flash(player2Icon).play();
                          } else if (players[1] == null) {
                            setPlayer2NotExist();
                          }
                          if (players[2] != null
                              && !player3Name
                              .getText()
                              .toLowerCase()
                              .equals(players[2].getName())) {
                            String name =
                                players[2].getName().substring(0, 1).toUpperCase()
                                    + players[2].getName().substring(1).toLowerCase();
                            player3Name.setText(name);
                            statPlayer3Name.setText(name);
                            statPlayer3Games.setText("" + players[2].getGames());
                            statPlayer3Wins.setText("" + players[2].getWins());
                            new Flash(player4Name).play();
                            player3Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                            new Flash(player3Icon).play();

                          } else if (players[2] == null) {
                            setPlayer3NotExist();
                          }
                          if (players[3] != null
                              && !player4Name
                              .getText()
                              .toLowerCase()
                              .equals(players[3].getName())) {
                            String name =
                                players[3].getName().substring(0, 1).toUpperCase()
                                    + players[3].getName().substring(1).toLowerCase();
                            player4Name.setText(name);
                            statPlayer4Name.setText(name);
                            statPlayer4Games.setText("" + players[3].getGames());
                            statPlayer4Wins.setText("" + players[3].getWins());
                            new Flash(player4Name).play();
                            player4Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                            new Flash(player4Icon).play();

                          } else if (players[3] == null) {
                            setPlayer4NotExist();
                          }
                        }
                      });
                }
              }
            });
    lob.start();
    hostIP.setText(ServerSettings.getLocalHostIp4Address());

    ClientProtocol clientProtocol =
        new ClientProtocol(
            hostIP.getText(),
            ServerSettings.port,
            Main.profile.getName(),
            new ClientMatch(
                Main.profile.getName(),
                new Player(
                    Main.profile.getName(),
                    "",
                    Main.profile.getGames(),
                    Main.profile.getWins(),
                    Playerstatus.WAIT)));
    clientProtocol.start();

    clientProtocol.getMatch().addProtocol(clientProtocol);
    GameInformation gameInformation = GameInformation.getInstance();
    gameInformation.setProfile(Main.profile);
    Player host =
        new Player(
            Main.profile.getName(),
            Main.profile.getColor(),
            Main.profile.getGames(),
            Main.profile.getWins(),
            Playerstatus.WAIT);
    gameInformation.setHost(host);

    gameInformation.setClientmatch(clientProtocol.getMatch());
  }

  /**
   * Helping function that sets the nodes in case the player 1 leaves the lobby.
   */
  public void setPlayer1NotExist() {
    player1Name.setText("Waiting");
    statPlayer1Name.setText("");
    statPlayer1Games.setText("");
    statPlayer1Wins.setText("");
  }

  /**
   * Helping function that sets the nodes in case the player 2 leaves the lobby.
   */

  public void setPlayer2NotExist() {
    player2Name.setText("Waiting");
    statPlayer2Name.setText("");
    statPlayer2Games.setText("");
    statPlayer2Wins.setText("");
  }

  /**
   * Helping function that sets the nodes in case the player 3 leaves the lobby.
   */

  public void setPlayer3NotExist() {
    player3Name.setText("Waiting");
    statPlayer3Name.setText("");
    statPlayer3Games.setText("");
    statPlayer3Wins.setText("");
  }

  /**
   * Helping function that sets the nodes in case the player 4 leaves the lobby.
   */

  public void setPlayer4NotExist() {
    player4Name.setText("Waiting");
    statPlayer4Name.setText("");
    statPlayer4Games.setText("");
    statPlayer4Wins.setText("");
  }

  /**
   * Function that loads a dictionary the host player hast chosen.
   *
   * @param e Click.
   */
  public void loadLibrary(ActionEvent e) {
    startGameButton.setVisible(false);
    loadLibraryButton.setDisable(true);
    loadLibraryButton.setText("Loading File");
    FileChooser fileChooser = new FileChooser();
    try {
      File selectedFile = fileChooser.showOpenDialog(Main.getStg());
      try {
        if (selectedFile != null
            && Files.probeContentType(selectedFile.toPath()).equals("text/plain")) {
          WordCheckDb.loadNewLibrary(selectedFile.getPath());
          loadLibraryButton.setText("Success");
        } else {
          GameScreenController.AlertBox.display(
              "Could not load Library", "Be sure to choose a .txt file");
        }
      } catch (IOException ioe) {
        GameScreenController.AlertBox.display(
            "Could not load Library", "Be sure to choose a .txt file");
      }
    } catch (Exception exception) {
      // File selectedFile = fileChooser.showOpenDialog(Main.getStg());
    }
    startGameButton.setVisible(true);
    loadLibraryButton.setDisable(false);
  }

  /**
   * Method adds an EasyAI PLayer to the lobby. If lobby is full, alert will be displayed.
   *
   * @param e Click.
   */
  public void addEasyAI(ActionEvent e) {
    Player[] player = GameInformation.getInstance().getServermatch().getPlayers();
    int playerInLobby = 0;
    for (Player p : player) {
      if (p != null) {
        playerInLobby++;
      }
    }
    EasyAI newAI = new EasyAI("easyAI " + (playerInLobby + 1));
    if (!GameInformation.getInstance().getServermatch().addPlayer(newAI)) {
      GameScreenController.AlertBox.display(
          "ERROR", "Lobby is already full. It is not possible to add further players.");
    } else {
      GameInformation.getInstance().addPlayer(newAI);
    }
  }

  /**
   * Method adds an HardAI PLayer to the lobby. If lobby is full, alert will be displayed.
   *
   * @param e Click.
   */
  public void addHardAI(ActionEvent e) {
    Player[] player = GameInformation.getInstance().getServermatch().getPlayers();
    int playerInLobby = 0;
    for (Player p : player) {
      if (p != null) {
        playerInLobby++;
      }
    }
    HardAI newAI = new HardAI("hardAI" + (playerInLobby + 1));

    if (!GameInformation.getInstance().getServermatch().addPlayer(newAI)) {
      GameScreenController.AlertBox.display(
          "ERROR", "Lobby is already full. It is not possible to add further players.");
    } else {
      GameInformation.getInstance().addPlayer(newAI);
    }
  }

  /**
   * Removes all players from lobby with Playerstatus AI.
   */
  public void removeAI() {
    for (Player p : GameInformation.getInstance().getServermatch().getPlayers()) {
      if (p != null) {
        if (p.getStatus() == Playerstatus.AI) {
          GameInformation.getInstance().getServermatch().removePlayer(p.getName());
          GameInformation.getInstance().removePlayer(p.getName());
        }
      }
    }
  }

  /**
   * Method that switches the screen to game screen.
   *
   * @param e Click.
   * @throws IOException if source for game screen is wrong.
   */
  public void startGame(ActionEvent e) throws IOException {
    GameInformation.getInstance()
        .getServermatch()
        .getTileBag()
        .importBagSet(LetterSetHolder.getInstance().getTileSet());
    GameInformation.getInstance().getServermatch().startMatch();
    WordCheckDb.importTextToDB();
    Main m = new Main();
    m.changeScene("screens/gameScreen.fxml");
    GameInformation.getInstance().getChat().display();
  }

  /**
   * Method connects joining player to lobby and server of hosting player.
   *
   * @param e Click
   */
  public void enterLobby(ActionEvent e) {
    boolean validIP = true;

    if (validIP) {
      ClientProtocol cp =
          new ClientProtocol(
              adressIP.getText(),
              ServerSettings.port,
              Main.profile.getName(),
              new ClientMatch(
                  Main.profile.getName(),
                  new Player(
                      Main.profile.getName(),
                      "",
                      Main.profile.getGames(),
                      Main.profile.getWins(),
                      Playerstatus.WAIT)));
      cp.start();
      Thread lob =
          new Thread(
              new Runnable() {
                @Override
                public void run() {
                  while (true) {

                    try {
                      Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                      ie.printStackTrace();
                    }

                    Platform.runLater(
                        new Runnable() {
                          @Override
                          public void run() {

                            Player[] players = GameInformation.getInstance().getPlayers();
                            if (players[0] == null) {
                              setPlayer1NotExist();
                            }
                            if (players[0] != null
                                && !player1Name
                                .getText()
                                .toLowerCase()
                                .equals(players[0].getName())) {
                              String name =
                                  players[0].getName().substring(0, 1).toUpperCase()
                                      + players[0].getName().substring(1).toLowerCase();
                              player1Name.setText(name);
                              player1Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                              statPlayer1Name.setText(name);
                              statPlayer1Games.setText("" + players[0].getGames());
                              statPlayer1Wins.setText("" + players[0].getWins());
                              new Flash(player1Name).play();
                              new Flash(player1Icon).play();
                            }
                            if (players[1] == null) {
                              setPlayer2NotExist();
                            }
                            if (players[1] != null
                                && !player2Name
                                .getText()
                                .toLowerCase()
                                .equals(players[1].getName())) {
                              String name =
                                  players[1].getName().substring(0, 1).toUpperCase()
                                      + players[1].getName().substring(1).toLowerCase();
                              player2Name.setText(name);
                              statPlayer2Name.setText(name);
                              statPlayer2Games.setText("" + players[1].getGames());
                              statPlayer2Wins.setText("" + players[1].getWins());
                              new Flash(player2Name).play();
                              player2Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                              new Flash(player2Icon).play();
                            }
                            if (players[2] != null
                                && !player3Name
                                .getText()
                                .toLowerCase()
                                .equals(players[2].getName())) {
                              String name =
                                  players[2].getName().substring(0, 1).toUpperCase()
                                      + players[2].getName().substring(1).toLowerCase();
                              player3Name.setText(name);
                              statPlayer3Name.setText(name);
                              statPlayer3Games.setText("" + players[2].getGames());
                              statPlayer3Wins.setText("" + players[2].getWins());
                              new Flash(player4Name).play();
                              player3Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                              new Flash(player3Icon).play();
                            }
                            if (players[2] == null) {
                              setPlayer3NotExist();
                            }
                            if (players[3] != null
                                && !player4Name
                                .getText()
                                .toLowerCase()
                                .equals(players[3].getName())) {
                              String name =
                                  players[3].getName().substring(0, 1).toUpperCase()
                                      + players[3].getName().substring(1).toLowerCase();
                              player4Name.setText(name);
                              statPlayer4Name.setText(name);
                              statPlayer4Games.setText("" + players[3].getGames());
                              statPlayer4Wins.setText("" + players[3].getWins());
                              new Flash(player4Name).play();
                              player4Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                              new Flash(player4Icon).play();
                            }
                            if (players[3] == null) {
                              setPlayer4NotExist();
                            }
                          }
                        });
                  }
                }
              });
      lob.start();
      cp.getMatch().addProtocol(cp);
      Player player =
          new Player(
              Main.profile.getName(),
              Main.profile.getColor(),
              Main.profile.getGames(),
              Main.profile.getWins(),
              Playerstatus.WAIT);
      GameInformation gameInformation = GameInformation.getInstance();
      gameInformation.setProfile(Main.profile);
      gameInformation.setHost(player);
      gameInformation.setClientmatch(cp.getMatch());
    }
    isHost = false;
    openStartGameView(e);
    startGameButton.setVisible(false);
    loadLibraryButton.setVisible(false);
  }

  /**
   * Method responsible for animations.
   *
   * @param e Mouseover
   */
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }

  /**
   * Method which starts the tutorial game.
   *
   * @throws IOException if source for tutorial screen is wrong.
   */
  public void startTutorial() throws IOException {
    Alert startTutorial = new Alert(AlertType.CONFIRMATION);

    startTutorial.setTitle("Start Tutorial");
    startTutorial.setHeaderText(null);
    startTutorial.setContentText("Do you want to start the Tutorial?");

    Optional<ButtonType> result = startTutorial.showAndWait();
    if (result.get() == ButtonType.OK) {
      TutorialMatch match = new TutorialMatch();
      TutorialInformation.getInstance().setTutorialmatch(match);
      TutorialInformation.getInstance().getTutorialMatch().startTutorial();
      Main m = new Main();

      m.changeScene("screens/tutorialScreen.fxml");
    } else {
      // ... user chose CANCEL or closed the dialog
    }
  }


  /**
   * Creates instance of window which shows the select letter set fxml.
   *
   * @param e Click
   */
  public void changeLetterSet(ActionEvent e) {
    try {
      Window window = new Window();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  /**
   * Creates pop up screen from Select letter set fxml file.
   */
  public class Window {

    public Window() throws IOException {
      display();
    }

    /**
     * Displays the window.
     *
     * @throws IOException if the source for the screen is wrong.
     */
    public void display() throws IOException {
      Parent root =
          FXMLLoader.load(
              getClass()
                  .getClassLoader()
                  .getResource("frontend/screens/selectLetterSetScreen.fxml"));
      Scene scene = new Scene(root);
      Stage window = new Stage();

      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("Letter Set Selector");
      window.setMinWidth(300);
      window.setMinHeight(400);

      window.setScene(scene);
      window.show();
      LetterSetHolder.getInstance().setWindow(window);
    }
  }
}
