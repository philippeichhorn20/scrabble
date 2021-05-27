package backend.basic;

/*
 * @author Nils Schäfer
 * @version 1.0
 * */

import java.io.Serializable;
import java.util.ArrayList;

public class Tile implements Serializable {

  private char letter; // A holder for a letter of a tile
  private int value; // A holder for the value of a tile
  private Tilestatus status; // A holder for the status of a tile
  private boolean joker; // A holder if the tile is a joker or not
  private int x; // The position of the Tile in the field
  private int y; // The position of the Tile in the field

  /*The main constructor of a tile
   * @param letter letter of the tile
   * @param value  value of the tile
   * @param status represents where the tile is
   * */
  public Tile(char letter, int value, Tilestatus status) {
    this.letter = letter;
    this.value = value;
    this.status = status;
  }

  /*A constructor which set the status automatic to INBAG
   * @param letter letter of the tile
   * @param value  value of the tile
   * */
  public Tile(char letter, int value) {
    this.letter = letter;
    this.value = value;
    this.status = Tilestatus.INBAG;
  }

  /*A constructor which creates a joker tile
   * @param isJoker defines if the tile is a Joker what have to be if this constructor get called
   * @param status set the Location where the tile is at the moment
   * */
  public Tile(boolean isJoker, Tilestatus status) {
    this.joker = isJoker;
    this.value = 0;
    this.status = status;
  }

  /*Copy constructor for a tile
   * @param tile the tile to copy
   * */
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

  public void setXY(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /*A methode which compares a tile by it's joker status or letter*/
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
  public String toString(){
    return "Tile " + this.letter + " of value " + this.value + " and status " + this.status;
  }

  public static Tile[] tileArrayToList(ArrayList<Tile> tiles) {
    Tile[] tiles1 = new Tile[tiles.size()];
    for (int x = 0; x < tiles1.length; x++) {
      tiles1[x] = tiles.get(x);
    }
    return tiles1;
  }

  /*Enum to set a status for a tile*/
  public enum Tilestatus {
    INBAG,
    ONPLAYERRACK,
    ONBOARD,
    ONSWITCHPANEL // Status for switching tiles back into the tilebag
  }
}
