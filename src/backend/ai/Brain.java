package backend.ai;

import backend.basic.Matchfield;
import backend.basic.Matchfield.Premiumstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import backend.basic.Timer;
import backend.basic.WordCheckDB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/*
@author jawinter
@version 1.2
@description This class is responsible for finding possible placements of tiles to identify possible
combinations for words, which can be laid. The brain needs a scrabbleBoard, which it tries to analyse.
 */
public class Brain implements Serializable {

  private ScrabbleBoard scrabbleBoard; //ScrabbleBoard, which is analysed by Brain
  private HashSet<String> words = new HashSet<String>(); //set of words from dictionary

  public Brain(ScrabbleBoard board) {
    this.scrabbleBoard = board;
    if (WordCheckDB.newUrl == null) {
      readDictionary(WordCheckDB.urlTxt);
    } else {
      readDictionary(WordCheckDB.newUrl);
    }
  }

  /*
  Method to update board brain uses to play
   */
  public void updateScrabbleboard(Tile[] tiles) {
    for (int i = 0; i < tiles.length; i++) {
      scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
    }
  }

  public void setScrabbleboard(ScrabbleBoard board) {
    this.scrabbleBoard = board;
  }

  /*
  Method should return all possible Words that could be played
   */
  public TreeSet<PossibleWord> getPlayableWords(Tile[] tilesOnHand) {
    ArrayList<WordPossibility> wordPossibilities = getWordPossibilities();
    TreeSet<PossibleWord> playableWords = new TreeSet<PossibleWord>();
    if (wordPossibilities.size() == 0) {
      playableWords = getPlayableWordsFirstMove(tilesOnHand);
    }
    Timer timer = new Timer();
    //for each possible Slot to append tiles to find suitable words
    for (int i = 0; i < wordPossibilities.size(); i++) {
      WordPossibility wordPossibility = wordPossibilities.get(i);
      TreeSet<String> allWords = findCorrectWords(wordPossibility, tilesOnHand);
      Iterator<String> it = allWords.iterator();

      while (it.hasNext()) {
        String currentWord = it.next();
        playableWords.addAll(checkIfSpaceSufficient(currentWord, wordPossibility, tilesOnHand));
        if (timer.getTimerOverall() > 20000) {
          return playableWords;
        }
      }
    }
    return playableWords;
  }

  public TreeSet<PossibleWord> checkIfSpaceSufficient(String currentWord,
      WordPossibility wordPossibility, Tile[] tilesOnHand) {
    TreeSet<PossibleWord> list = new TreeSet<PossibleWord>();
    int positionBaseLetter = getPositionBaseLetter(currentWord, wordPossibility.getLetter());
    int verticalPoints = 0;
    int horizontalPoints = 0;
    int xPosBaseLetter = wordPossibility.getxPos();
    int yPosBaseLetter = wordPossibility.getyPos();
    int verticalSpace = wordPossibility.getAboveSpace() + wordPossibility.getBelowSpace() - 2;
    int horizontalSpace = wordPossibility.getLeftSpace() + wordPossibility.getRightSpace() - 2;
    //vertical
    if (currentWord.length() <= verticalSpace) {
      int aboveSpaceNeeded = positionBaseLetter + 1;
      int belowSpaceNeeded = currentWord.length() - positionBaseLetter;
      if (aboveSpaceNeeded <= wordPossibility.getAboveSpace()
          && belowSpaceNeeded <= wordPossibility
          .getBelowSpace()) {
        ArrayList<Tile> calculatePoints = new ArrayList<Tile>();
        for (int j = 0; j < currentWord.length(); j++) {
          Tile newTile = new Tile(currentWord.charAt(j),
              getPointsOfLetter(currentWord.charAt(j), tilesOnHand, wordPossibility),
              Tilestatus.ONPLAYERRACK);
          newTile.setXY(xPosBaseLetter, yPosBaseLetter - positionBaseLetter + j);
          calculatePoints.add(newTile);
        }
        verticalPoints = this.scrabbleBoard.getPointsOfWord(calculatePoints);
        list.add(new PossibleWord(currentWord, verticalPoints, calculatePoints));
      }
    }
    //horizontal
    if (currentWord.length() <= horizontalSpace) {
      int leftSpaceNeeded = positionBaseLetter + 1;
      int rightSpaceNeeded = currentWord.length() - positionBaseLetter + 1;
      if (leftSpaceNeeded <= wordPossibility.getLeftSpace()
          && rightSpaceNeeded <= wordPossibility
          .getRightSpace()) {
        ArrayList<Tile> calculatePoints = new ArrayList<Tile>();
        for (int j = 0; j < currentWord.length(); j++) {
          Tile newTile = new Tile(currentWord.charAt(j),
              getPointsOfLetter(currentWord.charAt(j), tilesOnHand, wordPossibility),
              Tilestatus.ONPLAYERRACK);
          newTile.setXY(xPosBaseLetter - positionBaseLetter + j, yPosBaseLetter);
          calculatePoints.add(newTile);
        }
        horizontalPoints = this.scrabbleBoard.getPointsOfWord(calculatePoints);
        list.add(new PossibleWord(currentWord, horizontalPoints, calculatePoints));
      }
    }
    return list;
  }

