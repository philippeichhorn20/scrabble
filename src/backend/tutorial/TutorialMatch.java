package backend.tutorial;

import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.TileBag;

/**
 * A match class for the tutorial.
 *
 * @author nilschae
 */
public class TutorialMatch {
  private TileBag tutorialBag;
  private ScrabbleBoard tutorialBoard;
  public Tile[] tutorialRackTiles = new Tile[7];
  private boolean startFlag,
      highligthTilesFlag,
      highlightScrabbleboardPositionFlag,
      endFlag,
      gameOver;
  public String welcomeContentTitel = "", welcomeContentText = "";
  public String highlightedTilesTitle = "", higlightedTilesContentText = "";
  public String highligthScrabbleboardPositionTitle = "",
      highligthScrabbleboardPositionContentText = "";
  public String endGameTitle = "", endGameContentText = "";

  /** A constructor for the tutorial match. */
  public TutorialMatch() {
    tutorialBag = new TileBag();
    tutorialBoard = new ScrabbleBoard();

    tutorialRackTiles[0] = new Tile('H', 4);
    tutorialRackTiles[1] = new Tile('E', 1);
    tutorialRackTiles[2] = new Tile('L', 1);
    tutorialRackTiles[3] = new Tile('L', 1);
    tutorialRackTiles[4] = new Tile('O', 1);
    tutorialRackTiles[5] = new Tile('W', 2);
    tutorialRackTiles[6] = new Tile('O', 1);

    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;

    welcomeContentTitel = "Welcome!";
    welcomeContentText =
        "Welcome to the tutorial of Scrabble Online! \n"
            + "In this tutorial you will learn how to use the UI. \n"
            + "First of all you will learn how to place a tile from your rack.";

    highlightedTilesTitle = "Your tiles are highligted now";
    higlightedTilesContentText =
        "First of all you have to draw tiles from the tilebag.\n"
            + "This is done automatically by the tilebag. \n"
            + "Your tiles are in your rack at the bottom of the screen.\n";

    highligthScrabbleboardPositionTitle = "Place a word with your tiles!";
    highligthScrabbleboardPositionContentText =
        "Now place HELLO at the highlighted area on the board. \n"
            + "To select a Tile you first have to click on it \n"
            + "or press D on your keyboard to highlight it.\n"
            + "Then you can click on the board to place it.";

    endGameTitle = "Congratulations!";
    endGameContentText =
        "You mastered the tutorial for the \n"
            + "UI of scrabble online. Now you can play a game with \n"
            + "someone else or against an AI. \n"
            + "Good bye and have a nice time with the game!";
  }

  /** starts the tutorial. */
  public void startTutorial() {
    startFlag = true;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;
    gameOver = false;
  }

  /** set the highlight tiles flag. */
  public void highligthTiles() {
    startFlag = false;
    highligthTilesFlag = true;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;
    gameOver = false;
  }

  /** set the highlightScrabbleboard flag. */
  public void highlightScrabbleboardPosition() {
    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = true;
    endFlag = false;
    gameOver = false;
  }

  /** set the end flag. */
  public void endFlag() {
    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = true;
    gameOver = false;
  }

  /** set the gameover flag. */
  public void setGameOver() {
    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;
    gameOver = true;
  }

  public TileBag getTutorialBag() {
    return tutorialBag;
  }

  public ScrabbleBoard getTutorialBoard() {
    return tutorialBoard;
  }

  public Tile[] getTutorialRackTiles() {
    return tutorialRackTiles;
  }

  public void setStartFlag(boolean startFlag) {
    this.startFlag = startFlag;
  }

  public void setHighligthTilesFlag(boolean highligthTilesFlag) {
    this.highligthTilesFlag = highligthTilesFlag;
  }

  public void setHighlightScrabbleboardPositionFlag(boolean highlightScrabbleboardPositionFlag) {
    this.highlightScrabbleboardPositionFlag = highlightScrabbleboardPositionFlag;
  }

  public void setEndFlag(boolean endFlag) {
    this.endFlag = endFlag;
  }

  public boolean getStartFlag() {
    return startFlag;
  }

  public boolean getHighligthTilesFlag() {
    return highligthTilesFlag;
  }

  public boolean getHighlightScrabbleboardPositionFlag() {
    return highlightScrabbleboardPositionFlag;
  }

  public boolean getEndFlag() {
    return endFlag;
  }

  public boolean isOver() {
    return gameOver;
  }
}
