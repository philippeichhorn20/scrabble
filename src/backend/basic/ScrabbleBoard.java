package backend.basic;

import backend.basic.Matchfield.Premiumstatus;
import backend.basic.Tile.Tilestatus;
import backend.network.messages.points.PlayFeedbackMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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

public class ScrabbleBoard implements Serializable {

  private final ArrayList<Tile> tilesOnScrabbleBoard = new ArrayList<>();
  Matchfield[][] scrabbleBoard = new Matchfield[15][15];
  ArrayList<backend.basic.Tile> newTilesOfCurrentMove = new ArrayList<>();
  ArrayList<ArrayList<backend.basic.Tile>> editedWords = new ArrayList<>();

  /*
   * this function creates an empty Scrabble Board with all the right Matchfields
   * TODO: add the rest of the Premiumstatuses
   */

  public ScrabbleBoard() {
    this.scrabbleBoard = setUpScrabbleBoard();

  }

  /*
  in case tiles were already placed
   */
  public ScrabbleBoard(Matchfield[][] scrabbleBoard) {
    this.scrabbleBoard = scrabbleBoard;
  }

  public Matchfield[][] getScrabbleBoard() {
    return this.scrabbleBoard;
  }

  public static Matchfield[][] setUpScrabbleBoard() {
    Matchfield[][] scrabbleBoard = new Matchfield[16][16];
    for (int x = 1; x < 16; x++) {
      for (int y = 1; y < 16; y++) {
        scrabbleBoard[x][y] = new Matchfield(x, y, Premiumstatus.NOPREMIUM);
        if (x == y || x == 15 - y) {
          if (((x == 1 || x == 15) && (y == 1 || y == 15))) {
            scrabbleBoard[x][y].setPremiumstatus(Premiumstatus.TRIPLEWORD);
          } else if ((x < 6 && x > 1) || (x > 10 && x < 15)) {
            scrabbleBoard[x][y].setPremiumstatus(Premiumstatus.DOUBLEWORD);
          }
        }
      }
    }

    scrabbleBoard[2][6].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[6][2].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[2][10].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[10][2].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[6][6].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[10][10].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[6][10].setPremiumstatus(Premiumstatus.TRIPLELETTER);
    scrabbleBoard[10][6].setPremiumstatus(Premiumstatus.TRIPLELETTER);

    scrabbleBoard[4][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[1][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[11][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[1][11].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[7][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[9][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[8][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[1][12].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[12][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[15][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[4][11].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[7][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[9][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[8][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[4][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[1][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[11][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[1][11].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[7][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[9][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[8][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    return scrabbleBoard;
  }


  public void printScrabbleBoard() {
    for (int y = 1; y < 16; y++) {
      System.out.println();
      for (int x = 1; x < 16; x++) {
        if (scrabbleBoard[x][y].hasTile()) {
          System.out.print(scrabbleBoard[x][y].getTile().getLetter() + " ");
        } else {
          switch (scrabbleBoard[x][y].getPremiumstatus()) {
            case NOPREMIUM:
              System.out.print(" " + " ");
              break;
            case DOUBLELETTER:
              System.out.print(" " + " ");
              break;
            case TRIPLELETTER:
              System.out.print(" " + " ");
              break;
            case TRIPLEWORD:
              System.out.print(" " + " ");
              break;
            case DOUBLEWORD:
              System.out.print(" " + " ");
              break;
          }
        }
      }
    }


  }

  public void printEditedWords() {
    for (int i = 0; i < editedWords.size(); i++) {
      ArrayList<Tile> word = editedWords.get(i);
      for (int s = 0; s < word.size(); s++) {
      }
    }
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
      word.add(tile);
    }
    return word;


  }


  /*
  checks all the current words and returns the word+descriptipn of the word
   */
  public PlayFeedbackMessage wordCheck(String from) {
    boolean inputInvalid = false;
    ArrayList<String> result = new ArrayList<>();
    String[] words = getEditedWordsAsString(true);
    for (int i = 0; i < words.length; i++) {
      String resultString = WordCheckDB.findWord(words[i]);
      if (resultString == "") {
        result.add(words[i] + " - word isn't valid");
        inputInvalid = true;
      } else if (!inputInvalid) {
        result.add(resultString + "\n");
      }
    }
    GameInformation.getInstance().getClientmatch().setInvalidMove(inputInvalid);
    return new PlayFeedbackMessage(from, result, !inputInvalid);
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
    this.scrabbleBoard[x][y].setTile(newTile);
    newTile.setStatus(Tilestatus.ONBOARD);
    this.tilesOnScrabbleBoard.add(newTile);
  }


  /*
  this function removes the Tile from the Board. It is only possible to remove it,
  if it was placed in the current turn. It removes true if that is the case and false if it was not
   */
  public void removeTile(final backend.basic.Tile tile) {
    this.scrabbleBoard[tile.getX()][tile.getY()].setTile(null);
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
    return false;
  }

  /*
  finishes its turn and submits all the words
   */
  public PlayFeedbackMessage submitTiles(String from) {
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
    return wordCheck(from);
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
    if (this.newTilesOfCurrentMove.size() == 7) {
      points += 50;
    }
    return points;
  }

  /*
  @author jawinter
  method returns points of string
   */
  public int getPointsOfWord(ArrayList<Tile> tiles) {
    int pointsOfWord = 0;
    int wordMultiplikant = 1;
    ArrayList<Matchfield> wordAsMatchfields = new ArrayList<Matchfield>();
    for (int letterNum = 0; letterNum < tiles.size(); letterNum++) {
      Tile letter = tiles.get(letterNum);
      Matchfield copy = new Matchfield(scrabbleBoard[letter.getX()][letter.getY()]);
      copy.setTile(letter);
      wordAsMatchfields.add(copy);
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
    return pointsOfWord;
  }

  /*
  this method clears the fields that are only tracking the information of the current move
   */
  public void nextTurn() {
    editedWords.clear();
    newTilesOfCurrentMove.clear();

    // newTilesOfCurrentMove.clear();
  }

  public void dropChangedTiles() {
    this.editedWords.clear();
    Iterator<Tile> iterator = newTilesOfCurrentMove.iterator();
    while (iterator.hasNext()) {
      Tile tile = iterator.next();
      this.removeTile(tile);
    }
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

  public static boolean hasTileOnCenterMatchfield(Tile[] tiles) {
    for (int x = 0; x < tiles.length; x++) {
      if (tiles[x].getY() == 8 && tiles[x].getY() == 8) {
        return true;
      }
    }
    return false;
  }

  public boolean wordIsConnectedToMiddle(Tile[] tiles) {
    Matchfield[][] temporaryScrabbleBoard = placeTiles(tiles, this.scrabbleBoard);
    ArrayList<Tile> tilesOnBoard = getTilesOnBoard(temporaryScrabbleBoard);
    if (temporaryScrabbleBoard[8][8].hasTile()) {
      ArrayList<Tile> discoveredTiles = new ArrayList<>();
      discoveredTiles.add(temporaryScrabbleBoard[8][8].getTile());
      discoverTile(temporaryScrabbleBoard[8][8].getTile(), temporaryScrabbleBoard, discoveredTiles);
      if (discoveredTiles.size() == tilesOnBoard.size()) {
        return true;
      }
    }
    return false;
  }

  public void discoverTile(Tile tile, Matchfield[][] board, ArrayList<Tile> alreadyDiscovered) {
    if (tile.getY() < 15 && board[tile.getX()][tile.getY() + 1].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX()][tile.getY() + 1].getTile())) {
      alreadyDiscovered.add(board[tile.getX()][tile.getY() + 1].getTile());
      discoverTile(board[tile.getX()][tile.getY() + 1].getTile(), board, alreadyDiscovered);
    }
    if (tile.getX() < 15 && board[tile.getX() + 1][tile.getY()].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX() + 1][tile.getY()].getTile())) {
      alreadyDiscovered.add(board[tile.getX() + 1][tile.getY()].getTile());
      discoverTile(board[tile.getX() + 1][tile.getY()].getTile(), board, alreadyDiscovered);
    }
    if (tile.getY() > 1 && board[tile.getX()][tile.getY() - 1].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX()][tile.getY() - 1].getTile())) {
      alreadyDiscovered.add(board[tile.getX()][tile.getY() - 1].getTile());
      discoverTile(board[tile.getX()][tile.getY() - 1].getTile(), board, alreadyDiscovered);
    }
    if (tile.getX() > 1 && board[tile.getX() - 1][tile.getY()].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX() - 1][tile.getY()].getTile())) {
      alreadyDiscovered.add(board[tile.getX() - 1][tile.getY()].getTile());
      discoverTile(board[tile.getX() - 1][tile.getY()].getTile(), board, alreadyDiscovered);

    }


  }

  public static ArrayList<Tile> getTilesOnBoard(Matchfield[][] board) {
    ArrayList<Tile> tilesOnBoard = new ArrayList<>();
    for (int x = 1; x < board.length; x++) {
      for (int y = 1; y < board.length; y++) {
        if (board[x][y].hasTile()) {
          tilesOnBoard.add(board[x][y].getTile());
        }
      }
    }

    return tilesOnBoard;
  }

  public static Matchfield[][] placeTiles(Tile[] tiles, Matchfield[][] board) {
    Matchfield[][] temporaryScrabbleBoard = board.clone();
    for (int x = 0; x < tiles.length; x++) {
      temporaryScrabbleBoard[tiles[x].getX()][tiles[x].getY()].setTile(tiles[x]);
    }
    return temporaryScrabbleBoard;
  }
}





/*
BIN:


 */