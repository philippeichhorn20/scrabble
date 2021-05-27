package frontend.screens.controllers;

import animatefx.animation.Pulse;
import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.basic.ServerMatch;
import backend.network.client.ClientProtocol;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.server.Server;
import backend.network.server.ServerSettings;
import frontend.Main;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
@author jawinter
    This is the controller for the lobby screen. Here the user should be able to choose between hosting and joining a game, or starting a tutorial.
 */
public class LobbyScreenController {

  @FXML private VBox joinGameView;
  @FXML private ImageView lobbyView;
  @FXML private VBox startGameView;
  @FXML private TextField hostIP;
  @FXML private TextField adressIP;
  @FXML private Button startGameButton;
  @FXML private Text player1Name;
  @FXML private Text player2Name;
  @FXML private Text player3Name;
  @FXML private Text player4Name;
  @FXML private ImageView player2Icon;
  @FXML private ImageView player3Icon;
  @FXML private ImageView player4Icon;

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
    Player host = new Player(Main.profile.getName(), Main.profile.getColor(), Playerstatus.WAIT);
    player1Name.setText(host.getName());

    Server server = new Server();

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
                  if (server.newPlayer()){
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

                          if (players[0] != null) {
                            player1Name.setText(
                                players[0].getName().substring(0, 1).toUpperCase()
                                    + players[0].getName().substring(1).toLowerCase());
                          }
                          if (players[1] != null) {
                            player2Name.setText(
                                players[1].getName().substring(0, 1).toUpperCase()
                                    + players[1].getName().substring(1).toLowerCase());
                            player2Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                          }
                          if (players[2] != null) {
                            player3Name.setText(
                                players[2].getName().substring(0, 1).toUpperCase()
                                    + players[2].getName().substring(1).toLowerCase());
                            player3Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                          }
                          if (players[3] != null) {
                            player4Name.setText(
                                players[3].getName().substring(0, 1).toUpperCase()
                                    + players[3].getName().substring(1).toLowerCase());
                            player4Icon.setImage(
                                new Image("frontend/screens/resources/playerIcon.png"));
                          }
                        }
                      });
                }
              }
            });
    lob.start();
    server.setServerMatch(new ServerMatch(server, GameInformation.getInstance().getPlayers()));
    GameInformation.getInstance().setServermatch(server.getServerMatch());

    hostIP.setText(ServerSettings.getLocalHostIp4Address());

    ClientProtocol clientProtocol =
        new ClientProtocol(
            hostIP.getText(),
            ServerSettings.port,
            Main.profile.getName(),
            new ClientMatch(
                Main.profile.getName(), new Player(Main.profile.getName(), "", Playerstatus.WAIT)));
    clientProtocol.start();

    clientProtocol.getMatch().addProtocol(clientProtocol);
    GameInformation gameInformation = GameInformation.getInstance();
    gameInformation.setProfile(Main.profile);
    gameInformation.setHost(host);

    gameInformation.setClientmatch(clientProtocol.getMatch());
  }

  // Method switches to playboard and starts game.
  public void startGame(ActionEvent e) {
    System.out.println("Start match triggered");
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
                  new Player(Main.profile.getName(), "", Playerstatus.WAIT)));
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

                            if (players[0] != null) {
                              player1Name.setText(
                                  players[0].getName().substring(0, 1).toUpperCase()
                                      + players[0].getName().substring(1).toLowerCase());
                            }
                            if (players[1] != null) {
                              player2Name.setText(
                                  players[1].getName().substring(0, 1).toUpperCase()
                                      + players[1].getName().substring(1).toLowerCase());
                              player2Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                            }
                            if (players[2] != null) {
                              player3Name.setText(
                                  players[2].getName().substring(0, 1).toUpperCase()
                                      + players[2].getName().substring(1).toLowerCase());
                              player3Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                            }
                            if (players[3] != null) {
                              player4Name.setText(
                                  players[3].getName().substring(0, 1).toUpperCase()
                                      + players[3].getName().substring(1).toLowerCase());
                              player4Icon.setImage(
                                  new Image("frontend/screens/resources/playerIcon.png"));
                            }
                          }
                        });
                  }
                }
              });
      lob.start();
      cp.getMatch().addProtocol(cp);
      Player player =
          new Player(Main.profile.getName(), Main.profile.getColor(), Playerstatus.WAIT);
      GameInformation gameInformation = GameInformation.getInstance();
      gameInformation.setProfile(Main.profile);
      gameInformation.setHost(player);
      gameInformation.setClientmatch(cp.getMatch());
    }
    isHost = false;
    openStartGameView(e);
    startGameButton.setVisible(false);
  }

  // Method responsible for animations
  public void animate(MouseEvent e) {
    new Pulse((Button) e.getSource()).play();
  }
}
