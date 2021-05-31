package backend.basic;

import java.io.Serializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Matchfield represents a field on the scrabble board.
 *
 * @author nilschae
 */
public class Matchfield implements Serializable {

  private Tile tileOnField;
  private Premiumstatus isPremium;
  private Button field;



  /**
   * Constructor.
   *
   * @param posX posX the position on the x axis of the field
   * @param posY posY the position on the y axis of the field
   * @param width the width of the field
   * @param height the height of the field
   * @param premiumstatus  which kind of premiumfield it is
   * @param buttonListener b adds a Listener to the field/button of the field
   */

  public Matchfield(
      double posX,
      double posY,
      double width,
      double height,
      Premiumstatus premiumstatus,
      EventHandler<ActionEvent> buttonListener) {
    this.isPremium = premiumstatus;

    this.field = new Button();
    this.field.setLayoutX(posX);
    this.field.setLayoutY(posY);
    this.field.setPrefSize(width, height);

    this.field.setOnAction(buttonListener);

    this.assambleBackgroundColour();
  }



  /**
   * A copy constructor.
   *
   * @param fieldToCopy copy field
   */

  public Matchfield(Matchfield fieldToCopy) {
    this.tileOnField = fieldToCopy.tileOnField;
    this.isPremium = fieldToCopy.isPremium;
    this.field = fieldToCopy.field;

    this.assambleBackgroundColour();
  }

  /**
   * a matchfield constructor without the UI elements.
   *
   * @param x field on the x axis
   * @param y field on the y axis
   * @param premiumstatus premiustatus
   */
  public Matchfield(int x, int y, Premiumstatus premiumstatus) {
    this.isPremium = premiumstatus;
  }

  /**
   * Set the backgroundcolour of the field based on the premiumstatus.
   *
   */
  private void assambleBackgroundColour() {
    switch (this.isPremium) {
      case NOPREMIUM:
        // this.field.setStyle("-fx-background-color: #ffffff; "); // #ffffff code for white
        break;

      case DOUBLELETTER:
        // this.field.setStyle("-fx-background-color: #85f3ff; "); // #85f3ff code for lightblue
        break;

      case TRIPLELETTER:
        // this.field.setStyle("-fx-background-color: #3e52c7; "); // #3e52c7 code for darkblue
        break;

      case DOUBLEWORD:
        // this.field.setStyle("-fx-background-color: #ff69fa; "); // #ff69fa code for pink
        break;

      case TRIPLEWORD:
        // this.field.setStyle("-fx-background-color: #ff0000; "); // #ff0000 code for red
        break;

      default:
        break;
    }
  }

  public Tile getTile() {
    return this.tileOnField;
  }

  public Premiumstatus getPremiumstatus() {
    return this.isPremium;
  }

  public Button getButtonOfField() {
    return this.field;
  }

  public void setTile(Tile tileToPlaceOnField) {
    this.tileOnField = tileToPlaceOnField;
  }

  public boolean hasTile() {
    return this.tileOnField != null;
  }

  public void setPremiumstatus(Premiumstatus status) {
    this.isPremium = status;
    // this.assambleBackgroundColour();
  }

  public void setButton(Button button) {
    this.field = button;
  }

  public void setActionListener(EventHandler<ActionEvent> buttonListener) {
    this.field.setOnAction(buttonListener);
  }

  /**
   * Enum that sets the status of the matchfield.
   */
  public enum Premiumstatus {
    NOPREMIUM,
    DOUBLELETTER,
    TRIPLELETTER,
    DOUBLEWORD,
    TRIPLEWORD
  }
}
