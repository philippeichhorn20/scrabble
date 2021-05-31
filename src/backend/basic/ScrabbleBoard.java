package backend.basic;

import backend.basic.Matchfield.Premiumstatus;
import backend.basic.Tile.Tilestatus;
import backend.network.messages.points.PlayFeedbackMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is the representation of the physical scrabble board. It keeps track of the actual
 * board as well as the latest move. It contains matchfield which all have a certain premiumstatus
 * that is used to calcualte the points of a play. After a tile is played this tile will be added to
 * the matchfield that it is placed on. It also has a two variables which save information for a
 * single turn newTilesOfCurrentMove keeps track of the tiles that were placed in the current turn
 * in order to find out which words were affected by this play. Those are then saved in the
 * editedWords variable.
 */
public class ScrabbleBoard implements Serializable {

  private final ArrayList<Tile> tilesOnScrabbleBoard = new ArrayList<>();
  Matchfield[][] scrabbleBoard = new Matchfield[15][15];
  ArrayList<backend.basic.Tile> newTilesOfCurrentMove = new ArrayList<>();
  ArrayList<ArrayList<backend.basic.Tile>> editedWords = new ArrayList<>();

  /** this cunstructor creates an empty Scrabble Board with all the right Matchfields. */
  public ScrabbleBoard() {
    this.scrabbleBoard = setUpScrabbleBoard();
  }

  /**
   * does not set up the entire scrabbleboard but takes a matchfield array instead.
   *
   * @param scrabbleBoard a matchfield array
   */
  public ScrabbleBoard(Matchfield[][] scrabbleBoard) {
    this.scrabbleBoard = scrabbleBoard;
  }

