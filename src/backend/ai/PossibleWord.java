package backend.ai;

/*
@auhtor jawinter
Class represents a word which can ba put on board. It stores the word and points
 */

import backend.basic.Tile;
import java.util.ArrayList;

public class PossibleWord implements Comparable<PossibleWord> {

  private String word;
  private int points;
  private ArrayList<Tile> tiles = new ArrayList<Tile>();

  public PossibleWord(String s,int p,ArrayList<Tile> tiles){
    this.word = s;
    this.points = p;
    this.tiles = tiles;
  }

  public int getPoints() {
    return points;
  }

  public String getWord() {
    return word;
  }

  @Override
  public String toString() {
    return "PossibleWord{" +
        "word='" + word + '\'' +
        ", points=" + points +
        '}';
  }

  @Override
  public int compareTo(PossibleWord otherPossibleWord) {
    if(otherPossibleWord.getPoints()>this.points) {
      return 1;
    } else if(otherPossibleWord.getPoints()==this.points) {
      return 0;
    } else {
      return -1;
    }
  }
}
