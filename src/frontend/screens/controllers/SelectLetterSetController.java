package frontend.screens.controllers;

import backend.basic.Tile;
import frontend.screens.controllertools.LetterSetHolder;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class SelectLetterSetController extends Thread {
  @FXML
  ScrollPane viewSP;

  boolean setUpDone = false;
  GridPane gridPane = new GridPane();
  Label[] lettersLB;
  TextField[] numberOfLettersTF;
  TextField[] valueOfLettersTF;

  /*Set up the view for the player to select the whiched tileset*/
  public void setUp(MouseEvent e) {
    if(!setUpDone) {
      lettersLB = new Label[LetterSetHolder.getInstance().getPossibleLetters().length];
      numberOfLettersTF = new TextField[LetterSetHolder.getInstance().getPossibleLetters().length];
      valueOfLettersTF = new TextField[LetterSetHolder.getInstance().getPossibleLetters().length];

      int countLetter[] = new int[LetterSetHolder.getInstance().getPossibleLetters().length];

      for(int i = 0; i < LetterSetHolder.getInstance().getTileSet().length; i++) {
        countLetter[getPositionOfLetterInSet(LetterSetHolder.getInstance().getTileSet()[i].getLetter())] += 1;
      }

      for (int row = 0; row < LetterSetHolder.getInstance().getPossibleLetters().length; row++) {

        lettersLB[row] = new Label();
        lettersLB[row].setText(String.valueOf(LetterSetHolder.getInstance().getPossibleLetters()[row]));

        numberOfLettersTF[row] = new TextField();
        numberOfLettersTF[row].setText(countLetter[row] + "");

        valueOfLettersTF[row] = new TextField();
        valueOfLettersTF[row].setText(getValueOfLetter(LetterSetHolder.getInstance().getPossibleLetters()[row]) + "");

        gridPane.add(lettersLB[row], row, 0 );
        gridPane.add(numberOfLettersTF[row], row, 1 );
        gridPane.add(valueOfLettersTF[row], row, 2 );

      }
      viewSP.setContent(gridPane);
      setUpDone = true;
    }
  }

  private int getPositionOfLetterInSet(char letter) {
    for(int i = 0; i < LetterSetHolder.getInstance().getPossibleLetters().length; i++) {
      if(LetterSetHolder.getInstance().getPossibleLetters()[i] == letter) {
        return i;
      }
    }

    return -1;
  }

  private int getValueOfLetter(char letter) {
    for(int i = 0; i < LetterSetHolder.getInstance().getTileSet().length; i++) {
      if(LetterSetHolder.getInstance().getTileSet()[i].getLetter() == letter) {
        return LetterSetHolder.getInstance().getTileSet()[i].getValue();
      }
    }

    return -1;
  }

  private int getTotalNumberOfTiles() {
    int count = 0;

    for(int i = 0; i < numberOfLettersTF.length; i++) {
      count += Integer.parseInt(numberOfLettersTF[i].getText());
    }

    return count;
  }

  /*Create a letter set out of the value from the textfields
  * @return the created letterSet*/
  private Tile[] getLetterSet() {
    int setPosition = 0;
    Tile[] newSet = new Tile[getTotalNumberOfTiles()];

    for(int i = 0; i < LetterSetHolder.getInstance().getPossibleLetters().length; i++) {
      for(int j = 0; j < Integer.parseInt(numberOfLettersTF[i].getText()); j++) {
        newSet[setPosition++] =  new Tile(LetterSetHolder.getInstance().getPossibleLetters()[i],Integer.parseInt(valueOfLettersTF[i].getText()));
      }
    }

    return newSet;
  }

  public void save(){
    LetterSetHolder.getInstance().setTileSet(getLetterSet());
  }

  public void goBack() {
    this.interrupt();
  }
}
