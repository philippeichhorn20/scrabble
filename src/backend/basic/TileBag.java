package backend.basic;

import backend.basic.Tile.Tilestatus;
import java.io.Serializable;
import java.util.Random;

/* @author Nils Sch�fer
 * @version 1.0
 * @description A class which represents the tilebag of the game scrabbel with 100 Tiles and the standart letter set for english scrabbel*/
public class TileBag implements Serializable {

  private Tile[] bag = new Tile[100]; // stores the tiles in the tilebag

  /*A constructor which creates a tilebag and directly assigns a standart letter set */
  public TileBag() {
    createStandartSet();
  }

  /*Fill the tilebag with tiles of a standart scrabbel set after creating an object*/
  private void createStandartSet() {
    for (int i = 0; i < this.bag.length; i++) {
      if (i < 9) {
        this.bag[i] = new Tile('A', 1);
      } else if (i < 11) {
        this.bag[i] = new Tile('B', 3);
      } else if (i < 13) {
        this.bag[i] = new Tile('C', 3);
      } else if (i < 17) {
        this.bag[i] = new Tile('D', 2);
      } else if (i < 29) {
        this.bag[i] = new Tile('E', 1);
      } else if (i < 31) {
        this.bag[i] = new Tile('F', 4);
      } else if (i < 34) {
        this.bag[i] = new Tile('G', 2);
      } else if (i < 36) {
        this.bag[i] = new Tile('H', 4);
      } else if (i < 45) {
        this.bag[i] = new Tile('I', 1);
      } else if (i < 46) {
        this.bag[i] = new Tile('J', 8);
      } else if (i < 47) {
        this.bag[i] = new Tile('K', 5);
      } else if (i < 51) {
        this.bag[i] = new Tile('L', 1);
      } else if (i < 53) {
        this.bag[i] = new Tile('M', 3);
      } else if (i < 59) {
        this.bag[i] = new Tile('N', 1);
      } else if (i < 67) {
        this.bag[i] = new Tile('O', 1);
      } else if (i < 69) {
        this.bag[i] = new Tile('P', 3);
      } else if (i < 70) {
        this.bag[i] = new Tile('Q', 10);
      } else if (i < 76) {
        this.bag[i] = new Tile('R', 1);
      } else if (i < 80) {
        this.bag[i] = new Tile('S', 1);
      } else if (i < 86) {
        this.bag[i] = new Tile('T', 1);
      } else if (i < 90) {
        this.bag[i] = new Tile('U', 1);
      } else if (i < 92) {
        this.bag[i] = new Tile('V', 4);
      } else if (i < 94) {
        this.bag[i] = new Tile('W', 4);
      } else if (i < 95) {
        this.bag[i] = new Tile('X', 8);
      } else if (i < 97) {
        this.bag[i] = new Tile('Y', 4);
      } else if (i < 98) {
        this.bag[i] = new Tile('Z', 10);
      } else {
        this.bag[i] = new Tile(true, Tilestatus.INBAG);
      }
    }
  }

  /* A funktion to override the existing letter set in the tilebag
   * @param set to override the current set of letters with */
  public void importBagSet(Tile[] set) {
    this.bag = set;
  }

  /* @return the current letter set of the bag*/
  public Tile[] exportBagSet() {
    return this.bag;
  }

  /*@return the size of the bag */
  public int size() {
    return this.bag.length;
  }

  /*@return if the bag is empty or not*/
  public boolean isEmpty() {
    return (this.bag.length > 0) ? false : true;
  }

  /* A funktion to draw a tile random out of the bag.
   * This returns the tile which got drawn and delete it out of the bag.
   * @return the drawn tile*/
  public Tile drawTile() {
    if(this.bag.length-1>=0){
      Tile[] newTileSet = new Tile[this.bag.length - 1];
      Random randomNumber = new Random();
      int randomPosition = randomNumber.nextInt(bag.length);

      Tile drawnTile = this.bag[randomPosition];

      for (int i = 0;
          i < this.bag.length;
          i++) { // Go through the whole bag and copy it in the new set without the drawn tile
        if (i < randomPosition) {
          newTileSet[i] = this.bag[i];

        } else if (i == randomPosition) {

        } else {
          newTileSet[i - 1] = this.bag[i];
        }
      }

      this.bag = newTileSet; // Override the bag with the new tileset
      System.out.println("Tilebag size: "+ this.size() +": "+drawnTile.getLetter());
      return drawnTile;
    }else{
      System.out.println("NOTHING LEFT IN THE BAG");
      return null;
    }



  }

  /*Add a new tile to the bag and increase the size of the bag by one
   * @param tileToAdd the tile that will be added to the bag*/
  public void insertTileToBag(Tile tileToAdd) {
    Tile[] newTileSet = new Tile[this.bag.length + 1];
    for (int i = 0; i < this.bag.length; i++) {
      newTileSet[i] = this.bag[i];
    }
    tileToAdd.setStatus(Tilestatus.INBAG);
    newTileSet[newTileSet.length - 1] = tileToAdd;

    this.bag = newTileSet;
  }

  @Override
  /*Overrides the toString methode with the Letters in the bag and their matching value*/
  public String toString() {
    String list = "";
    for (Tile tile : this.bag) {
      list +=
          "L: "
              + tile.getLetter()
              + ", V: "
              + tile.getValue()
              + "; "; // L stands for letter, V for value
    }
    return list;
  }
}
