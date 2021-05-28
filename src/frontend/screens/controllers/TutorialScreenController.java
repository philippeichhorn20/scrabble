package frontend.screens.controllers;

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
import backend.basic.Player.Playerstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.ServerMatch;
import backend.basic.Tile;
import backend.basic.TileBag;
import backend.tutorial.TutorialInformation;
import frontend.Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

/*
   @author nilschae
*/
public class TutorialScreenController extends Thread{
  @FXML private Rectangle tile1;
  @FXML private Rectangle tile2;
  @FXML private Rectangle tile3;
  @FXML private Rectangle tile4;
  @FXML private Rectangle tile5;
  @FXML private Rectangle tile6;
  @FXML private Rectangle tile7;
  @FXML private Button backButton;
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
  @FXML private GridPane board;
  @FXML private Button boun;
  @FXML private AnchorPane scoreboard;
  @FXML private Label currPlayer;
  @FXML private Label time;
  @FXML private AnchorPane pane;
  @FXML private ImageView tileBagIcon;
  @FXML private Button resetTilesButton;
  @FXML private Label currPlayerText;
  Player currentPlayer = new Player(Main.profile.getName(),"Red", Playerstatus.WAIT);
  private TutorialInformation tutorialInformation = TutorialInformation.getInstance();
  private ScrabbleBoard scrabbleBoard = new ScrabbleBoard();
  private Tile[] placedTiles;
  private ArrayList<Tile> placeTilesList = new ArrayList<Tile>();
  private static char jokerChar;
  private boolean[][] tilesOnBoard = new boolean[17][16];
  private Tile[] turnTiles = new Tile[5];
  private int setTiles = 0;



  private boolean setUpDone = false;
  private int totalNumberOfTiles = 0;
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
  private Label[] history = new Label[11];// = {history1,history2,history3,history4,history5,history6,history7,history8,history9,history10,history11};
  private GraphicTile gtile1;
  private GraphicTile gtile2;
  private GraphicTile gtile3;
  private GraphicTile gtile4;
  private GraphicTile gtile5;
  private GraphicTile gtile6;
  private GraphicTile gtile7;
  private GraphicTile[] gtiles = new GraphicTile[7];
  private int turn = 0;

