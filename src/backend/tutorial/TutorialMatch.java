package backend.tutorial;

import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.TileBag;

/*@author nilschae
* @version 1.0
* @description a Class which controlls the flow of the tutorial*/
public class TutorialMatch {
  TileBag tutorialBag;
  ScrabbleBoard tutorialBoard;
  Tile[] tutorialRackTiles = new Tile[7];
  boolean startFlag, highligthTilesFlag, highlightScrabbleboardPositionFlag, endFlag;

  public TutorialMatch() {
    tutorialBag = new TileBag();
    tutorialBoard = new ScrabbleBoard();

    tutorialRackTiles[0] = new Tile('H', 4);
    tutorialRackTiles[1] = new Tile('A', 1);
    tutorialRackTiles[2] = new Tile('L', 1);
    tutorialRackTiles[3] = new Tile('L', 1);
    tutorialRackTiles[4] = new Tile('O', 1);
    tutorialRackTiles[5] = new Tile('P', 2);
    tutorialRackTiles[6] = new Tile('O', 1);

    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;
  }

  public void startTutorial() {
    startFlag = true;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;
  }

  public void highligthTiles() {
    startFlag = false;
    highligthTilesFlag = true;
    highlightScrabbleboardPositionFlag = false;
    endFlag = false;
  }

  public void highlightScrabbleboardPosition() {
    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = true;
    endFlag = false;
  }

  public void endFlag() {
    startFlag = false;
    highligthTilesFlag = false;
    highlightScrabbleboardPositionFlag = false;
    endFlag = true;
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
}
