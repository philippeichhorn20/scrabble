package frontend.screens.controllers;

import frontend.screens.controllertools.LetterSetHolder;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.event.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SelectLetterSetController extends Thread {
  @FXML
  ScrollPane viewSP;

  boolean setUpDone = false;
  GridPane gridPane = new GridPane();
  Label[] lettersLB;
  TextField[] numberOfLetters;
  TextField[] valueOfLetters;

  public void setUp(MouseEvent e) {
    if(!setUpDone) {
      lettersLB = new Label[LetterSetHolder.getInstance().getPossibleLetters().length];
      numberOfLetters = new TextField[LetterSetHolder.getInstance().getPossibleLetters().length];
      valueOfLetters = new TextField[LetterSetHolder.getInstance().getPossibleLetters().length];

      /* List<Item> toAdd = new ArrayList<>();
    for (int column = 0; column < columnCount; column++) {
        for (int row = 0; row < rowCount; row++) {
            Item item = getItemAt(column, row);

            // if it is null it wont be painted e.g. empty/blank item
            if (item != null) {
                toAdd.add(item);
                GridPane.setColumnIndex(item, item.getColumn());
                GridPane.setRowIndex(item, item.getRow());
            }
        }
    }

    // add all at once for better performance
    grid.getChildren().setAll(toAdd);
}*/

      setUpDone = true;
    }
  }

  public void save(){

  }

  public void goBack() {

  }
}