  /**
   * adds the premiusstatus to the respective fields.
   *
   * @return the matchfield array equipped with Matchfields
   */
  public static Matchfield[][] setUpScrabbleBoard() {
    Matchfield[][] scrabbleBoard = new Matchfield[16][16];
    for (int x = 1; x < 16; x++) {
      for (int y = 1; y < 16; y++) {
        scrabbleBoard[x][y] = new Matchfield(x, y, Premiumstatus.NOPREMIUM);
        if (x == y || x == 16 - y) {
          if (((x == 1 || x == 15) && (y == 1 || y == 15))) {
            scrabbleBoard[x][y].setPremiumstatus(Premiumstatus.TRIPLEWORD);
          } else if ((x < 6 && x > 1) || (x > 10 && x < 16)) {
            scrabbleBoard[x][y].setPremiumstatus(Premiumstatus.DOUBLEWORD);
          }
        }
      }
    }
    scrabbleBoard[8][1].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[1][8].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[8][15].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[15][8].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[14][6].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[14][10].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[6][14].setPremiumstatus(Premiumstatus.TRIPLEWORD);
    scrabbleBoard[10][14].setPremiumstatus(Premiumstatus.TRIPLEWORD);

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
    scrabbleBoard[1][12].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[3][7].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[3][9].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[4][8].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[7][7].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[7][9].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[9][7].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[9][9].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[15][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[7][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[9][3].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[8][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[4][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[1][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[15][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[1][15].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[15][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[15][12].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[12][15].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[4][15].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[13][7].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[13][9].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[12][15].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[4][15].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[7][13].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[9][13].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[15][12].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[15][4].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    scrabbleBoard[8][12].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[12][8].setPremiumstatus(Premiumstatus.DOUBLELETTER);
    scrabbleBoard[12][1].setPremiumstatus(Premiumstatus.DOUBLELETTER);

    return scrabbleBoard;
  }

  /**
   * searches board for tiles.
   *
   * @param board that the tiles are placed on
   * @return returns an array of all the tiles that were placed
   */
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

  /**
   * places a tile on a given matchfield array and returns it after placing the tiles.
   *
   * @param tiles the tiles that need to be added
   * @param board the board that it has to be placed on
   * @return a clone of the given input, with tiles put on
   */
  public static Matchfield[][] placeTiles(Tile[] tiles, Matchfield[][] board) {
    Matchfield[][] temporaryScrabbleBoard = board.clone();
    for (int x = 0; x < tiles.length; x++) {
      temporaryScrabbleBoard[tiles[x].getX()][tiles[x].getY()].setTile(tiles[x]);
    }
    return temporaryScrabbleBoard;
  }

  /** prints the scrabbleboard in console. */
  public void printScrabbleBoard() {
    int count = 0;
    for (int y = 1; y < 16; y++) {
      System.out.println();
      for (int x = 1; x < 16; x++) {
        if (scrabbleBoard[x][y].hasTile()) {
          System.out.print(scrabbleBoard[x][y].getTile().getLetter() + " ");
        }
        if (x == 8 && y == 8) {
          System.out.print("00\t");
        } else {
          switch (scrabbleBoard[x][y].getPremiumstatus()) {
            case NOPREMIUM:
              System.out.print("  \t");
              break;
            case DOUBLELETTER:
              count++;
              System.out.print("2L\t");
              break;
            case TRIPLELETTER:
              count++;
              System.out.print("3L\t");
              break;
            case TRIPLEWORD:
              count++;
              System.out.print("3W\t");
              break;
            case DOUBLEWORD:
              count++;
              System.out.print("2W\t");
              break;
            default:
              break;
          }
        }
      }
    }
    System.out.println("\n" + count);
  }

  /** prints edited words that were found. */
  public void printEditedWords() {
    for (int i = 0; i < editedWords.size(); i++) {
      ArrayList<Tile> word = editedWords.get(i);
      for (int s = 0; s < word.size(); s++) {}
    }
  }

  /**
   * finds the starting word that a tile is part of in vertical direction.
   *
   * @param tile the tile that the starting letter is looked for
   * @return the tile which would be the starting letter of a word in vertical direction
   */
  public Tile getLeadingTileVertical(Tile tile) {
    while (tile.getY() > 0 && scrabbleBoard[tile.getX()][tile.getY() - 1].hasTile()) {
      tile = scrabbleBoard[tile.getX()][tile.getY() - 1].getTile();
    }
    return tile;
  }

  /**
   * returns the tile which would be the starting letter of a word in horizontal direction.
   *
   * @param tile the tile that the starting letter is looked for
   * @return the tile which would be the starting letter of a word in horizontal direction
   */
  public Tile getLeadingTileHorizontal(Tile tile) {
    while (tile.getX() > 0 && scrabbleBoard[tile.getX() - 1][tile.getY()].hasTile()) {
      tile = scrabbleBoard[tile.getX() - 1][tile.getY()].getTile();
    }
    return tile;
  }

  /**
   * returns the word which starts with the @param tile in the vertical direction as array list.
   *
   * @param tile the starting
   * @return the array going from the tile to the right
   */
  public ArrayList<Tile> getWordFromLeadingTileVertical(Tile tile) {
    ArrayList<Tile> word = new ArrayList<Tile>();

    word.add(tile);
    while (tile.getX() <= 15
        && tile.getY() <= 15
        && scrabbleBoard[tile.getX()][tile.getY() + 1].hasTile()) {
      tile = scrabbleBoard[tile.getX()][tile.getY() + 1].getTile();
      word.add(tile);
    }
    return word;
  }

  /**
   * returns the word which starts with the @param tile in the horizontal direction as array list.
   *
   * @param tile the starting
   * @return the array going from the tile to the bottom
   */
  ArrayList<Tile> getWordFromLeadingTileHorizontal(Tile tile) {
    ArrayList<Tile> word = new ArrayList<Tile>();
    word.add(tile);
    while (tile.getX() < 15
        && tile.getY() < 15
        && scrabbleBoard[tile.getX() + 1][tile.getY()].hasTile()) {
      tile = scrabbleBoard[tile.getX() + 1][tile.getY()].getTile();
      word.add(tile);
    }
    return word;
  }

  /**
   * checks all the current words and returns the word+description in the message of the word.
   *
   * @param from the player that requests the word check
   * @return a message that holds all the collected information
   */
  public PlayFeedbackMessage wordCheck(String from) {
    boolean inputInvalid = false;
    ArrayList<String> result = new ArrayList<>();
    String[] words = getEditedWordsAsString(false);
    for (int i = 0; i < words.length; i++) {
      String resultString = WordCheckDb.findWord(words[i]);
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

  /**
   * this functions simulates the placing of a single tile on the board. It also stores it into the
   * temporary list that keeps track of the current move.
   *
   * @param newTile the tile
   * @param x the index in the matchfield array
   * @param y the index in the matchfield array
   */
  public void placeTile(backend.basic.Tile newTile, int x, int y) {
    this.newTilesOfCurrentMove.add(newTile);
    newTile.setXY(x, y);
    this.scrabbleBoard[x][y].setTile(newTile);
    newTile.setStatus(Tilestatus.ONBOARD);
    this.tilesOnScrabbleBoard.add(newTile);
  }

  /**
   * this function removes the Tile from the Board. It is only possible to remove it, if it was
   * placed in the current turn. It removes true if that is the case and false if it was not.
   *
   * @param tile the tile that need removal
   */
  public void removeTile(final backend.basic.Tile tile) {
    this.scrabbleBoard[tile.getX()][tile.getY()].setTile(null);
  }

  /**
   * checks, if a word is already in the editedWords list by comparing the first two tiles.
   *
   * @param tile the tile that is looked for
   * @return if it is the case or not
   */
  public boolean isInEditedTiles(Tile tile) {
    for (int i = 0; i < editedWords.size(); i++) {
      int x = tile.getX();
      int y = tile.getY();
      Tile firstTile = editedWords.get(i).get(0);
      if ((x == firstTile.getX()) && (y == firstTile.getY())) {
        if (scrabbleBoard[x + 1][y].hasTile()
            && (scrabbleBoard[x + 1][y].getTile().getX() == editedWords.get(i).get(1).getX())) {
          return true;
        } else if (scrabbleBoard[x][y + 1].hasTile()
            && scrabbleBoard[x][y + 1].getTile().getY() == editedWords.get(i).get(1).getY()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * finishes its turn and submits all the words. See Scrabble test, for further information about
   * the algorithm that finds coneected tiles.
   *
   * @param from the String determining the person who requestst the move
   * @return the PlayFeedBack message
   */
  public PlayFeedbackMessage submitTiles(String from) {
    for (int i = 0; i < newTilesOfCurrentMove.size(); i++) {
      if (getWordFromLeadingTileHorizontal(getLeadingTileHorizontal(newTilesOfCurrentMove.get(i)))
                  .size()
              > 1
          && !isInEditedTiles(getLeadingTileHorizontal(newTilesOfCurrentMove.get(i)))) {
        editedWords.add(
            getWordFromLeadingTileHorizontal(
                getLeadingTileHorizontal(newTilesOfCurrentMove.get(i))));
      }
      if (getWordFromLeadingTileVertical(getLeadingTileVertical(newTilesOfCurrentMove.get(i)))
                  .size()
              > 1
          && !isInEditedTiles(getLeadingTileVertical(newTilesOfCurrentMove.get(i)))) {
        editedWords.add(
            getWordFromLeadingTileVertical(getLeadingTileVertical(newTilesOfCurrentMove.get(i))));
      }
    }
    return wordCheck(from);
  }

  /**
   * this method calculates the points of the current move.
   *
   * @return the amount of points the move gave
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
        System.out.println(letterValue);
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
      points += 50; // Bingo rule
    }
    return points;
  }

  /**
   * method returns points of string. It does not take other tiles on the board into considertion
   * (as its static)
   *
   * @param tiles the tiles of which the points are calculated
   * @return the amount
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

  /** this method clears the fields that are only tracking the information of the current move. */
  public void nextTurn() {
    editedWords.clear();
    newTilesOfCurrentMove.clear();
  }

  /** this method drops the changed tiles. */
  public void dropChangedTiles() {
    this.editedWords.clear();
    Iterator<Tile> iterator = newTilesOfCurrentMove.iterator();
    while (iterator.hasNext()) {
      Tile tile = iterator.next();
      this.removeTile(tile);
    }
    newTilesOfCurrentMove.clear();
  }

  /**
   * this method gets the edited words as a string.
   *
   * @param printIt can be used for debugging
   * @return the words as Strings
   */
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

  // getter and setter

  public Matchfield[][] getScrabbleBoard() {
    return this.scrabbleBoard;
  }

  public void setScrabbleBoard(Matchfield[][] scrabbleBoard) {
    this.scrabbleBoard = scrabbleBoard;
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

  /**
   * This functions returns the tiles that were placed on this turn.
   *
   * @return tile array.
   */
  public Tile[] lastPlacedTiles() {
    Tile[] tiles = new Tile[this.newTilesOfCurrentMove.size()];
    for (int x = 0; x < this.newTilesOfCurrentMove.size(); x++) {
      tiles[x] = this.newTilesOfCurrentMove.get(x);
    }
    return tiles;
  }

  /*
  @author peichhor
  this method finds out weather the tiles are placed properly.
  For the tiles to be valid there always has to be a tile which is placed on the center matchfield
  and all the words need to be connected to each other.

  @explanation We solved this implementing a recursive algorithm.
  Each tile adds their neighbouring tiles to the @variable discovered list.
  And then the new tile does the same

   */

  /**
   * this method finds out weather the tiles are placed properly. For the tiles to be valid there
   * always has to be a tile which is placed on the center matchfield and all the words need to be
   * connected to each other.
   *
   * @explanation We solved this implementing a recursive algorithm. Each tile adds their
   *     neighbouring tiles to the @variable discovered list. And then the new tile does the same
   * @param tiles the new tiles of the move
   * @return if the tales were placed properly (ignoring content)
   */
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

  /**
   * this method looks in all 4 directions to find neighbouring tiles.
   *
   * @param tile The tile of which neighbouring tiles are searched for
   * @param board the board in which the tiles are
   * @param alreadyDiscovered the tiles that were already dicovered
   */
  public void discoverTile(Tile tile, Matchfield[][] board, ArrayList<Tile> alreadyDiscovered) {
    if (tile.getY() < 15
        && board[tile.getX()][tile.getY() + 1].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX()][tile.getY() + 1].getTile())) {
      alreadyDiscovered.add(board[tile.getX()][tile.getY() + 1].getTile());
      discoverTile(board[tile.getX()][tile.getY() + 1].getTile(), board, alreadyDiscovered);
    }
    if (tile.getX() < 15
        && board[tile.getX() + 1][tile.getY()].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX() + 1][tile.getY()].getTile())) {
      alreadyDiscovered.add(board[tile.getX() + 1][tile.getY()].getTile());
      discoverTile(board[tile.getX() + 1][tile.getY()].getTile(), board, alreadyDiscovered);
    }
    if (tile.getY() > 1
        && board[tile.getX()][tile.getY() - 1].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX()][tile.getY() - 1].getTile())) {
      alreadyDiscovered.add(board[tile.getX()][tile.getY() - 1].getTile());
      discoverTile(board[tile.getX()][tile.getY() - 1].getTile(), board, alreadyDiscovered);
    }
    if (tile.getX() > 1
        && board[tile.getX() - 1][tile.getY()].hasTile()
        && !alreadyDiscovered.contains(board[tile.getX() - 1][tile.getY()].getTile())) {
      alreadyDiscovered.add(board[tile.getX() - 1][tile.getY()].getTile());
      discoverTile(board[tile.getX() - 1][tile.getY()].getTile(), board, alreadyDiscovered);
    }
  }
}

/*
BIN:


 */
