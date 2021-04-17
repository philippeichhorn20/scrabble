package backend.basic;

import java.util.ArrayList;

/* @author peichhor
 * @version 1.0
 * @description this class is the representation of the physical scrabble board
 *
 * @param scrabbleBoard is the physical representation fo the actual boards. It conatins matchfield
 * which all have a certain premiumstatus that is used to calcualte the points of a play. After
 * a tile is played this tile will be added to the matchfield that it is placed on
 *
 * it also has a two variables which saves information for a single turn
 *
 * @param newTilesOfCurrentMove keeps track of the tiles that were placed in the current turn in order
 * to find out which words were affected by this play.
 *
 * @param editedWords Those are then saved in the editedWords variable
 * TODO: int getPoints() which returns the points of a play
 * */

class ScrabbleBoard {

  static Matchfield[][] scrabbleBoard = new Matchfield[15][15];
  static ArrayList<backend.basic.Tile> newTilesOfCurrentMove = new ArrayList<>();
  static ArrayList<ArrayList<backend.basic.Tile>> editedWords = new ArrayList<>();

  /*
   * this function creates an empty Scrabble Board with all the right Matchfields
   * TODO: add the rest of the Premiumstatuses
   */
  static void setUpScrabbleBoard() {
    for (int x = 0; x < 15; x++) {
      for (int y = 0; y < 15; y++) {
        scrabbleBoard[x][y] = new Matchfield(x, y, Premiumstatus.NOPREMIUM);
        if (x == y || x == 14 - y) {
          if (((x == 0 || x == 14) && (y == 0 || y == 14))) {
            scrabbleBoard[x][y].setPremiumstatus(Premiumstatus.TRIPLEWORD);
          } else if ((x < 5 && x > 0) || (x > 9 && x < 14)) {
            scrabbleBoard[x][y].setPremiumstatus(Premiumstatus.DOUBLEWORD);
          }
        }
      }
    }
    scrabbleBoard[0][7].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[14][7].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[7][0].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[7][14].setPremiumstatus(Premiumstatus.TRIPLEWORD);
  }

  static void printScrabbleBoard() {
    for (int x = 0; x < 15; x++) {
      for (int y = 0; y < 15; y++) {
        switch (scrabbleBoard[x][y].getPremiumstatus()) {
          case NOPREMIUM:
            System.out.print("o" + "\t");
            break;
          case DOUBLELETTER:
            System.out.print("2" + "\t");
            break;
          case TRIPLELETTER:
            System.out.print("3" + "\t");
            break;
          case TRIPLEWORD:
            System.out.print("33" + "\t");
            break;
          case DOUBLEWORD:
            System.out.print("22" + "\t");
            break;
        }

      }
      System.out.println();
    }
  }

  static void addWordToEdited(final ArrayList<backend.basic.Tile> word) {
    if (word.size() > 1) {
      final backend.basic.Tile startingLetterOfNewWord = word.get(0);
      for (int a = 0; a < ScrabbleBoard.editedWords.size(); a++) {
        final ArrayList<backend.basic.Tile> wordInList = ScrabbleBoard.editedWords.get(a);
        final backend.basic.Tile startingLetterInList = wordInList.get(0);
        if (startingLetterInList.getY() == startingLetterOfNewWord.getY()
            && startingLetterInList.getX() == startingLetterOfNewWord.getX()) {
          final backend.basic.Tile secondWordInList = wordInList.get(1);
          final backend.basic.Tile secondLetterOfNewWord = word.get(1);
          if (secondWordInList.getY() == secondLetterOfNewWord.getY()
              && secondWordInList.getX() == secondLetterOfNewWord.getX()) {
            break;
          }
        }
        ScrabbleBoard.editedWords.add(word);
      }
    }
  }

  /*
  checks all the current words and returns the word+descriptipn of the word
   */
  static boolean wordCheck() {
    for (int i = 0; i < ScrabbleBoard.editedWords.size(); i++) {
      final ArrayList<backend.basic.Tile> word = ScrabbleBoard.editedWords.get(i);
      String wordAsString = "";
      for (int a = 0; a < word.size(); a++) {
        Tile tile = word.get(a);
        wordAsString += tile.getLetter();
      }
      if (!WordCheckDB.findWord(wordAsString)) {
        return false;
      }
    }
    return true;
  }

  /* this functions simulates the placing of a single tile on the board.
  It also stores it into the temporary list that keeps track of the current move
   */
  void placeTile(final backend.basic.Tile newTile) {
    ScrabbleBoard.newTilesOfCurrentMove.add(newTile);
    scrabbleBoard[newTile.getX()][newTile.getY()].setTile(newTile);
  }

  /*
  this function removes the Tile from the Board. It is only possible to remove it,
  if it was placed in the current turn. It removes true if that is the case and false if it was not
   */
  boolean removeTile(final backend.basic.Tile tile) {
    if (ScrabbleBoard.newTilesOfCurrentMove.contains(tile)) {
      ScrabbleBoard.newTilesOfCurrentMove.remove(tile);
      return true;
    } else {
      return false;
    }
  }


  /*
  finishes its turn and submits all the words
   */
  boolean submitTiles() {
    for (int i = 0; i < ScrabbleBoard.newTilesOfCurrentMove.size(); i++) {
      backend.basic.Tile tile = ScrabbleBoard.newTilesOfCurrentMove.get(i);
      //find the starting letter of the word in horizontal direction
      while ((tile.getX() >= 1) && scrabbleBoard[tile.getX() - 1][tile.getY()].hasTile()) {
        tile = scrabbleBoard[tile.getX() + 1][tile.getY()].getTile();
      }
      //now add the Tiles following tiles to the word
      int numberOfLetters = 1;
      final ArrayList<backend.basic.Tile> word = new ArrayList<>();
      word.add(scrabbleBoard[tile.getX()][tile.getY()].getTile());
      while ((tile.getX() + numberOfLetters <= 15) && scrabbleBoard[tile.getX()
          + numberOfLetters][tile.getY()].hasTile()) {
        word.add(scrabbleBoard[tile.getX() + numberOfLetters][tile.getY()].getTile());
      }
      ScrabbleBoard.addWordToEdited(word);
      word.clear();
      //find the starting letter in vertical direction
      while ((tile.getX() >= 1) && scrabbleBoard[tile.getX() - 1][tile.getY()].hasTile()) {
        tile = scrabbleBoard[tile.getX() + 1][tile.getY()].getTile();
      }
      //now add the Tiles following tiles to the word
      numberOfLetters = 1;
      word.add(scrabbleBoard[tile.getX()][tile.getY()].getTile());
      while ((tile.getY() + numberOfLetters <= 15) && scrabbleBoard[tile.getX()][tile.getY()
          + numberOfLetters].hasTile()) {
        word.add(scrabbleBoard[tile.getX()][tile.getY() + numberOfLetters].getTile());
      }
      ScrabbleBoard.addWordToEdited(word);
      word.clear();
    }
    return ScrabbleBoard.wordCheck();
  }
}