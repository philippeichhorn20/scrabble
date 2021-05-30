package frontend.screens.controllers;

import animatefx.animation.Bounce;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.Pulse;
import animatefx.animation.SlideInLeft;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomInDown;
import backend.basic.ClientMatch;
import backend.basic.GameInformation;
import backend.basic.GraphicTile;
import backend.basic.Player;
import backend.basic.Tile;
import backend.basic.TileBag;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.tiles.SendStartRackMessage;
import frontend.Main;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller for the game screen. It's here where the interaction between the front-end of the
 * application and the back-end.
 *
 * @author mkolinsk,peichhor
 */
public class GameScreenController extends Thread {

  @FXML private Rectangle tile1;
  @FXML private Rectangle tile2;
  @FXML private Rectangle tile3;
  @FXML private Rectangle tile4;
  @FXML private Rectangle tile5;
  @FXML private Rectangle tile6;
  @FXML private Rectangle tile7;
  @FXML private Text text1;
  @FXML private Text text2;
  @FXML private Text text3;
  @FXML private Text text4;
  @FXML private Text text5;
  @FXML private Text text6;
  @FXML private Text text7;
  @FXML private Label name1;
  @FXML private Label name2;
  @FXML private Label name3;
  @FXML private Label name4;
  @FXML private Label nameScore1;
  @FXML private Label nameScore2;
  @FXML private Label nameScore3;
  @FXML private Label nameScore4;
  @FXML private Label history1;
  @FXML private Label history2;
  @FXML private Label history3;
  @FXML private Label history4;
  @FXML private Label history5;
  @FXML private Label history6;
  @FXML private Label history7;
  @FXML private Label history8;
  @FXML private Label history9;
  @FXML private Label history10;
  @FXML private Label history11;
  @FXML private GridPane board;
  @FXML private Label time;
  @FXML private Button openChatButton;
  @FXML private AnchorPane pane;
  @FXML private ImageView tileBagIcon;
  @FXML private Button resetTilesButton;
  @FXML public Label currPlayerText;
  @FXML private Label serverMessage;
  @FXML private Button endTurnButton;

  private ClientMatch match = GameInformation.getInstance().getClientmatch();
  private final GraphicTile[] gtiles = new GraphicTile[7];
  private final GameInformation gameInformation = GameInformation.getInstance();
  private ArrayList<Tile> placeTilesList = new ArrayList<>();
  private final Label[] history = new Label[11];
  private static char jokerChar;
  private boolean[][] tilesOnBoard = new boolean[17][16];
  private boolean setUpDone = false;
  private int turn = 0;

  public void goSettings(MouseEvent e) {
    // TODO:
    // CREATE SETTINGS SCREEN IN NEW WINDOW FOR GAME SCREEN
  }

  /**
   * This function is called when an important game event happens. It enters the text message from
   * the client match into the log chat on the left
   */
  public void writeTextMessages() {
    ArrayList<String> textMessageClone = (ArrayList<String>) match.getTextMessages().clone();
    for (String s : textMessageClone) {
      newHistoryMessageOther(s);
    }
    match.getTextMessages().clear();
  }

  /**
   * Function that is called when the end turn button gets clicked. Generally only calls upon the
   * endTurnFunction.
   *
   * @param e Click on the end turn button
   */
  public void endTurn(ActionEvent e) {
    new Bounce(endTurnButton).play();
    endTurnFunction();
  }
  /**
   * This function sends the tiles that are placed on the current turn to the serverthrough a
   * function in Clientmatch.
   */

  private void endTurnFunction() {
    if (this.match.getScrabbleBoard().getNewTilesOfCurrentMove().size() > 0) {
      activateServerMessage("Checking the word...");
    }
    GameInformation.getInstance().getClientmatch().sendPlacedTilesToServer();
  }
  /** AlertBox that pops up after the player has won the game. */

