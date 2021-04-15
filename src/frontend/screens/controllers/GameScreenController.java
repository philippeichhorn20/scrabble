package frontend.screens.controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.ZoomOut;
import frontend.Main;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
  @FXML private Button animateButton;
  @FXML private Rectangle animateRec;
  private boolean setUpDone = false;

  private Hashtable<Rectangle, Text> tiles = new Hashtable<>(7);
  private Hashtable<Text, Rectangle> texts = new Hashtable<>(7);

  public void goBack(ActionEvent e) throws IOException {
    Main m = new Main();
    m.changeScene("screens/startingMenu.fxml");
  }

  public void moveTile(MouseEvent e) throws IOException {
    Node n = (Node) e.getSource();
    new ZoomOut(backButton).play();
    if (e.getSource() instanceof Rectangle) {
      Rectangle rec = (Rectangle) e.getSource();
      rec.setX(e.getX());
      rec.setY(e.getY());
      tiles.get(rec).setX(e.getX() + 1);
      tiles.get(rec).setY(e.getY() + 7);
    } else if (e.getSource() instanceof Text) {
      Text text = (Text) e.getSource();
      texts.get(text).setX(e.getX());
      texts.get(text).setY(e.getY());
      text.setX(e.getX() + 1);
      text.setY(e.getY() + 9);
    }
  }

  public void setUp(MouseEvent e) {
    if (!setUpDone) {
      tiles.put(tile1, text1);
      tiles.put(tile2, text2);
      tiles.put(tile3, text3);
      tiles.put(tile4, text4);
      tiles.put(tile5, text5);
      tiles.put(tile6, text6);
      tiles.put(tile7, text7);
      texts.put(text1, tile1);
      texts.put(text2, tile2);
      texts.put(text3, tile3);
      texts.put(text4, tile4);
      texts.put(text5, tile5);
      texts.put(text6, tile6);
      texts.put(text7, tile7);
      for (Map.Entry<Rectangle, Text> map : tiles.entrySet()) {
        map.getValue().setText("B");
        map.getValue().setFont(new Font(20));
        map.getValue().setLayoutX(map.getKey().getLayoutX() + 13);
        map.getValue().setLayoutY(map.getKey().getLayoutY() + 25);
        map.getValue().setVisible(true);
      }
      setUpDone = true;
    }
  }

  public void animate(ActionEvent e) {
    new FadeIn(animateRec).play();
  }
}
