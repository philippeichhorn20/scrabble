package backend.ai;

import backend.basic.Tile;

import java.io.IOException;
import java.util.Random;
import java.util.TreeSet;

/**
 * @author vivanova EasyAI is a concrete representation of PlayerAI
 */
public class EasyAI extends PlayerAI {

  private int random = 1;

  /**
   * Constructor to create insatnce of EasyAI
   *
   * @param name name you want the ai to have
   */
  public EasyAI(String name) {
    super(name);
  }

  /**
   * basic setter
   *
   * @return brain of ai
   */
  public Brain getEasyBrain() {
    return brain;
  }

  /**
   * handleturn overwrites parent method. easyai always tries to use worst word possible which is
   * the word with least amount of points
   */
  public void handleTurn() {
    if (this.random % 4 == 0) {
      try {
        pass();
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.random++;
      if (this.random > 100) {
        this.random = 0;
      }
    } else {
      this.random++;
      if (this.random > 100) {
        this.random = 0;
      }
      tryFindingWord();
    }
  }

  /**
   * Finds worst word to place
   */
  public void tryFindingWord() {
    PossibleWord worstWord = null;
    TreeSet<PossibleWord> possibleWords = brain.getPlayableWords(this.tilesOnHand);
    possibleWords.removeAll(this.getTriedWords());
    if (possibleWords.size() != 0) {
      worstWord = possibleWords.last();
    }
    try {
      if (worstWord != null) {
        this.getTriedWords().add(worstWord);
        Tile[] toPlace = worstWord.getTile().toArray(new Tile[0]);
        Tile[] toPlaceCleaned = removeBaseTile(toPlace, worstWord);
        placeTiles(toPlaceCleaned);
      } else {
        sendShuffleMessage(this.tilesOnHand);
        this.setTiles(new Tile[]{null, null, null, null, null, null, null});
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}