package backend.basic;

import backend.basic.Matchfield.Premiumstatus;
import backend.basic.Tile.Tilestatus;
import java.util.ArrayList;
/* @author peichhor
 * @version 1.0
 * @description this class is the representation of the physical scrabble board
 *
 * @param scrabbleBoard is the physical representation fo the actual boards. It contains matchfield
 * which all have a certain premiumstatus that is used to calcualte the points of a play. After
 * a tile is played this tile will be added to the matchfield that it is placed on
 *
 * it also has a two variables which saves information for a single turn
 *
 * @param newTilesOfCurrentMove keeps track of the tiles that were placed in the current turn in order
 * to find out which words were affected by this play.
 *
 * @param editedWords Those are then saved in the editedWords variable
 * */

public class ScrabbleBoard {

  private final ArrayList<Tile> tilesOnScrabbleBoard = new ArrayList<>();
  Matchfield[][] scrabbleBoard = new Matchfield[15][15];
  ArrayList<backend.basic.Tile> newTilesOfCurrentMove = new ArrayList<>();
  ArrayList<ArrayList<backend.basic.Tile>> editedWords = new ArrayList<>();

  /*
   * this function creates an empty Scrabble Board with all the right Matchfields
   * TODO: add the rest of the Premiumstatuses
   */

  public ScrabbleBoard(){
    this.scrabbleBoard = getScrabbleBoard();
  }

  /*
  in case tiles were already placed
   */
  public ScrabbleBoard(Matchfield[][] scrabbleBoard){
    this.scrabbleBoard = scrabbleBoard;
  }

  public Matchfield[][] getScrabbleBoard() {
    return this.scrabbleBoard;
  }

  public static Matchfield[][] setUpScrabbleBoard() {
    Matchfield[][] scrabbleBoard = new Matchfield[15][15];
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


    return scrabbleBoard;
  }


  public void printScrabbleBoard() {
    for (int y = 0; y < 15; y++) {
      for (int x = 0; x < 15; x++) {
        if (scrabbleBoard[x][y].hasTile()) {
          System.out.print(scrabbleBoard[x][y].getTile().getLetter() + ":");
        } else {
          switch (scrabbleBoard[x][y].getPremiumstatus()) {
            case NOPREMIUM:
              System.out.print("N" + "\t");
              break;
            case DOUBLELETTER:
              System.out.print("N" + "\t");
              break;
            case TRIPLELETTER:
              System.out.print("N" + "\t");
              break;
            case TRIPLEWORD:
              System.out.print("N" + "\t");
              break;
            case DOUBLEWORD:
              System.out.print("N" + "\t");
              break;
          }
        }
      }
      System.out.println();
    }
    for (int i = 0; i < newTilesOfCurrentMove.size(); i++) {
      System.out.print(newTilesOfCurrentMove.get(i).getLetter() + " | ");
    }

  }

  public void printEditedWords() {
    for (int i = 0; i < editedWords.size(); i++) {
      ArrayList<Tile> word = editedWords.get(i);
      System.out.print("wordsize " + word.size() + ": ");
      for (int s = 0; s < word.size(); s++) {
        System.out.print(editedWords.get(s).get(i).getLetter());
      }
      System.out.println();
    }
    System.out.println();
  }


  public Tile getLeadingTileVertical(Tile tile) {
    while (tile.getY() > 0 && scrabbleBoard[tile.getX()][tile.getY() - 1].hasTile()) {
      tile = scrabbleBoard[tile.getX()][tile.getY() - 1].getTile();
    }
    return tile;
  }

  Tile getLeadingTileHorizontal(Tile tile) {
    while (tile.getX() > 0 && scrabbleBoard[tile.getX() - 1][tile.getY()].hasTile()) {
      tile = scrabbleBoard[tile.getX() - 1][tile.getY()].getTile();
    }

    return tile;
  }

  public ArrayList<Tile> getWordFromLeadingTileVertical(Tile tile) {
    ArrayList<Tile> word = new ArrayList<Tile>();

    word.add(tile);
    while (tile.getX() <= 15 && tile.getY() <= 15 && scrabbleBoard[tile.getX()][tile.getY() + 1]
        .hasTile()) {
      tile = scrabbleBoard[tile.getX()][tile.getY() + 1].getTile();
      word.add(tile);
    }
    return word;

  }

  ArrayList<Tile> getWordFromLeadingTileHorizontal(Tile tile) {
    ArrayList<Tile> word = new ArrayList<Tile>();
    word.add(tile);
    while (tile.getX() < 15 && tile.getY() < 15 && scrabbleBoard[tile.getX() + 1][tile.getY()]
        .hasTile()) {
      tile = scrabbleBoard[tile.getX() + 1][tile.getY()].getTile();
      word.add(scrabbleBoard[tile.getX() + 1][tile.getY()].getTile());
    }

    return word;


  }


  /*
  checks all the current words and returns the word+descriptipn of the word
   */
  public String[][] wordCheck() {
    String[][] result = new String[2][];
    String[] words = getEditedWordsAsString(false);
    String[] explanations = new String[words.length];
    for (int i = 0; i < words.length; i++) {
      explanations[i] = String.valueOf(WordCheckDB.findWord(words[i]));
      if (explanations[i] != "") {
        System.out.println(explanations[i]);
      }
    }
    result[0] = words;
    result[1] = explanations;
    return result;
  }

  public boolean inputValudation(String[][] result) {
    for (int x = 0; x < result[0].length; x++) {
      return result[1][x] != "";
    }
    return true;
  }

