package backend.ai;

import backend.basic.Matchfield;
import backend.basic.Matchfield.Premiumstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import backend.basic.WordCheckDB;
import java.util.ArrayList;
import java.util.List;
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
  public TreeSet<String> getPlayableWords(Tile[] tilesOnHand,
      ArrayList<WordPossibility> wordPossibilities) {
    TreeSet<String> playableWords = new TreeSet<String>();
    for (int i = 0; i < wordPossibilities.size(); i++) {
      WordPossibility wordPossibility = wordPossibilities.get(i);
      int x = wordPossibility.getxPos();
      int y = wordPossibility.getyPos();
      Matchfield[] neighbors = getNeighbors(scrabbleBoard, x, y);
      playableWords.addAll(findCorrectWords(wordPossibility.getLetter(), tilesOnHand));
    }
    return playableWords;
  }

  /*
  Tries creating words by mixing letter with wordlength amount of tiles from tilesOnHand
  @TODO
   */
  TreeSet<String> findCorrectWords(char givenChar, Tile[] tilesOnHand) {
    TreeSet<String> words = new TreeSet<String>();
    String lettersOnHand = "";
    //get letters from tiles on hand
    for (int i = 0; i < tilesOnHand.length; i++) {
      lettersOnHand += tilesOnHand[i].getLetter();
    }
    //trying all combinations with one tile from hand
    for (int i = 0; i < lettersOnHand.length(); i++) {
      String permute = "" + givenChar + lettersOnHand.charAt(i);
      words.addAll(validPermutations(permute));
    }
    //trying all comb with two tiles
    for (int i = 0; i < lettersOnHand.length(); i++) {
      for (int j = 0; j < lettersOnHand.length(); j++) {
        if (i != j) {
          String permute = "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j);
          words.addAll(validPermutations(permute));
        }
      }
    }
    //with 3 tiles
    for (int i = 0; i < lettersOnHand.length(); i++) {
      for (int j = 0; j < lettersOnHand.length(); j++) {
        for (int k = 0; k < lettersOnHand.length(); k++) {
          if (i != j && j != k && i != k) {
            String permute =
                "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j) + lettersOnHand
                    .charAt(k);
            words.addAll(validPermutations(permute));
          }
        }
      }
    }
    //with 4 tiles
    for (int i = 0; i < lettersOnHand.length(); i++) {
      for (int j = 0; j < lettersOnHand.length(); j++) {
        for (int k = 0; k < lettersOnHand.length(); k++) {
          for (int l = 0; l < lettersOnHand.length(); l++) {
            if (i != j && j != k && i != k && i != l && j != l && k != l) {
              String permute =
                  "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j) + lettersOnHand
                      .charAt(k) + lettersOnHand.charAt(l);
              words.addAll(validPermutations(permute));
            }
          }
        }
      }
    }
    //with 5 tiles
    for (int i = 0; i < lettersOnHand.length(); i++) {
      for (int j = 0; j < lettersOnHand.length(); j++) {
        for (int k = 0; k < lettersOnHand.length(); k++) {
          for (int l = 0; l < lettersOnHand.length(); l++) {
            for (int m = 0; m < lettersOnHand.length(); m++) {
              if (i != j && j != k && i != k && i != l && j != l && k != l && i != m && j != m
                  && k != m && l != m) {
                String permute =
                    "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j)
                        + lettersOnHand
                        .charAt(k) + lettersOnHand.charAt(l) + lettersOnHand.charAt(m);
                words.addAll(validPermutations(permute));
              }
            }
          }
        }
      }
    }

    return words;
  }

  private static void swap(char[] a, int i, int j) {
    char ch = a[i];
    a[i] = a[j];
    a[j] = ch;
  }

  // Iterative function to find permutations of a string in Java
  public static List<String> validPermutations(String s) {
    ArrayList<String> valid = new ArrayList<String>();
    // convert the string to a character array (Since the string is immutable)
    char[] chars = s.toCharArray();

    // Weight index control array
    int[] p = new int[s.length()];

    // `i` and `j` represent the upper and lower bound index, respectively,
    // for swapping
    int i = 1, j = 0;

    // print the given string, as only its permutations will be printed later
    if(WordCheckDB.checkWord(s)){
      valid.add(String.valueOf(chars));
    }

    while (i < s.length()) {
      if (p[i] < i) {
        // if `i` is odd then `j = p[i]`; otherwise, `j = 0`
        j = (i % 2) * p[i];

        // swap(a[j], a[i])
        swap(chars, i, j);

        // print the current permutation
        if(WordCheckDB.checkWord(String.valueOf(chars))) {
          valid.add(String.valueOf(chars));
        }

        p[i]++;     // increase index "weight" for `i` by one
        i = 1;      // reset index `i` to 1
      }
      // otherwise, `p[i] == i`
      else {
        // reset `p[i]` to 0
        p[i] = 0;

        // set new index value for `i` (increase by one)
        i++;
      }
    }
    return valid;
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
          wordPossibilities.add(new WordPossibility(letter, x, y, scrabbleBoard));
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
