package backend.ai;

import backend.basic.Tile;
import java.io.IOException;

/*
 * @author jawinter
 * @description Class represents AI with hard difficulty
 */
public class HardAI extends PlayerAI {


  public HardAI(String name) {
    super(name);
  }

  //choose best Tiles to place and if not found shuffles tiles
  public void handleTurn() {
    PossibleWord bestWord = brain.getPlayableWords(this.tilesOnHand).first();
    System.out.println(bestWord);
    try {
      if (bestWord != null) {
        Tile[] toPlace = bestWord.getTile().toArray(new Tile[0]);
        removeUsedTilesFromHand(toPlace);
        placeTiles(toPlace);
      } else {
        this.setTiles(new Tile[]{null, null, null, null, null, null, null});
        requestNewTiles();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  //If placetiles or shuffleTiles dont forget to set them null

  /*
  Method removes tiles from hand after being placed on board. Tiles to remove are given in parameter
   */
  public void removeUsedTilesFromHand(Tile[] tilesRemove) {
    for (Tile t : tilesRemove) {
      for (Tile tileOnHand : this.tilesOnHand) {
        if (t.getLetter() == tileOnHand.getLetter()) {
          tileOnHand = null;
        }
      }
    }
  }
}