  /* this functions simulates the placing of a single tile on the board.
  It also stores it into the temporary list that keeps track of the current move
   */
  public void placeTile(backend.basic.Tile newTile, int x, int y) {
    this.newTilesOfCurrentMove.add(newTile);
    newTile.setXY(x, y);
    scrabbleBoard[x][y].setTile(newTile);
    newTile.setStatus(Tilestatus.ONBOARD);
    tilesOnScrabbleBoard.add(newTile);
  }

  /*
  this function removes the Tile from the Board. It is only possible to remove it,
  if it was placed in the current turn. It removes true if that is the case and false if it was not
   */
  public boolean removeTile(final backend.basic.Tile tile) {
    if (newTilesOfCurrentMove.contains(tile)) {
      newTilesOfCurrentMove.remove(tile);
      return true;
    } else {
      return false;
    }
  }

  public boolean isInEditedTiles(Tile tile) {
    for (int i = 0; i < editedWords.size(); i++) {
      int x = tile.getX();
      int y = tile.getY();
      Tile firstTile = editedWords.get(i).get(0);
      if ((x == firstTile.getX()) && (y == firstTile.getY())) {
        if (scrabbleBoard[x + 1][y].hasTile() && (scrabbleBoard[x + 1][y].getTile().getX()
            == editedWords.get(i).get(1).getX())) {
          return true;
        } else if (scrabbleBoard[x][y + 1].hasTile()
            && scrabbleBoard[x][y + 1].getTile().getY() == editedWords.get(i).get(1).getY()) {
          return true;

        }

      }
    }
    System.out.println();
    return false;
  }

  /*
  finishes its turn and submits all the words
   */
  public String[][] submitTiles() {
    for (int i = 0; i < newTilesOfCurrentMove.size(); i++) {
      if (getWordFromLeadingTileHorizontal(getLeadingTileHorizontal(newTilesOfCurrentMove.get(i)))
          .size() > 1 && !isInEditedTiles(getLeadingTileHorizontal(newTilesOfCurrentMove.get(i)))) {
        editedWords.add(getWordFromLeadingTileHorizontal(
            getLeadingTileHorizontal(newTilesOfCurrentMove.get(i))));
      }
      if (getWordFromLeadingTileVertical(getLeadingTileVertical(newTilesOfCurrentMove.get(i)))
          .size() > 1 && !isInEditedTiles(getLeadingTileVertical(newTilesOfCurrentMove.get(i)))) {
        editedWords.add(
            getWordFromLeadingTileVertical(getLeadingTileVertical(newTilesOfCurrentMove.get(i))));
      }
    }
    return wordCheck();
  }

  /*
  this method calculates the points of the current move
   */
  public int getPoints() {
    int points = 0;
    for (int wordNum = 0; wordNum < editedWords.size(); wordNum++) {
      int pointsOfWord = 0;
      ArrayList<Matchfield> wordAsMatchfields = new ArrayList<>();
      ArrayList<Tile> word = editedWords.get(wordNum);
      int wordMultiplikant = 1;
      for (int letterNum = 0; letterNum < word.size(); letterNum++) {
        Tile letter = word.get(letterNum);
        wordAsMatchfields.add(scrabbleBoard[letter.getX()][letter.getY()]);
      }
      for (int length = 0; length < wordAsMatchfields.size(); length++) {
        int letterValue = 0;
        Matchfield currentField = wordAsMatchfields.get(length);
        letterValue = currentField.getTile().getValue();
        switch (currentField.getPremiumstatus()) {
          case DOUBLELETTER:
            letterValue *= 2;
            break;
          case TRIPLELETTER:
            letterValue *= 3;
            break;
          case DOUBLEWORD:
            wordMultiplikant *= 2;
            break;
          case TRIPLEWORD:
            wordMultiplikant *= 3;
            break;
          default:
            break;

        }
        pointsOfWord += letterValue;
      }
      pointsOfWord *= wordMultiplikant;
      points += pointsOfWord;
    }
    return points;
  }

  /*
  this method clears the fields that are only tracking the information of the current move
   */
  public void nextTurn() {
    editedWords.clear();
    newTilesOfCurrentMove.clear();
  }

  public String[] getEditedWordsAsString(boolean printIt) {
    String[] words = new String[editedWords.size()];
    for (int x = 0; x < words.length; x++) {
      words[x] = "";
      for (int i = 0; i < editedWords.get(x).size(); i++) {
        words[x] += editedWords.get(x).get(i).getLetter();
      }
      if (printIt) {
        System.out.println(words[x]);
      }
    }
    return words;
  }

  public ArrayList<Tile> getTilesOnScrabbleBoard() {
    return tilesOnScrabbleBoard;
  }


  public ArrayList<ArrayList<Tile>> getEditedWords() {
    return editedWords;
  }

  public void setEditedWords(ArrayList<ArrayList<Tile>> editedWords) {
    this.editedWords = editedWords;
  }

  public ArrayList<Tile> getNewTilesOfCurrentMove() {
    return newTilesOfCurrentMove;
  }

  public void setNewTilesOfCurrentMove(ArrayList<Tile> newTilesOfCurrentMove) {
    this.newTilesOfCurrentMove = newTilesOfCurrentMove;
  }


  public void setScrabbleBoard(Matchfield[][] scrabbleBoard) {
    this.scrabbleBoard = scrabbleBoard;
  }

  public Tile[] lastPlacedTiles() {
    Tile[] tiles = new Tile[this.newTilesOfCurrentMove.size()];
    for (int x = 0; x < this.newTilesOfCurrentMove.size(); x++) {
      tiles[x] = this.newTilesOfCurrentMove.get(x);
    }
    return tiles;
  }

}




/*
BIN:


 */