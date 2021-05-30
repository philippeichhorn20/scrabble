package backend.basic;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * GraphicTile is a quasi representation of combined Rectangle, Text and Tile Node It is used only
 * in game screen controller.
 *
 * @author mkolinsk
 */
public class GraphicTile extends Rectangle {
  private boolean highlighted = false;
  private boolean highlightedToDraw = false;
  private Rectangle tile;
  private Text letter;
  private Tile theTile;

  /**
   * Standard constructor with a rectangle and text.
   *
   * @param rec Rectangle
   * @param letter Text which should contain only a single letter
   */
  public GraphicTile(Rectangle rec, Text letter) {
    this.tile = rec;
    this.letter = letter;
  }

  /**
   * Getter of highlighted boolean.
   *
   * @return Returns whether the GraphicTile is highlighted
   */

  public boolean isHighlighted() {
    return highlighted;
  }

  /**
   * Setter of highlighted boolean.
   *
   * @param b whether the GraphicTile should be highlighted or not.
   */
  public void highlight(boolean b) {
    highlighted = b;
  }

  /**
   * Getter of toDraw.
   *
   * @return returns if the tile is set to be drawn.
   */

  public boolean toDraw() {
    return highlightedToDraw;
  }

  /**
   * Setter of toDraw.
   *
   * @param b whether the toDraw boolean should be true or false.
   */

  public void setToDraw(boolean b) {
    this.highlightedToDraw = b;
  }

  /**
   * Getter of Text which represents the letter.
   *
   * @return Text
   */
  public Text getLetter() {
    return letter;
  }

  /**
   * Setter of letter Text.
   *
   * @param letter Text
   */
  public void setLetter(Text letter) {
    this.letter = letter;
  }

  /**
   * Shortcut setter which doesn't change the letter but only sets its text.
   *
   * @param c letter to be set
   */
  public void setLetter(char c) {
    letter.setText(String.valueOf(c));
  }

  /**
   * Getter of Rectangle.
   *
   * @return Rectangle
   */

  public Rectangle getRec() {
    return tile;
  }

  /**
   * Setter of Rectangle.
   *
   * @param tile Rectangle
   */

  public void setRec(Rectangle tile) {
    this.tile = tile;
  }

  /**
   * Setter of tile.
   *
   * @param tile Tile
   */
  public void setTile(Tile tile) {
    this.theTile = tile;
  }

  /**
   * Getter of tile.
   *
   * @return Tile.
   */
  public Tile getTile() {
    return theTile;
  }

  /**
   * Sets the coordinates of rectangle and text.
   *
   * @param x x coordinate
   * @param y y coordinate
   */

  public void setXy(double x, double y) {
    tile.setLayoutX(x);
    letter.setLayoutX(x + 13);
    tile.setLayoutY(y);
    letter.setLayoutY(y + 25);
  }

  /**
   * Sets if the rectangle and text should both be visible or not.
   *
   * @param b boolean
   */
  public void setVisiblee(boolean b) {
    tile.setVisible(b);
    letter.setVisible(b);
  }

  /**
   * Gets if the rectangle and text should both be visible or not.
   *
   * @return boolean if visible
   */
  public boolean isVisiblee() {
    return this.letter.isVisible() && this.tile.isVisible();
  }
}
