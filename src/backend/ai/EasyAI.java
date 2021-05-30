package backend.ai;

import backend.basic.Tile;

import backend.basic.Tile.Tilestatus;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import backend.basic.ScrabbleBoard;
import java.util.TreeSet;

/*
@author vivanova
EasyAI uses Brain to find simple possible moves.
 */

public class EasyAI extends PlayerAI {

  private Random r = new Random(System.currentTimeMillis());
  private int random = 1;

  public EasyAI(String name) {
    super(name);
  }

  public Brain getEasyBrain() {
    return brain;
  }

  public void handleTurn() {
    if(this.random%4==0) {
      System.out.println("ICH WILL NIX LEGEN");
      try {
        pass();
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.random++;
      if(this.random>100) {
        this.random = 0;
      }
    }else {
      this.random++;
      if(this.random>100) {
        this.random = 0;
      }
      tryFindingWord();
    }
  }

  /*
  Tries to put word on board, but if it doesnt work, then don't bother.
   */
  public void tryFindingWord(){
    PossibleWord worstWord = null;
    TreeSet<PossibleWord> possibleWords = brain.getPlayableWords(this.tilesOnHand);
    possibleWords.removeAll(this.getTriedWords());
    if(possibleWords.size()!=0) {
      worstWord = possibleWords.last();
    }
    try {
      if (worstWord != null) {
        this.getTriedWords().add(worstWord);
        Tile[] toPlace = worstWord.getTile().toArray(new Tile[0]);
        Tile[] toPlaceCleaned = removeBaseTile(toPlace,worstWord);
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