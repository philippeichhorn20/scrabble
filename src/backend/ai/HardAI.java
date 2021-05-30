package backend.ai;

import backend.basic.Tile;
import java.io.IOException;
import java.util.TreeSet;

/*
 * @author jawinter
 * @description Class represents AI with hard difficulty
 */

/**
 * @author jawinter EasyAI is a concrete representation of PlayerAI
 */
public class HardAI extends PlayerAI {

  /**
   * Constructor to create instance of HardAI
   *
   * @param name name you want the ai to have
   */
  public HardAI(String name) {
    super(name);
  }

  /**
   * handleturn overwrites parent method. hardai always tries to use the best word possible which is
   * the word with most amount of points
   */
  public void handleTurn() {
    PossibleWord bestWord = null;
    TreeSet<PossibleWord> possibleWords = brain.getPlayableWords(this.tilesOnHand);
    possibleWords.removeAll(this.getTriedWords());
    if (possibleWords.size() != 0) {
      bestWord = possibleWords.first();
    }
    try {
      if (bestWord != null) {
        this.getTriedWords().add(bestWord);
        Tile[] toPlace = bestWord.getTile().toArray(new Tile[0]);
        Tile[] toPlaceCleaned = removeBaseTile(toPlace, bestWord);
        placeTiles(toPlaceCleaned);
      }
      if (possibleWords.size() == 0) {
        sendShuffleMessage(this.tilesOnHand);
        this.setTiles(new Tile[]{null, null, null, null, null, null, null});
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