  public void goBack() throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }

  public void goSettings(MouseEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/settingsScreen.fxml");
  }

  public void getBounds(ActionEvent e) {
    Tile tile = new Tile('c', 5);
    setTile(tile, 5, 7);
  }
  int timInt = 0;
  public void update(MouseEvent e) {
    // Player[] players = match.getPlayers();
    // currPlayer.setText(players[match.getCurrentPlayer()].getName());



  }
  int fdg = 1;
  public void endTurn(ActionEvent e) throws IOException {
    boolean correctMove = true;
    for(int i = 0; i < 5; i++) {
      if(turnTiles[i] != null) {
        if(turnTiles[i].getX() == 6 && turnTiles[i].getY() == 8 && turnTiles[i].getLetter() == 'H') {

        } else if(turnTiles[i].getX() == 7 && turnTiles[i].getY() == 8 && turnTiles[i].getLetter() == 'E') {

        } else if(turnTiles[i].getX() == 8 && turnTiles[i].getY() == 8 && turnTiles[i].getLetter() == 'L') {

        } else if(turnTiles[i].getX() == 9 && turnTiles[i].getY() == 8 && turnTiles[i].getLetter() == 'L') {

        } else if(turnTiles[i].getX() == 10 && turnTiles[i].getY() == 8 && turnTiles[i].getLetter() == 'O') {

        } else {
          correctMove = false;
        }
      } else {
        correctMove = false;
      }
    }

    if(correctMove) {
      TutorialInformation.getInstance().getTutorialMatch().endFlag();
    } else {
      resetTiles(e);
      for(int i = 0; i < 5; i++) {
        turnTiles[i] = null;
      }
    }
  }
  private void endTurnB(){
    turn++;
    drawTiles();
    tileBagIcon.setVisible(true);
    resetTilesButton.setVisible(true);
    GameInformation.getInstance().getClientmatch().sendPlacedTilesToServer();
    new FadeIn(tileBagIcon).play();
  }

  public void sendWinBox() {
    AlertBox.display("You win!", "Congratulations,you have won!");
  }

  public void sendLostBox() {
    AlertBox.display("Game over", "The game is over, you have lost");
  }

  public void setTile(Tile tile, int x, int y) {
    Rectangle newTile = new Rectangle(36, 36, Color.web("#ffe5b4"));
    newTile.setStroke(Color.BLACK);
    newTile.setStyle("-fx-stroke-width: 1");
    Text letter = new Text("  " + String.valueOf(tile.getLetter()));
    letter.setFont(new Font("Times New Roman", 20));
    GraphicTile gt = new GraphicTile(newTile, letter);
    board.add(gt, x, y);
  }

  public void getNewTiles(MouseEvent e) {
    drawTiles();
  }

  private void drawTiles() {
    resetColor();
    for (GraphicTile gt : gtiles) {
      if (!gt.isVisiblee() || gt.toDraw()) {
        // Tile newTile = servMatch.getTileBag().drawTile(); unlock when servermatch is done
        Tile newTile = new TileBag().drawTile();
        Text let = new Text(String.valueOf(newTile.getLetter()));
        let.setLayoutX((gt.getLetter().getLayoutX()));
        let.setLayoutY((gt.getLetter().getLayoutY()));
        let.setFont(gt.getLetter().getFont());
        let.setMouseTransparent(true);
        pane.getChildren().add(let);
        gt.setXY(gt.getRec().getLayoutX(), gt.getRec().getLayoutY());
        pane.getChildren().remove(gt.getLetter());
        gt.setLetter(let);
        gt.setVisiblee(true);
        gt.setTile(newTile);
        new SlideInLeft(gt.getRec()).play();
        new SlideInLeft(gt.getLetter()).play();
        new FadeOut(tileBagIcon).play();
        tileBagIcon.setVisible(false);
        resetTilesButton.setVisible(false);
      }
    }
  }

  public void setTileOnBoard(MouseEvent e) {
    for (int i = 0; i < 17; i++) {
      for (int j = 0; j < 16; j++) {
        Bounds b = board.getCellBounds(i, j);
        if (i == 0 || j == 0 || i == 16 || i == 17 || j == 16){

        }else{
        if (b.contains(e.getX(), e.getY())) {
          new FadeOut(tileBagIcon).play();
          //tileBagIcon.setVisible(false);
          for (int k = 0; k < 7; k++) {
            if (gtiles[k].isHighlighted()) {
              if(tilesOnBoard[i][j]){
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
              let.setId("tile" + turn);
              totalNumberOfTiles++;
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
              if(setTiles < 5) {
                turnTiles[setTiles++] = newTile;
              }
              tilesOnBoard[i][j]=true;

              Tile[] tt = {newTile};

              resetColor();
              }
            }
          }
        }
      }
    }
  }

  public void resetTiles(ActionEvent e) {
    ObservableList<Node> boardChildren = board.getChildren();
    Node[] nodesToRemove;
    nodesToRemove = new Node[14];
    int i = 0;
    tileBagIcon.setVisible(true);
    for (Node node : boardChildren) {
      if (node.getId().equals("tile" + turn)) {
        nodesToRemove[i] = node;
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
    nodesToRemove = null;
  }

  public void highlightTile(MouseEvent e) {
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
              gtiles[i].getTile().setJoker(false);
              gtiles[i].getLetter().setFill(Color.PURPLE);
            }
          }
        }
      }
    }
  }
  private int keyIterator = 0;
  public void highlightTileKey(KeyEvent e){
    if (e.getCode().equals(KeyCode.D)) {
      boolean allInvis = true;
      for (int i = 0; i < 7; i++) {
        if (gtiles[i].isVisiblee()) {
          allInvis = false;
        }
      }
      if (allInvis) {

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
  private void newHistoryMessage(String mess){
    boolean full = false;
    for (int i = 0;i<=10;i++){
     if (history[i].getText().equals("")){
        history[i].setText(mess);
        new FadeIn(history[i]).play();
        full = true;
        break;
      }

    }
    if (!full){
      String help = history[10].getText();
      for (int i = 0;i<=9;i++){
        history[i].setText(history[i+1].getText());
      }
      history[10].setText(mess);
      new FadeIn(history[10]).play();

    }
  }
  private boolean checkNextVisible(){
    if (keyIterator >= 7){
      keyIterator = 0;
    }
    if (gtiles[keyIterator].isVisiblee()){
      return true;
    }else{
      return false;
    }
  }
  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      Thread taskThread = new Thread(new Runnable() {
        @Override
        public void run() {
          int progress = 100;
          while (!TutorialInformation.getInstance().getTutorialMatch().isOver()) {
            try{
              Thread.sleep(1000);
            }catch(InterruptedException ie){
              ie.printStackTrace();
            }
            progress -= 1;
            if (progress <= 0){
              break;
            }
            final double reportedProgress = progress;
            Platform.runLater(new Runnable() {
              @Override
              public void run() {

                if(tutorialInformation.getTutorialMatch().getStartFlag()) {
                  for (int i = 0; i < 7; i++) {
                    gtiles[i].setTile(TutorialInformation.getInstance().getTutorialMatch().tutorialRackTiles[i]);
                    gtiles[i].getLetter().setText(String.valueOf(TutorialInformation.getInstance().getTutorialMatch().tutorialRackTiles[i].getLetter()));
                    gtiles[i].getLetter().setFont(new Font(20));
                    gtiles[i].setXY(402 + (i * 36), 644);
                    gtiles[i].getLetter().setVisible(true);
                  }

                  Alert welcomeAlert = new Alert(AlertType.INFORMATION);

                  welcomeAlert.setTitle(tutorialInformation.getTutorialMatch().welcomeContentTitel);
                  welcomeAlert.setHeaderText(null);
                  welcomeAlert.setContentText(tutorialInformation.getTutorialMatch().welcomeContentText);

                  TutorialInformation.getInstance().getTutorialMatch().setStartFlag(false);

                  tutorialInformation.getTutorialMatch().highligthTiles();

                } else if(tutorialInformation.getTutorialMatch().getHighligthTilesFlag()) {
                  Alert higlitedTilesAlert = new Alert(AlertType.INFORMATION);

                  higlitedTilesAlert.setTitle(tutorialInformation.getTutorialMatch().highlightedTilesTitle);
                  higlitedTilesAlert.setHeaderText(null);
                  higlitedTilesAlert.setContentText(tutorialInformation.getTutorialMatch().higlightedTilesContentText);

                  TutorialInformation.getInstance().getTutorialMatch().setHighligthTilesFlag(false);

                  Optional<ButtonType> result = higlitedTilesAlert.showAndWait();

                  for(int i = 0; i < 5; i++){
                    gtiles[i].getRec().setFill(Color.RED);
                  }

                  tutorialInformation.getTutorialMatch().highlightScrabbleboardPosition();

                  for(int i = 0; i < 5; i++) {
                    Rectangle highlight = new Rectangle(36,36,Color.RED);
                    board.add(highlight,6 + i, 8);
                  }

                } else if(tutorialInformation.getTutorialMatch().getHighlightScrabbleboardPositionFlag()) {
                  Alert highligthScrabbleboardPositionAlert = new Alert(AlertType.INFORMATION);

                  highligthScrabbleboardPositionAlert.setTitle(tutorialInformation.getTutorialMatch().highligthScrabbleboardPositionTitle);
                  highligthScrabbleboardPositionAlert.setHeaderText(null);
                  highligthScrabbleboardPositionAlert.setContentText(tutorialInformation.getTutorialMatch().highligthScrabbleboardPositionContentText);

                  TutorialInformation.getInstance().getTutorialMatch().setHighlightScrabbleboardPositionFlag(false);
                  Optional<ButtonType> result = highligthScrabbleboardPositionAlert.showAndWait();



                } else if(tutorialInformation.getTutorialMatch().getEndFlag()) {
                  try {
                    Alert endGameAlert =  new Alert(AlertType.INFORMATION);

                    endGameAlert.setTitle(TutorialInformation.getInstance().getTutorialMatch().endGameTitle);
                    endGameAlert.setHeaderText(null);
                    endGameAlert.setContentText(TutorialInformation.getInstance().getTutorialMatch().endGameContentText);

                    TutorialInformation.getInstance().getTutorialMatch().setEndFlag(false);

                    Optional<ButtonType> result = endGameAlert.showAndWait();
                    goBack();
                    TutorialInformation.getInstance().getTutorialMatch().setGameOver();

                  } catch (IOException ioException) {
                    ioException.printStackTrace();
                  }
                }
              }
            });
          }
        }
      });
      taskThread.start();
      history[0] = history1;
      history[1] = history2;
      history[2] =  history3;
      history[3] = history4;
      history[4] =  history5;
      history[5] =  history6;
      history[6] = history7;
      history[7] =  history8;
      history[8] =  history9;
      history[9] =  history10;
      history[10] =  history11;


      gtiles[0] = new GraphicTile(tile1, text1);
      gtiles[1] = new GraphicTile(tile2, text2);
      gtiles[2] = new GraphicTile(tile3, text3);
      gtiles[3] = new GraphicTile(tile4, text4);
      gtiles[4] = new GraphicTile(tile5, text5);
      gtiles[5] = new GraphicTile(tile6, text6);
      gtiles[6] = new GraphicTile(tile7, text7);
      for (int i = 0;i<17;i++){
        for (int j = 0;j<16;j++){
          tilesOnBoard[i][j] = false;
        }
      }

      //Player[] players = GameInformation.getInstance().getClientmatch().getPlayers();



        currPlayerText.setText(this.currentPlayer.getName());//players[0].getName().substring(0,1).toUpperCase()+players[0].getName().substring(1).toLowerCase());
        //name1.setText(players[0].getName().substring(0,1).toUpperCase()+players[0].getName().substring(1).toLowerCase() + ":");
        //name2.setText(players[1].getName() + ":");
        //name3.setText(players[2].getName() + ":");
        //name4.setText(players[3].getName() + ":");
        nameScore1.setText("0");
        nameScore2.setText("0");
        nameScore3.setText("0");
        nameScore4.setText("0");

      setUpDone = true;
    }
  }

  public void placeTiles(Tile[] tiles) {
    for (Tile t : tiles) {
      Rectangle rec = new Rectangle(36, 36, Color.web("ffe5b4"));
      rec.setId("tiles");
      Text tileChar = new Text(" " + String.valueOf(t.getLetter()).toUpperCase());
      tileChar.setId("tiles");
      tileChar.setFont(new Font("Times New Roman Bold", 20));

      board.add(rec, t.getX(), t.getY());
      board.add(tileChar, t.getX(), t.getY());
    }
  }
  public void placeTile(Tile tile){
    Rectangle rec = new Rectangle(36, 36, Color.web("ffe5b4"));
    rec.setId("tiles");
    Text tileChar = new Text(" " + String.valueOf(tile.getLetter()).toUpperCase());
    tileChar.setId("tiles");
    tileChar.setFont(new Font("Times New Roman Bold", 20));
    board.add(rec, tile.getX(), tile.getY());
    board.add(tileChar, tile.getX(), tile.getY());
  }

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

  public static class AlertBox {
    public static void display(String title, String message) {
      Stage window = new Stage();
      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle(title);
      window.setMinWidth(250);
      window.setMinHeight(250);
      Label label = new Label(message);
      Button button = new Button("CLOSE");
      button.setOnAction(e -> window.close());
      VBox layout = new VBox(10);
      layout.getChildren().addAll(label, button);
      layout.setAlignment(Pos.CENTER);
      Scene scene = new Scene(layout);
      scene.addEventFilter(
          KeyEvent.KEY_PRESSED,
          new EventHandler<KeyEvent>() {
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
