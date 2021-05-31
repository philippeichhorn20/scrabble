package backend.ai;

import backend.basic.Matchfield;
import backend.basic.Matchfield.Premiumstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import backend.basic.Timer;
import backend.basic.WordCheckDb;
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

/**
 * @author jawinter
 * @version 1.2
 * @description This class is responsible for finding possible placements of tiles to identify
 * possible combinations for words, which can be laid. The brain needs a scrabbleBoard, which it
 * tries to analyse.
 */
public class Brain implements Serializable {

  private ScrabbleBoard scrabbleBoard; //ScrabbleBoard, which is analysed by Brain
  private HashSet<String> words = new HashSet<String>(); //set of words from dictionary

  /**
   * @param board
   */
  public Brain(ScrabbleBoard board) {
    this.scrabbleBoard = board;
    if (WordCheckDb.newUrl == null) {
      readDictionary(WordCheckDb.urlTxt);
    } else {
      readDictionary(WordCheckDb.newUrl);
    }
  }

  /**
   * Method to update board brain uses to play
   *
   * @param tiles are the tiles placed on the board
   */
  public void updateScrabbleboard(Tile[] tiles) {
    for (int i = 0; i < tiles.length; i++) {
      scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
    }
  }

  /**
   * Basic setter method for scrabbleboard of brain
   *
   * @param board
   */
  public void setScrabbleboard(ScrabbleBoard board) {
    this.scrabbleBoard = board;
  }

  /**
   * Method should return all possible Words that could be played
   *
   * @param tilesOnHand the tiles of ai player should be
   * @return return value is a treeset ordered by points of a posible word
   */
  public TreeSet<PossibleWord> getPlayableWords(Tile[] tilesOnHand) {
    ArrayList<WordPossibility> wordPossibilities = getWordPossibilities();
    TreeSet<PossibleWord> playableWords = new TreeSet<PossibleWord>();
    if (!scrabbleBoard.getScrabbleBoard()[8][8].hasTile()) {
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

  /**
   * Method tries to place a word preliminary by calculating space needed
   *
   * @param currentWord     the word which is tried to put on the board
   * @param wordPossibility holds the possible slot
   * @param tilesOnHand     holds the tiles which are in rack of ai
   * @return treeset to add to all playable words
   */
  public TreeSet<PossibleWord> checkIfSpaceSufficient(String currentWord,
      WordPossibility wordPossibility, Tile[] tilesOnHand) {
    TreeSet<PossibleWord> list = new TreeSet<PossibleWord>();
    int positionBaseLetter = getPositionBaseLetter(currentWord,
        wordPossibility.getLetter().getLetter());
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
        list.add(new PossibleWord(currentWord, verticalPoints, calculatePoints,
            wordPossibility.getLetter()));
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
        list.add(new PossibleWord(currentWord, horizontalPoints, calculatePoints,
            wordPossibility.getLetter()));
      }
    }
    return list;
  }

  /**
   * This method is executed, when the ScrabbleBoard is empty
   *
   * @param tilesOnHand ai rack to look for words
   * @return returns same as getPlayablewords but deals with no tile on board due to making first
   * place tiles
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
      WordPossibility wordPossibility = new WordPossibility(tilesOnHand[i], 8, 8,
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

  /**
   * Method returns Points of a tile letter
   *
   * @param c               letter which needs points
   * @param tiles
   * @param wordPossibility possible Slot
   * @return an int which is a value for the letter
   */
  public int getPointsOfLetter(char c, Tile[] tiles, WordPossibility wordPossibility) {
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i].getLetter() == c) {
        return tiles[i].getValue();
      }
    }
    return wordPossibility.getBaseLetterValue();

  }

  /**
   * Method is responsible for determining position of letter on which ai places new tiles
   *
   * @param word       word which would be placed
   * @param baseLetter the letter which is extended
   * @return int which is like the index in the string word
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

  /**
   * Tries creating words by mixing letter with wordlength amount of tiles from tilesOnHand
   *
   * @param wordPossibility wordSlot for which words should be found
   * @param tilesOnHand     tiles which could be placed
   * @return treeset with strings containing possible words permutated
   */
  TreeSet<String> findCorrectWords(WordPossibility wordPossibility, Tile[] tilesOnHand) {
    TreeSet<String> words = new TreeSet<String>();
    char givenChar = wordPossibility.getLetter().getLetter();
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

  /**
   * swaps characters in character array
   *
   * @param charArray represents the char array
   * @param i         is first index
   * @param j         is second index
   */
  private static void swap(char[] charArray, int i, int j) {
    char ch = charArray[i];
    charArray[i] = charArray[j];
    charArray[j] = ch;
  }

  /**
   * Iterative function to find permutations of a string
   *
   * @param s the string which is permutated
   * @return list of strings, which are valid words
   */
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

  /**
   * Method returns List of slots where player could place a word
   *
   * @return slots where words could be placed
   */
  public ArrayList<WordPossibility> getWordPossibilities() {
    ArrayList<WordPossibility> wordPossibilities = new ArrayList<WordPossibility>();
    for (int x = 1; x < scrabbleBoard.getScrabbleBoard().length; x++) {
      for (int y = 1; y < scrabbleBoard.getScrabbleBoard()[x].length; y++) {
        if (scrabbleBoard.getScrabbleBoard()[x][y] != null && scrabbleBoard.getScrabbleBoard()[x][y]
            .hasTile()) {
          Tile letter = scrabbleBoard.getScrabbleBoard()[x][y].getTile();
          int value = scrabbleBoard.getScrabbleBoard()[x][y].getTile().getValue();
          wordPossibilities.add(new WordPossibility(letter, x, y, value, scrabbleBoard));
        }
      }
    }
    return wordPossibilities;
  }

  public ScrabbleBoard getScrabbleBoard() {
    return scrabbleBoard;
  }

  /**
   * Method uses internal set of words from dictionary to check if words are valid
   *
   * @param word which should be checked
   * @return true if word exists
   */
  public boolean checkWord(String word) {
    boolean exists = false;
    if (this.words.contains(word.toUpperCase())) {
      exists = true;
    }
    return exists;
  }

  /**
   * Method reads textfile containing dictionary and assigns it to dictionary brain will use to
   * check words
   *
   * @param textFile path of the txt file in correct format for dictionary see user manual for the
   *                 correct format
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

  /**
   * Method is used for a given matchfield and checks, where a matchfield has a neighboring
   * non-empty matchfield.
   *
   * @param board board the brain uses
   * @param xPos  of matchfield
   * @param yPos  of matchfield
   * @return a matchfield with the sorrounding neighbors
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

  /**
   * Method helps recognize whether array is empty or not.
   *
   * @param field which is check if it is empty
   * @return true if field empty
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

  /**
   * basic setter
   *
   * @return return the words (dictionary) actually quite large set pay attention
   */
  public HashSet<String> getWords() {
    return words;
  }

}
