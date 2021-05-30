package backend.ai;

import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

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
    PossibleWord bestWord = null;
    TreeSet<PossibleWord> possibleWords = brain.getPlayableWords(this.tilesOnHand);
    possibleWords.removeAll(this.getTriedWords());
    if(possibleWords.size()!=0) {
       bestWord = possibleWords.first();
    }
    try {
      if (bestWord != null) {
        this.getTriedWords().add(bestWord);
        Tile[] toPlace = bestWord.getTile().toArray(new Tile[0]);
        Tile[] toPlaceCleaned = removeBaseTile(toPlace,bestWord);
        placeTiles(toPlaceCleaned);
      }
      if(possibleWords.size()==0){
        sendShuffleMessage(this.tilesOnHand);
        this.setTiles(new Tile[]{null, null, null, null, null, null, null});
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  //If placetiles or shuffleTiles dont forget to set them null

}
