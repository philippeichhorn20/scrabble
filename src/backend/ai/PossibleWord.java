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

  public PossibleWord(String s, int p, ArrayList<Tile> tiles) {
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
    String tileText = " Tiles: ";
    for (int i = 0; i < this.tiles.size(); i++) {
      tileText += tiles.get(i).getX();
      tileText += "" + tiles.get(i).getY() + " ";
      tileText += tiles.get(i).getValue() + "; ";
    }
    return "PossibleWord{" +
        "word='" + word + '\'' +
        ", points=" + points + tileText +
        '}';
  }

  @Override
  public int compareTo(PossibleWord otherPossibleWord) {
    if (otherPossibleWord.getPoints() > this.points) {
      return 1;
    } else if (otherPossibleWord.getPoints() == this.points) {
      return 0;
    } else {
      return -1;
    }
  }
}
