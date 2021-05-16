package frontend.screens.controllers;

import backend.basic.ClientMatch;
import backend.basic.GraphicTile;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.TileBag;
import frontend.Main;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
   TODO:
   This is an empty controller of the game screen. This is the main screen the user sees when they're playing the scrabble game.
   This should contain the board, tiles, chat, points and much more.
*/
public class GameScreenController {
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
  private ScrabbleBoard scrabbleBoard;
  ClientMatch match;

  {
    assert match != null;
    //scrabbleBoard = match.getScrabbleBoard();
  }
  private boolean setUpDone = false;



  private GraphicTile gtile1;
  private GraphicTile gtile2;
  private  GraphicTile gtile3;
  private GraphicTile gtile4;
  private GraphicTile gtile5;
  private GraphicTile gtile6;
  private GraphicTile gtile7;
  private GraphicTile[] gtiles = new GraphicTile[7];

  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/mainMenu.fxml");
  }

  public void goSettings(MouseEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/settingsScreen.fxml");
  }

  public void getBounds(ActionEvent e) {
    Tile tile = new Tile('c',5);
    setTile(tile,5,7);
  }
  public void update(MouseEvent e){
    //Player[] players = match.getPlayers();
    // currPlayer.setText(players[match.getCurrentPlayer()].getName());
    //time.setText(String.valueOf(players[match.getCurrentPlayer()].getTimer().getTimerCurrentPlayer()));

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
    GraphicTile gt = new GraphicTile(newTile,letter);
    board.add(gt,x,y);
  }

  public void getNewTiles(MouseEvent e){
    for (GraphicTile gt:gtiles){
      if (!gt.isVisiblee()){
        TileBag tb = new TileBag();
        Tile newTile = tb.drawTile();
        Text let = new Text(String.valueOf(newTile.getLetter()));
        let.setLayoutX((gt.getLetter().getLayoutX()));
        let.setLayoutY((gt.getLetter().getLayoutY()));
        let.setFont(gt.getLetter().getFont());
        pane.getChildren().add(let);
        gt.setXY(gt.getRec().getLayoutX(),gt.getRec().getLayoutY());
        gt.setLetter(let);
        gt.setVisiblee(true);
      }
    }
  }
  public void highlight(MouseEvent e) {
    for (int i = 0; i < 17; i++) {
      for (int j = 0; j < 16; j++) {
        Bounds b = board.getCellBounds(i,j);
        if (b.contains(e.getX(), e.getY())) {
          for (int k = 0; k < 7;k++){
            if (gtiles[k].isHighlighted()){
              Rectangle rec = new Rectangle(gtiles[k].getRec().getX(),gtiles[k].getRec().getY(),gtiles[k].getRec().getWidth(),gtiles[k].getRec().getHeight());
              rec.setFill(Color.web("#ffe5b4"));
              Text let = new Text(gtiles[k].getX(),gtiles[k].getY(),gtiles[k].getLetter().getText());
              let.setFont(new Font("Times New Roman Bold",20));
              let.setText("  " + let.getText());
              board.add(rec,i,j);
              board.add(let,i,j);
              let.setX((let.getX()+5));
              gtiles[k].highlight(false);
              gtiles[k].setVisiblee(false);
              resetColor();
            }
          }

        }
      }
    }
  }
  public void highlightTile(MouseEvent e) {
    Rectangle tile = (Rectangle) e.getSource();
    if (tile.getFill() == Color.RED){
      tile.setFill(Color.web("#ffe5b4"));
    }else{
      resetColor();
      tile.setFill(Color.RED);
      for (int i = 0;i<7;i++){
        if (gtiles[i].getRec().getBoundsInParent().contains(e.getSceneX(),e.getSceneY())){
          gtiles[i].highlight(true);
        }
      }
    }
  }

  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      gtiles[0] = new GraphicTile(tile1,text1);
      gtiles[1] = new GraphicTile(tile2,text2);
      gtiles[2] = new GraphicTile(tile3,text3);
      gtiles[3] = new GraphicTile(tile4,text4);
      gtiles[4] = new GraphicTile(tile5,text5);
      gtiles[5] = new GraphicTile(tile6,text6);
      gtiles[6] = new GraphicTile(tile7,text7);

      for (int i = 0;i<7;i++){
        gtiles[i].getLetter().setText("A");
        gtiles[i].getLetter().setFont(new Font(20));
        gtiles[i].setXY(402 + (i * 36),644);
        gtiles[i].getLetter().setVisible(true);
      }
        /*unlock when it works
        Player[] players = sMatch.getPlayers();

          name1.setText(players[0].getName() + ":");
          name2.setText(players[1].getName() + ":");
          name3.setText(players[2].getName() + ":");
          name4.setText(players[3].getName() + ":");
          nameScore1.setText("0");
          nameScore2.setText("0");
          nameScore3.setText("0");
          nameScore4.setText("0");*/

      setUpDone = true;
    }
  }


  private void resetColor(){
    GraphicTile gtile1 = new GraphicTile(tile1,text1);
    GraphicTile gtile2 = new GraphicTile(tile2,text2);
    GraphicTile gtile3 = new GraphicTile(tile3,text3);
    GraphicTile gtile4 = new GraphicTile(tile4,text4);
    GraphicTile gtile5 = new GraphicTile(tile5,text5);
    GraphicTile gtile6 = new GraphicTile(tile6,text6);
    GraphicTile gtile7 = new GraphicTile(tile7,text7);
    GraphicTile[] gtiles = {gtile1 , gtile2 , gtile3 , gtile4 , gtile5 , gtile6 , gtile7};
    for (int i = 0;i<7;i++){
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
      window.setScene(scene);
      window.showAndWait();
    }
  }
}