  public void sendWinBox() {
    TutorialScreenController.AlertBox.display("You win!", "Congratulations,you have won!");
  }
  /** AlertBox that pops up after the player has lost the game. */

  public void sendLostBox() {
    TutorialScreenController.AlertBox.display("Game over", "The game is over, you have lost");
  }

  /**
   * Internal function that is called when a tile is placed onto the scrabbleboard.
   *
   * @param tile The tile that needs to be placed.
   */
  private void setTile(Tile tile) {
    Rectangle newTile = new Rectangle(36, 36, Color.web("#ffe5b4"));
    newTile.setStroke(Color.BLACK);
    newTile.setStyle("-fx-stroke-width: 1");
    Text letter = new Text("  " + tile.getLetter());
    letter.setFont(new Font("Times New Roman", 20));
    GraphicTile gt = new GraphicTile(newTile, letter);
    board.add(gt, tile.getX(), tile.getY());
  }

  public void getNewTiles(MouseEvent e) {
    shuffleTiles();
  }

  private void drawTiles() {
    showServerMessage("Exchanging your tiles", 3);
    resetColor();
    int i = 0;
    new FadeOut(tileBagIcon).play();
    for (GraphicTile gt : gtiles) {
      if (!gt.isVisiblee() || gt.toDraw()) {
        Tile newTile = new TileBag().drawTile();
        i++;
        Text let = new Text(String.valueOf(newTile.getLetter()));
        let.setLayoutX((gt.getLetter().getLayoutX()));
        let.setLayoutY((gt.getLetter().getLayoutY()));
        let.setFont(gt.getLetter().getFont());
        let.setMouseTransparent(true);
        pane.getChildren().add(let);
        gt.setXy(gt.getRec().getLayoutX(), gt.getRec().getLayoutY());
        pane.getChildren().remove(gt.getLetter());
        gt.setLetter(let);
        gt.setVisiblee(true);
        gt.setTile(newTile);
        new SlideInLeft(gt.getRec()).play();
        new SlideInLeft(gt.getLetter()).play();
      }
    }
  }

  /**
   * This functions gets called internally when the players wants to exchange their tiles. Those are
   * set by clicking onto their tile on the rack while pressing control.
   *
   * @return Tile array that will be put into the game Tile bag.
   */
  private Tile[] tilesToSwitch() {
    activateServerMessage("New tiles are loading...");
    ArrayList<Tile> tiles = new ArrayList<>();
    for (GraphicTile gt : gtiles) {
      if (gt.toDraw()) {
        tiles.add(gt.getTile());
      }
    }
    Tile[] tilesToDraw = new Tile[tiles.size()];
    for (int x = 0; x < tilesToDraw.length; x++) {
      tilesToDraw[x] = tiles.get(x);
    }
    return tilesToDraw;
  }

  /**
   * Gets called when the player clicks the tile bag icon. If the player has not chosen any tiles to
   * exchange, a message will be shown. If the player has marked one or more tiles to be exchanged
   * (by pressing ctrl+left click) those will be put into the tile bag and the player will receive
   * new ones, and the turn will end.
   */
  public void shuffleTiles() {
    new FadeOut(tileBagIcon).play();
    tileBagIcon.setMouseTransparent(true);
    Tile[] oldTiles = tilesToSwitch();
    if (oldTiles.length == 0) {
      showServerMessage("Please chose Tiles!", 2);
    } else {
      try {
        this.match.shuffleTiles(oldTiles);
        GameInformation.getInstance().getClientmatch().sendPlacedTilesToServer();
      } catch (IOException ioe) {
        showServerMessage("Could not perform action, try again", 2);
      }
    }
  }

  /**
   * Internal function that is called at the end of the turn if the player has placed any tiles and
   * requires new ones.
   *
   * @param tiles This is called from the shared tile bag from client match.
   */
  private void newTilesFromBag(Tile[] tiles) {
    resetColor();
    int i = 0;
    for (GraphicTile gt : gtiles) {
      if (!gt.isVisiblee() || gt.toDraw()) {
        gt.getLetter().setText(String.valueOf(tiles[i].getLetter()));
        gt.setTile(tiles[i]);
        gt.setVisiblee(true);
        i++;
      }
    }
    deactivateServerMessage();
  }

