package backend.ai;

import backend.basic.Matchfield;
import backend.basic.Matchfield.Premiumstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import java.util.ArrayList;
import java.util.TreeSet;

/*
@author jawinter
@version 1.2
@description This class is responsible for finding possible placements of tiles to identify possible
combinations for words, which can be laid. The brain needs a scrabbleBoard, which it tries to analyse.
 */
public class Brain {

  private ScrabbleBoard scrabbleBoard; //ScrabbleBoard, which is analysed by Brain

  public Brain(ScrabbleBoard board) {
    this.scrabbleBoard = board;
  }

  /*
  Method should return all possible Words that could be played
  @TODO
   */
  public TreeSet<String> getPlayableWords(Tile[] tilesOnHand){
    TreeSet<String> playableWords = new TreeSet<String>();
    ArrayList<WordPossibility> wordPossibilities = this.getWordPossibilities();
    for(int i = 0;i<wordPossibilities.size();i++) {
      WordPossibility wordPossibility = wordPossibilities.get(i);
    int x = wordPossibility.getxPos();
    int y = wordPossibility.getyPos();
    Matchfield[] neighbors = getNeighbors(scrabbleBoard,x,y);
    for(int j = 1;j<8;j++) {
      findCorrectWords(wordPossibility.getLetter(),j,tilesOnHand);
      }
    }
    return playableWords;
  }

/*
Tries creating words by mixing letter with wordlength amount of tiles from tilesOnHand
@TODO
 */
  public TreeSet<String> findCorrectWords(char givenChar,int wordLength,Tile[] tilesOnHand){
    TreeSet<String> words = new TreeSet<String>();

    return words;
  }

  /*
  Method returns List of slots where player could place a word
  @param board is the current scrabbleBoard
   */
  public ArrayList<WordPossibility> getWordPossibilities() {
    ArrayList<WordPossibility> wordPossibilities = new ArrayList<WordPossibility>();
    for (int x = 0; x < 15; x++) {
      for (int y = 0; y < 15; y++) {
        if (scrabbleBoard.getScrabbleBoard()[x][y].hasTile()) {
          char letter = scrabbleBoard.getScrabbleBoard()[x][y].getTile().getLetter();
          wordPossibilities.add(new WordPossibility(letter,x, y, scrabbleBoard));
        }
      }
    }
    return wordPossibilities;
  }

  /*
  Method is used for a given matchfield and checks, where a matchfield has a neighboring non-empty
  matchfield.
  @param board is the current board played on
  xPos and yPos give access to the matchfield which is checked for neighbors (we assume coord is
  allowed)
  return value neighbors is an array of length 4, the array fields are empty if no tile is on the
  matchfield or filled with the neighboring tile. [top] [right] [bottom] [left] -> if possibleSlot
  has a neighbor above neighbors[0] returns the tile
   */
  public static Matchfield[] getNeighbors(ScrabbleBoard board, int xPos, int yPos) {
    Matchfield[][] boardArray = board.getScrabbleBoard();
    Matchfield[] neighbors = new Matchfield[4];
    Matchfield current = boardArray[xPos][yPos];
    for (int i = 0; i < 4; i++) {
      switch (i) {
        case 0:
          if (yPos - 1 >= 0) {
            if (boardArray[xPos][yPos - 1].hasTile()) {
              neighbors[i] = new Matchfield(xPos, yPos - 1, Premiumstatus.NOPREMIUM);
              neighbors[i].setTile(boardArray[xPos][yPos - 1].getTile());
            }
          }
          break;
        case 1:
          if (xPos + 1 < 15) {
            if (boardArray[xPos + 1][yPos].hasTile()) {
              neighbors[i] = new Matchfield(xPos + 1, yPos, Premiumstatus.NOPREMIUM);
              neighbors[i].setTile(boardArray[xPos + 1][yPos].getTile());
            }
          }
          break;
        case 2:
          if (yPos + 1 < 15) {
            if (boardArray[xPos][yPos + 1].hasTile()) {
              neighbors[i] = new Matchfield(xPos, yPos + 1, Premiumstatus.NOPREMIUM);
              neighbors[i].setTile(boardArray[xPos][yPos + 1].getTile());
            }
          }
          break;
        case 3:
          if (xPos - 1 >= 0) {
            if (boardArray[xPos - 1][yPos].hasTile()) {
              neighbors[i] = new Matchfield(xPos - 1, yPos, Premiumstatus.NOPREMIUM);
              neighbors[i].setTile(boardArray[xPos - 1][yPos].getTile());
            }
          }
          break;
      }
    }
    return neighbors;
  }

  /*
  Method helps recognize whether array is empty or not.
  @param field is the array to be checked if empty
  return value is true, if every element is null
   */
  public static boolean isEmpty(Matchfield[] field) {
    boolean empty = true;
    for (int i = 0; i < field.length; i++) {
      if ((field[i] != null)) {
        empty = false;
      }
    }
    return empty;
  }
}
