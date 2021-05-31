package backend.basic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Tile is a tile - it contains the letter, the coordinates on the board, its status and if it is a
 * joker.
 *
 * @author nilschae
 */
public class Tile implements Serializable {

  private char letter; // A holder for a letter of a tile
  private int value; // A holder for the value of a tile
  private Tilestatus status; // A holder for the status of a tile
  private boolean joker; // A holder if the tile is a joker or not
  private int x; // The position of the Tile in the field
  private int y; // The position of the Tile in the field

  /**
   * Constructor.
   *
   * @param letter letter
   * @param value value
   * @param status status
   */
  public Tile(char letter, int value, Tilestatus status) {
    this.letter = letter;
    this.value = value;
    this.status = status;
  }

  /**
   * Constructor with default status inbag.
   *
   * @param letter letter
   * @param value value
   */
  public Tile(char letter, int value) {
    this.letter = letter;
    this.value = value;
    this.status = Tilestatus.INBAG;
  }

  /**
   * Constructor for joker.
   *
   * @param isJoker boolean if tile should be joker
   * @param status tilestatus.
   */
  public Tile(boolean isJoker, Tilestatus status) {
    this.joker = isJoker;
    this.value = 0;
    this.status = status;
  }

  /**
   * Copy contstructor.
   *
   * @param tile tile.
   */
  public Tile(Tile tile) {
    this.letter = tile.letter;
    this.value = tile.value;
    this.status = tile.status;
    this.joker = tile.joker;
  }

  /*@return letter of the tile
   * */
  public char getLetter() {
    return this.letter;
  }

  /*@param letter set the letter of a tile
   * */
  public void setLetter(char letter) {
    this.letter = letter;
  }

  /*@return the value of the letter
   * */
  public int getValue() {
    return this.value;
  }

  /*@param value set the value of a tile
   * */
  public void setValue(int value) {
    this.value = value;
  }

  /*@return the status of a tile
   * */
  public Tilestatus getStatus() {
    return this.status;
  }

  /*@param status set the status of a tile
   * */
  public void setStatus(Tilestatus status) {
    this.status = status;
  }

  /*@return if the tile is a joker*/
  public boolean isJoker() {
    return this.joker;
  }

  /*@param isJoker set if the tile is a Joker or not*/
  public void setJoker(boolean isJoker) {
    this.joker = isJoker;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getYOnBoard() {
    return this.y - 1;
  }

  public int getXOnBoard() {
    return this.x - 1;
  }

  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Compare method.
   *
   * @param tile tile to compare to.
   * @return whether the tiles are the same
   */
  public boolean equals(Tile tile) {
    if (this.joker && tile.joker) {
      return true;
    } else {
      if (this.letter != 0 && tile.letter != 0) {
        return this.letter == tile.letter;
      }
    }
    return false;
  }

  public String toString() {
    return "Tile " + this.letter + " of value " + this.value + " and status " + this.status;
  }

  /**
   * Funnily, does exactly the oppositive - transfers an array list of tiles to an array of tiles.
   *
   * @param tiles arraylist of tiles
   * @return tile array
   */
  public static Tile[] tileArrayToList(ArrayList<Tile> tiles) {
    Tile[] tiles1 = new Tile[tiles.size()];
    for (int x = 0; x < tiles1.length; x++) {
      tiles1[x] = tiles.get(x);
    }
    return tiles1;
  }

  /** Enum for the status of the tile. */
  public enum Tilestatus {
    INBAG,
    ONPLAYERRACK,
    ONBOARD,
  }
}
