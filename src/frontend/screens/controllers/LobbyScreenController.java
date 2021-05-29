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
import backend.basic.WordCheckDB;
import backend.network.client.ClientProtocol;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.server.Server;
import backend.network.server.ServerSettings;
import backend.tutorial.TutorialInformation;
import backend.tutorial.TutorialMatch;
import frontend.Main;
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

/*
@author jawinter
    This is the controller for the lobby screen. Here the user should be able to choose between hosting and joining a game, or starting a tutorial.
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

  // Method determines button clicked and changes to desired scene
  @FXML
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
    }
    m.changeScene(scene);
  }

  // Makes settings visible to hosting player.
  public void openStartGameView(ActionEvent e) {
    System.out.println("openStartGameView called");
    lobbyView.setVisible(false);
    joinGameView.setVisible(false);
    startGameView.setVisible(true);
    if (isHost) {
      startLobby(e);
    }
  }

  // Allows player to enter a code to join hosting player's server
  public void openJoinGameView(ActionEvent e) {
    System.out.println("openJoinGameView called");
    lobbyView.setVisible(false);
    startGameView.setVisible(false);
    joinGameView.setVisible(true);
  }

  public void startLobby(ActionEvent e) {
    System.out.println("start Lobby called");
    isHost = true;
    Player host = new Player(Main.profile.getName(), Main.profile.getColor(),
        Main.profile.getGames(), Main.profile.getWins(), Playerstatus.WAIT);
    Server server = new Server();
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
                    server.sendToAllBut(
                        Main.profile.getName(),
                        new LobbyInformationMessage(
                            Main.profile.getName(), GameInformation.getInstance().getPlayers()));
                    server.playerAdded();
                  }

                  Platform.runLater(
                      new Runnable() {
                        @Override
                        public void run() {

                          Player[] players = GameInformation.getInstance().getPlayers();

                          if (players[0] != null && !player1Name.getText().toLowerCase()
                              .equals(players[0].getName())) {
                            String name = players[0].getName().substring(0, 1).toUpperCase()
                                + players[0].getName().substring(1).toLowerCase();
                            player1Name.setText(name);
                            statPlayer1Name.setText(name);
                            statPlayer1Games.setText("" + players[0].getGames());
                            statPlayer1Wins.setText("" + players[0].getWins());
                            new Flash(player1Name).play();
                            new Flash(player1Icon).play();
                          }
                          if (players[1] != null && !player2Name.getText().toLowerCase()
                              .equals(players[1].getName())) {
                            String name = players[1].getName().substring(0, 1).toUpperCase()
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
                          if (players[2] != null && !player3Name.getText().toLowerCase()
                              .equals(players[2].getName())) {
                            String name = players[2].getName().substring(0, 1).toUpperCase()
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
                          if (players[3] != null && !player4Name.getText().toLowerCase()
                              .equals(players[3].getName())) {
                            String name = players[3].getName().substring(0, 1).toUpperCase()
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
                Main.profile.getName(), new Player(Main.profile.getName(), "", Main.profile
                .getGames(), Main.profile.getWins(), Playerstatus.WAIT)));
    clientProtocol.start();

    clientProtocol.getMatch().addProtocol(clientProtocol);
    GameInformation gameInformation = GameInformation.getInstance();
    gameInformation.setProfile(Main.profile);
    gameInformation.setHost(host);

    gameInformation.setClientmatch(clientProtocol.getMatch());
  }

  public void loadLibrary(ActionEvent e) {
    startGameButton.setVisible(false);
    loadLibraryButton.setDisable(true);
    loadLibraryButton.setText("Loading File");
    FileChooser fileChooser = new FileChooser();
    try {
      File selectedFile = fileChooser.showOpenDialog(Main.getStg());
      try {
        System.out.println(Files.probeContentType(selectedFile.toPath()));
        if (selectedFile != null && Files.probeContentType(selectedFile.toPath())
            .equals("text/plain")) {
          WordCheckDB.loadNewLibrary(selectedFile.getPath());
          loadLibraryButton.setText("Sucecss");
        } else {
          GameScreenController.AlertBox
              .display("Could not load Library", "Be sure to choose a .txt file");
        }
      } catch (IOException ioe) {
        GameScreenController.AlertBox
            .display("Could not load Library", "Be sure to choose a .txt file");
      }
    } catch (Exception exception) {
      //File selectedFile = fileChooser.showOpenDialog(Main.getStg());
    }
    startGameButton.setVisible(true);
    loadLibraryButton.setDisable(false);
  }

  /*
  Method adds an EasyAI PLayer to the lobby. If lobby is full, alert will be displayed
   */
  public void addEasyAI(ActionEvent e) {
    Player[] player = GameInformation.getInstance().getServermatch().getPlayers();
    int playerInLobby = 0;
    for (Player p : player) {
      if (p != null) {
        playerInLobby++;
      }
    }
    EasyAI newAI = new EasyAI("easyAI" + (playerInLobby + 1));
    if (!GameInformation.getInstance().getServermatch()
        .addPlayer(newAI)) {
      GameScreenController.AlertBox
          .display("ERROR", "Lobby is already full. It is not possible to add further players.");
    } else {
      GameInformation.getInstance().addPlayer(newAI);
    }
  }

  /*
  Method adds an HardAI PLayer to the lobby. If lobby is full, alert will be displayed
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
    if (!GameInformation.getInstance().getServermatch()
        .addPlayer(newAI)) {
      GameScreenController.AlertBox
          .display("ERROR", "Lobby is already full. It is not possible to add further players.");
    } else {
      GameInformation.getInstance().addPlayer(newAI);
    }
  }

  /*
  Remove all player from lobby with Playerstatus AI.
   */
  public void removeAI(){
    for(Player p : GameInformation.getInstance().getServermatch().getPlayers()) {
      if (p != null) {
        if (p.getStatus() == Playerstatus.AI) {
          GameInformation.getInstance().getServermatch().removePlayer(p.getName());
          GameInformation.getInstance().removePlayer(p.getName());
        }
      }
    }
  }

  // Method switches to playboard and starts game.
  public void startGame(ActionEvent e) {
    System.out.println("Start match triggered");
    GameInformation.getInstance().getServermatch().startMatch();
    //WordCheckDB.importTextToDB();
    GameInformation.getInstance().getChat().display();
    Main m = new Main();
    try {
      m.changeScene("screens/gameScreen.fxml");
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  // Method connects joining player to lobby or server of hosting player.
  public void enterLobby(ActionEvent e) {
    System.out.println("enter Lobby called");
    boolean validIP = true;

    if (validIP) {
      ClientProtocol cp =
          new ClientProtocol(
              adressIP.getText(),
              ServerSettings.port,
              Main.profile.getName(),
              new ClientMatch(
                  Main.profile.getName(),
                  new Player(Main.profile.getName(), "", Main.profile.getGames(), Main.profile
                      .getWins(), Playerstatus.WAIT)));
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

                            if (players[0] != null && !player1Name.getText().toLowerCase()
                                .equals(players[0].getName())) {
                              String name = players[0].getName().substring(0, 1).toUpperCase()
                                  + players[0].getName().substring(1).toLowerCase();
                              player1Name.setText(name);
                              statPlayer1Name.setText(name);
                              statPlayer1Games.setText("" + players[0].getGames());
                              statPlayer1Wins.setText("" + players[0].getWins());
                              new Flash(player1Name).play();
                              new Flash(player1Icon).play();
                            }
                            if (players[1] != null && !player2Name.getText().toLowerCase()
                                .equals(players[1].getName())) {
                              String name = players[1].getName().substring(0, 1).toUpperCase()
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
                            if (players[2] != null && !player3Name.getText().toLowerCase()
                                .equals(players[2].getName())) {
                              String name = players[2].getName().substring(0, 1).toUpperCase()
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
                            if (players[3] != null && !player4Name.getText().toLowerCase()
                                .equals(players[3].getName())) {
                              String name = players[3].getName().substring(0, 1).toUpperCase()
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
                          }
                        });
                  }
                }
              });
      lob.start();
      cp.getMatch().addProtocol(cp);
      Player player =
          new Player(Main.profile.getName(), Main.profile.getColor(), Main.profile.getGames(),
              Main.profile
                  .getWins(), Playerstatus.WAIT);
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

  // Method responsible for animations
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }

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

  /*creates a pop up screen from selectLetterSetScreen.fxml*/
  public void changeLetterSet(ActionEvent e) {
    try {
      Window window = new Window();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }


  }

  public class Window {

    public Window() throws IOException {
      display();
    }

    public void display() throws IOException {
      Parent root = FXMLLoader.load(
          getClass().getClassLoader().getResource("frontend/screens/selectLetterSetScreen.fxml"));
      Scene scene = new Scene(root);
      Stage window = new Stage();

      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("Letter Set Selector");
      window.setMinWidth(300);
      window.setMinHeight(400);

      window.setScene(scene);
      window.show();
    }
  }
}