  /*
  This method is executed, when the ScrabbleBoard is empty
   */
  public TreeSet<PossibleWord> getPlayableWordsFirstMove(Tile[] tilesOnHand) {
    TreeSet<PossibleWord> playableWords = new TreeSet<PossibleWord>();
    TreeSet<String> allWords = new TreeSet<String>();
    for (int i = 0; i < tilesOnHand.length; i++) {
      Tile[] without = new Tile[6];
      int skip = 0;
      for (int j = 0; j < without.length; j++) {
        if (skip == i) {
          skip++;
        }
        without[j] = tilesOnHand[skip++];
      }
      WordPossibility wordPossibility = new WordPossibility(tilesOnHand[i].getLetter(), 7, 7,
          tilesOnHand[i].getValue(), this.scrabbleBoard);
      allWords = findCorrectWords(wordPossibility, without);
      Iterator<String> it = allWords.iterator();
      while (it.hasNext()) {
        String currentWord = it.next();
        playableWords.addAll(checkIfSpaceSufficient(currentWord, wordPossibility, without));
      }
    }
    return playableWords;
  }

  /*
  Method returns Points of a tile letter
   */
  public int getPointsOfLetter(char c, Tile[] tiles, WordPossibility wordPossibility) {
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i].getLetter() == c) {
        return tiles[i].getValue();
      }
    }
    return wordPossibility.getBaseLetterValue();

  }

  /*
  Of the given word
   */
  public static int getPositionBaseLetter(String word, char baseLetter) {
    int position = 0;
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == baseLetter) {
        position = i;
      }
    }
    return position;
  }

  /*
  Tries creating words by mixing letter with wordlength amount of tiles from tilesOnHand
   */
  TreeSet<String> findCorrectWords(WordPossibility wordPossibility, Tile[] tilesOnHand) {
    TreeSet<String> words = new TreeSet<String>();
    char givenChar = wordPossibility.getLetter();
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
        if (j > i) {
          String permute = "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j);
          words.addAll(validPermutations(permute));
        }
      }
    }
    //with 3 tiles
    for (int i = 0; i < lettersOnHand.length(); i++) {
      for (int j = 0; j < lettersOnHand.length(); j++) {
        for (int k = 0; k < lettersOnHand.length(); k++) {
          if (i < j && j < k) {
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
            if (i < j && j < k && k < l) {
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
              if (i < j && j < k && k < l && l < m) {
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
    //with 6 tiles
    for (int i = 0; i < lettersOnHand.length(); i++) {
      for (int j = 0; j < lettersOnHand.length(); j++) {
        for (int k = 0; k < lettersOnHand.length(); k++) {
          for (int l = 0; l < lettersOnHand.length(); l++) {
            for (int m = 0; m < lettersOnHand.length(); m++) {
              for (int n = 0; n < lettersOnHand.length(); n++) {
                if (i < j && j < k && k < l && l < m && m < n) {
                  String permute =
                      "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j)
                          + lettersOnHand
                          .charAt(k) + lettersOnHand.charAt(l) + lettersOnHand.charAt(m)
                          + lettersOnHand.charAt(n);
                  words.addAll(validPermutations(permute));
                }
              }
            }
          }
        }
      }
    }
    //with 7 tiles
    if (tilesOnHand.length > 6) {
      for (int i = 0; i < lettersOnHand.length(); i++) {
        for (int j = 0; j < lettersOnHand.length(); j++) {
          for (int k = 0; k < lettersOnHand.length(); k++) {
            for (int l = 0; l < lettersOnHand.length(); l++) {
              for (int m = 0; m < lettersOnHand.length(); m++) {
                for (int n = 0; n < lettersOnHand.length(); n++) {
                  for (int o = 0; o < lettersOnHand.length(); o++) {
                    if (i < j && j < k && k < l && l < m && m < n) {
                      String permute =
                          "" + givenChar + lettersOnHand.charAt(i) + lettersOnHand.charAt(j)
                              + lettersOnHand
                              .charAt(k) + lettersOnHand.charAt(l) + lettersOnHand.charAt(m)
                              + lettersOnHand.charAt(n);
                      words.addAll(validPermutations(permute));
                    }
                  }
                }
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

  // Iterative function to find permutations of a string
  public List<String> validPermutations(String s) {
    int counter = 0;
    ArrayList<String> valid = new ArrayList<String>();
    // convert the string to a character array (Since the string is immutable)
    char[] chars = s.toCharArray();

    // Weight index control array
    int[] p = new int[s.length()];

    // `i` and `j` represent the upper and lower bound index, respectively,
    // for swapping
    int i = 1, j = 0;

    // print the given string, as only its permutations will be printed later
    if (this.checkWord(s)) {

      valid.add(String.valueOf(chars));
    }
    counter++;

    while (i < s.length()) {
      if (p[i] < i) {
        // if `i` is odd then `j = p[i]`; otherwise, `j = 0`
        j = (i % 2) * p[i];

        // swap(a[j], a[i])
        swap(chars, i, j);

        // print the current permutation
        counter++;
        if (this.checkWord(String.valueOf(chars))) {
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
    for (int x = 0; x < scrabbleBoard.getScrabbleBoard().length; x++) {
      for (int y = 0; y < scrabbleBoard.getScrabbleBoard()[x].length; y++) {
        if (scrabbleBoard.getScrabbleBoard()[x][y].hasTile()) {
          char letter = scrabbleBoard.getScrabbleBoard()[x][y].getTile().getLetter();
          int value = scrabbleBoard.getScrabbleBoard()[x][y].getTile().getValue();
          wordPossibilities.add(new WordPossibility(letter, x, y, value, scrabbleBoard));
        }
      }
    }
    return wordPossibilities;
  }

  /*
  Method checks whether word is valid
   */
  public boolean checkWord(String word) {
    boolean exists = false;
    if (this.words.contains(word.toUpperCase())) {
      exists = true;
    }
    return exists;
  }

  /*
  Method reads textfile containing dictionary and assigns it to dictionary brain will use to check words
  @TODO textFile should be used to dynamically read dictionary
   */
  public void readDictionary(String textFile) {
    try {
      File dic = new File(textFile);
      FileReader fr = new FileReader(dic);
      BufferedReader br = new BufferedReader(fr);
      String z;
      while ((z = br.readLine()) != null) {
        String[] array = z.split("\\t");
        if (array.length > 1) {
          this.words.add(array[0]);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
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