  /**
   * Shows a server message onto the screen.
   *
   * @param textString The message.
   */
  public void activateServerMessage(String textString) {
    serverMessage.setVisible(true);
    serverMessage.setText(textString);
  }

  /** Hides the server message from the screen. */
  public void deactivateServerMessage() {
    new FadeOut(serverMessage).play();
    serverMessage.setMouseTransparent(true);
  }

  /**
   * Shows a server message on the screen for a certain duration.
   *
   * @param mess Message to be shown.
   * @param duration Duration how long the message should be visible (in seconds).
   */
  public void showServerMessage(String mess, int duration) {
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                for (int i = 0; i < 8; i++) {
                  try {
                    Thread.sleep(100);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  final int f = i;

                  Platform.runLater(
                      new Runnable() {
                        @Override
                        public void run() {

                          if (f == 0) {
                            serverMessage.setVisible(true);
                            serverMessage.setText(mess);
                            new FadeIn(serverMessage).play();
                          } else if (f == duration) {
                            new FadeOut(serverMessage).play();
                            serverMessage.setMouseTransparent(true);
                          }
                        }
                      });
                }
              }
            })
        .start();
  }

  /**
   * Function that is called when the player has a tile selected to be placed and they click on the
   * board. It places the highlighted tile onto the board.
   *
   * @param e Click onto the scrabble board.
   */
  public void setTileOnBoard(MouseEvent e) {
    for (int i = 0; i < 17; i++) {
      for (int j = 0; j < 16; j++) {
        Bounds b = board.getCellBounds(i, j);
        if (i == 0 || j == 0 || i == 16) {
          // do nothing
        } else {
          if (b.contains(e.getX(), e.getY())) {
            if (!tileBagIcon.isMouseTransparent()) {
              new FadeOut(tileBagIcon).play();
              tileBagIcon.setMouseTransparent(true);
            }
            for (int k = 0; k < 7; k++) {
              if (gtiles[k].isHighlighted()) {
                if (tilesOnBoard[i][j]) {
                  break;
                }

                Rectangle rec =
                    new Rectangle(
                        gtiles[k].getRec().getX(),
                        gtiles[k].getRec().getY(),
                        gtiles[k].getRec().getWidth(),
                        gtiles[k].getRec().getHeight());
                rec.setFill(Color.web("#ffe5b4"));
                Text let =
                    new Text(gtiles[k].getX(), gtiles[k].getY(), gtiles[k].getLetter().getText());
                let.setFont(new Font("Times New Roman Bold", 20));
                let.setText("  " + let.getText());
                rec.setId("tile" + turn);
                Rectangle help = new Rectangle();
                help.setX(i);
                help.setY(j);
                rec.setUserData(help);
                let.setId("tile" + turn);
                board.add(rec, i, j);
                board.add(let, i, j);
                new ZoomIn(rec).play();
                new ZoomIn(let).play();
                gtiles[k].highlight(false);
                gtiles[k].setToDraw(false);
                gtiles[k].setVisiblee(false);
                Tile newTile = new Tile(gtiles[k].getLetter().getText().charAt(0), 0);
                newTile.setXY(i, j);
                int ite = 0;
                placeTilesList.add(newTile);
                tilesOnBoard[i][j] = true;

                GameInformation.getInstance()
                    .getClientmatch()
                    .getScrabbleBoard()
                    .placeTile(gtiles[k].getTile(), newTile.getX(), newTile.getY());
                resetColor();
              }
            }
          }
        }
      }
    }
  }

  /**
   * Function that essentially only fires resetTilesAction by the player.
   *
   * @param e Click onto the set back button.
   */
  public void resetTiles(ActionEvent e) {
    new Bounce(resetTilesButton).play();
    resetTilesAction();
  }

  /** This function sets all the tiles that were placed during the current turn back on the rack. */
  public void resetTilesAction() {
    this.match.getScrabbleBoard().nextTurn();
    if (tileBagIcon.isMouseTransparent()) {
      new FadeIn(tileBagIcon).play();
      tileBagIcon.setMouseTransparent(false);
    }
    ObservableList<Node> boardChildren = board.getChildren();
    Node[] nodesToRemove;
    nodesToRemove = new Node[14];
    int i = 0;
    for (Node node : boardChildren) {
      int x = 0;
      int y = 0;
      if (node.getId().equals("tile" + turn)) {
        nodesToRemove[i] = node;
        if (node instanceof Rectangle) {
          Rectangle r = (Rectangle) node;
          Rectangle help = (Rectangle) r.getUserData();
          x = (int) help.getX();
          y = (int) help.getY();
        }
        tilesOnBoard[x][y] = false;
        i++;
      }
    }
    for (GraphicTile gt : gtiles) {
      if (!gt.isVisiblee()) {
        gt.setVisiblee(true);
        new ZoomInDown(gt.getRec()).play();
        new ZoomInDown(gt.getLetter()).play();
      }
    }
    for (Node node : nodesToRemove) {
      board.getChildren().remove(node);
    }
  }

  /**
   * Function that highlights the tile on the rack by clicking on it. This tile will later be the
   * one that gets placed when the player clicks on the scrabble board. If the player hold control
   * down while clicking, the tile will be set to blue and it will be set to draw. If the clicked
   * tile is the joker, an alert will pop up forcing player to choose a letter for the joker tile.
   *
   * @param e click on the tile on the rack.
   */
  public void highlightTile(MouseEvent e) {
    if (Main.profile.getName().equals(match.getCurrentPlayerName()) && !match.isOver()) {
      Rectangle tile = (Rectangle) e.getSource();
      if (e.isControlDown()) {
        tile.setFill(Color.BLUE);
        for (int i = 0; i < 7; i++) {
          if (gtiles[i].getRec().getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
            gtiles[i].setToDraw(true);
          }
        }
      } else {
        for (GraphicTile gt : gtiles) {
          gt.highlight(false);
          gt.setToDraw(false);
        }
        if (tile.getFill() == Color.RED) {
          tile.setFill(Color.web("#ffe5b4"));
        } else {
          resetColor();
          tile.setFill(Color.RED);

          new Pulse(tile).play();
          for (int i = 0; i < 7; i++) {
            if (gtiles[i].getRec().getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
              gtiles[i].highlight(true);
              new Pulse(gtiles[i].getLetter()).play();
              if (gtiles[i].getTile().isJoker()) {
                AlertBox.display("Choose Joker", "Enter the letter the Joker should assume:");
                gtiles[i].setLetter(jokerChar);
                gtiles[i].getTile().setLetter(jokerChar);
                gtiles[i].getTile().setJoker(false);
                gtiles[i].getLetter().setFill(Color.PURPLE);
              }
            }
          }
        }
      }
    }
  }

  private int keyIterator = 0;

  /**
   * The same function as highlightTile, but it happens by pressing the D key. It cycles through the
   * tiles on the rack, highlighting every one.
   *
   * @param e pressing the D key.
   */
  public void highlightTileKey(KeyEvent e) {
    if (e.getCode().equals(KeyCode.D)) {
      boolean allInvis = true;
      for (int i = 0; i < 7; i++) {
        if (gtiles[i].isVisiblee()) {
          allInvis = false;
        }
      }
      if (allInvis) {
        //do nothing
      } else {
        resetColor();
        for (GraphicTile gt : gtiles) {
          gt.highlight(false);
        }

        while (!checkNextVisible()) {
          keyIterator++;
        }
        gtiles[keyIterator].highlight(true);
        gtiles[keyIterator].getRec().setFill(Color.RED);
        keyIterator++;
        if (keyIterator >= 7) {
          keyIterator = 0;
        }
      }
    }
  }

  /**
   * Function that puts a new log message onto the log panel. In opposite to newHistoryMessage, this
   * one is called when the message comes from another source, and will not send the message to all
   * other clients.
   *
   * @param mess The message that will be shown on the log panel.
   */
  public void newHistoryMessageOther(String mess) {
    boolean full = false;
    for (int i = 0; i <= 10; i++) {
      if (history[i].getText().equals("")) {
        history[i].setText(mess);
        new FadeIn(history[i]).play();
        full = true;
        break;
      }
    }
    if (!full) {
      String help = history[10].getText();
      for (int i = 0; i <= 9; i++) {
        history[i].setText(history[i + 1].getText());
      }
      history[10].setText(mess);
      new FadeIn(history[10]).play();
    }
  }

  /**
   * Function that puts a new history message onto the log panel. It will also send a history
   * message to all the other clients.
   *
   * @param mess The message that will be shown.
   */
  public void newHistoryMessage(String mess) {
    boolean full = false;
    for (int i = 0; i <= 10; i++) {
      if (history[i].getText().equals("")) {
        history[i].setText(mess);
        new FadeIn(history[i]).play();
        try {
          GameInformation.getInstance()
              .getClientmatch()
              .getProtocol()
              .sendToServer(new HistoryMessage(Main.profile.getName(), mess));
        } catch (IOException ie) {
          ie.printStackTrace();
        }
        full = true;
        break;
      }
    }
    if (!full) {
      String help = history[10].getText();
      for (int i = 0; i <= 9; i++) {
        history[i].setText(history[i + 1].getText());
      }
      history[10].setText(mess);
      try {
        GameInformation.getInstance()
            .getClientmatch()
            .getProtocol()
            .sendToServer(new HistoryMessage(Main.profile.getName(), mess));
      } catch (IOException ie) {
        ie.printStackTrace();
      }
      new FadeIn(history[10]).play();
    }
  }

  /**
   * Internal function to iterate between tiles for the highlightTileKey function.
   *
   * @return boolean to see if the next tile is visible
   */
  private boolean checkNextVisible() {
    if (keyIterator >= 7) {
      keyIterator = 0;
    }
    return gtiles[keyIterator].isVisiblee();
  }

  /**
   * Opens the chat.
   *
   * @param e Click on the open chat button
   */
  public void openChat(ActionEvent e) {
    new Bounce(openChatButton).play();
    GameInformation.getInstance().getChat().close();
    GameInformation.getInstance().getChat().display();
  }

  /**
   * internal function that places an array of tiles onto the baord.
   *
   * @param tiles The tile array that needs to be placed.
   */
  public void placeTiles(Tile[] tiles) {
    for (Tile t : tiles) {
      Rectangle rec = new Rectangle(36, 36, Color.web("ffe5b4"));
      rec.setId("tilesfromothers");
      Text tileChar = new Text(" " + String.valueOf(t.getLetter()).toUpperCase());
      tileChar.setId("tilesfromothers");
      tileChar.setFont(new Font("Times New Roman Bold", 20));
      tilesOnBoard[t.getX()][t.getY()] = true;
      board.add(rec, t.getX(), t.getY());
      board.add(tileChar, t.getX(), t.getY());
    }
  }
  /**
   * Function that is called only once, but starts the thread that will run throughout the whole
   * game. It sets up most nodes, starts the important thread that updates the whole screen.
   *
   * @param e entering the mouse on the screen.
   */

  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      this.match.setGameScreenController(this);
      serverMessage.setVisible(false);
      Thread taskThread =
          new Thread(
              new Runnable() {
                @Override
                public void run() {
                  while (!GameInformation.getInstance().getClientmatch().isOver()) {
                    try {
                      Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                      ie.printStackTrace();
                    }
                    Platform.runLater(
                        new Runnable() {
                          @Override
                          public void run() {
                            if (Main.profile.getName().equals(match.getCurrentPlayerName())) {
                              currPlayerText.setText("Your Turn");
                              currPlayerText.setFont(Font.font(35));
                            } else {
                              currPlayerText.setText(
                                  GameInformation.getInstance()
                                          .getClientmatch()
                                          .getCurrentPlayerName()
                                          .substring(0, 1)
                                          .toUpperCase()
                                      + GameInformation.getInstance()
                                          .getClientmatch()
                                          .getCurrentPlayerName()
                                          .substring(1)
                                          .toLowerCase());
                            }
                            nameScore1.setText(
                                (match.getPlayers()[0] != null)
                                    ? "" + match.getPlayers()[0].getScore()
                                    : "");
                            nameScore2.setText(
                                (match.getPlayers()[1] != null)
                                    ? "" + match.getPlayers()[1].getScore()
                                    : "");
                            nameScore3.setText(
                                (match.getPlayers()[2] != null)
                                    ? "" + match.getPlayers()[2].getScore()
                                    : "");
                            nameScore4.setText(
                                (match.getPlayers()[3] != null)
                                    ? "" + match.getPlayers()[3].getScore()
                                    : "");
                            if (GameInformation.getInstance().getClientmatch().hasNewTiles()) {
                              Tile[] newTiles =
                                  GameInformation.getInstance()
                                      .getClientmatch()
                                      .getNewTilesToBeAdded();
                              placeTiles(newTiles);
                              match.dropNewTiles();
                            }
                            if (GameInformation.getInstance()
                                .getClientmatch()
                                .getProtocol()
                                .messageChanged()) {
                              newHistoryMessageOther(
                                  GameInformation.getInstance()
                                      .getClientmatch()
                                      .getProtocol()
                                      .getHistoryMessage());
                              GameInformation.getInstance()
                                  .getClientmatch()
                                  .getProtocol()
                                  .messageRead();
                            }
                            if (match.getTextMessages() != null
                                && match.getTextMessages().size() > 0) {
                              writeTextMessages();
                            }
                            if (!match.checkTimer()
                                && Main.profile.getName().equals(match.getCurrentPlayerName())) {
                               endTurnFunction();
                            }
                            if (match.dropTiles()) {
                              resetTilesAction();
                              match.setDropTiles(false);
                            }
                            if (!match.isInvalidMove()) {
                              drawTiles();
                              turn++;
                              match.setInvalidMove(true);
                            }
                            if (match.getNewTilesOnRack() != null) {
                              newTilesFromBag(match.getNewTilesOnRack());
                              match.clearNewTilesOnRack();
                            }
                            if (match.isOver()) {
                              sendLostBox();
                              // sendWinBox();
                            }
                            if (GameInformation.getInstance()
                                .getClientmatch()
                                .isStartingTiles()) {
                              int d = 0;
                              for (Tile t : match.getProtocol().getStartingRack()) {
                                gtiles[d].setTile(t);
                                gtiles[d].setLetter(t.getLetter());
                                gtiles[d].getLetter().setFont(new Font(20));
                                gtiles[d].setXy(402 + (d * 36), 644);
                                gtiles[d].getLetter().setVisible(true);
                                d++;
                              }
                              match.setStartingTiles(false);
                            }

                            time.setText(String.valueOf(match.getTimer().getTimerCurrentPlayerString()));
                          }
                        });
                  }
                }
              });
      taskThread.start();
      GameInformation.getInstance().setGsc(this);
      history[0] = history1;
      history[1] = history2;
      history[2] = history3;
      history[3] = history4;
      history[4] = history5;
      history[5] = history6;
      history[6] = history7;
      history[7] = history8;
      history[8] = history9;
      history[9] = history10;
      history[10] = history11;

      gtiles[0] = new GraphicTile(tile1, text1);
      gtiles[1] = new GraphicTile(tile2, text2);
      gtiles[2] = new GraphicTile(tile3, text3);
      gtiles[3] = new GraphicTile(tile4, text4);
      gtiles[4] = new GraphicTile(tile5, text5);
      gtiles[5] = new GraphicTile(tile6, text6);
      gtiles[6] = new GraphicTile(tile7, text7);
      for (int i = 0; i < 17; i++) {
        for (int j = 0; j < 16; j++) {
          tilesOnBoard[i][j] = false;
        }
      }
      try {
        GameInformation.getInstance()
            .getClientmatch()
            .getProtocol()
            .sendToServer(new SendStartRackMessage(Main.profile.getName(), new Tile[7]));
      } catch (IOException ie) {
        ie.printStackTrace();
      }
      Player[] players = GameInformation.getInstance().getPlayers();
      if (players[0] != null) {
        name1.setText(
            players[0].getName().substring(0, 1).toUpperCase()
                + players[0].getName().substring(1).toLowerCase()
                + ":");
      }
      if (players[1] != null) {
        name2.setText(
            players[1].getName().substring(0, 1).toUpperCase()
                + players[1].getName().substring(1).toLowerCase()
                + ":");
      }

      if (players[2] != null) {
        name3.setText(
            players[2].getName().substring(0, 1).toUpperCase()
                + players[2].getName().substring(1).toLowerCase()
                + ":");
      }
      if (players[3] != null) {
        name4.setText(
            players[3].getName().substring(0, 1).toUpperCase()
                + players[3].getName().substring(1).toLowerCase()
                + ":");
      }
      setUpDone = true;
    }
  }

  /** Internal function to reset the color of the tiles on the rack. */
  private void resetColor() {
    GraphicTile gtile1 = new GraphicTile(tile1, text1);
    GraphicTile gtile2 = new GraphicTile(tile2, text2);
    GraphicTile gtile3 = new GraphicTile(tile3, text3);
    GraphicTile gtile4 = new GraphicTile(tile4, text4);
    GraphicTile gtile5 = new GraphicTile(tile5, text5);
    GraphicTile gtile6 = new GraphicTile(tile6, text6);
    GraphicTile gtile7 = new GraphicTile(tile7, text7);
    GraphicTile[] gtiles = {gtile1, gtile2, gtile3, gtile4, gtile5, gtile6, gtile7};
    for (int i = 0; i < 7; i++) {
      gtiles[i].getRec().setFill(Color.web("#ffe5b4"));
    }
  }

  /**
   * AlertBox is a slightly change version of Alert. It is a pop up window that can be called using
   * the display function. When called by the joker tile it will record the key pressed by the
   * player.
   */
  public static class AlertBox {

    /**
     * Used to display the AlertBox.
     *
     * @param title The tile of the window.
     * @param message The message that will be shown in the scene.
     */
    public static void display(String title, String message) {
      Stage window = new Stage();
      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle(title);
      window.setMinWidth(250);
      window.setMinHeight(250);
      Label label = new Label(message);
      Button button = new Button("CLOSE");
      button.setOnAction(new EventHandler<ActionEvent>() {
                           @Override
                           public void handle(ActionEvent actionEvent) {
                             if (GameInformation.getInstance().getClientmatch().isOver()){
                               try{
                                 Main m = new Main();
                                 m.changeScene("screens/mainMenu.fxml");
                               }catch(IOException e){
                                 e.printStackTrace();
                               }
                             }else{
                               window.close();
                             }
                           }
                         });
          VBox layout = new VBox(10);
      layout.getChildren().addAll(label, button);
      layout.setAlignment(Pos.CENTER);
      Scene scene = new Scene(layout);
      scene.addEventFilter(
          KeyEvent.KEY_PRESSED,
          new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
              label.setText(keyEvent.getText().toUpperCase());
              jokerChar = (keyEvent.getText()).toUpperCase().charAt(0);
              window.close();
            }
          });
      window.setScene(scene);
      window.showAndWait();
    }
  }
}
