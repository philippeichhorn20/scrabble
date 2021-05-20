package backend.ai;


import backend.basic.Matchfield;
import backend.basic.ScrabbleBoard;

/*
@auhtor jawinter
This class helps dealing with the handling of available possibilities
to place Tiles and create words. xPos and yPos tell the position of
available Tilefield and the different spaces hold the number of empty
Matchfields in given direction from current.
 */
public class WordPossibility {

  private int xPos;
  private int yPos;
  private char letter;
  private int aboveSpace;
  private int rightSpace;
  private int belowSpace;
  private int leftSpace;
  private ScrabbleBoard board;

  //Instructor for initialization
  public WordPossibility(char givenLetter,int xPosition, int yPosition, ScrabbleBoard scrabbleBoard) {
    this.letter = givenLetter;
    if (xPosition < 15 && xPosition >= 0) {
      this.xPos = xPosition;
    } else {
      this.xPos = 0;
    }
    if (yPosition < 15 && yPosition >= 0) {
      this.yPos = yPosition;
    } else {
      this.yPos = 0;
    }
    this.board = scrabbleBoard;
    calculateSpaces();
  }

  //Method to calculate available empty spaces for each direction
  public void calculateSpaces() {
    Matchfield[][] scrabbleBoard = board.getScrabbleBoard();
    int x = this.xPos;
    int y = this.yPos;
    boolean aboveDone = false;
    boolean rightDone = false;
    boolean belowDone = false;
    boolean leftDone = false;
    for (int i = 0; i < 15; i++) {
      if ((y - (i + 1) < 0 || scrabbleBoard[x][y - (i + 1)].hasTile()) && !aboveDone) {
        aboveSpace = i;
        aboveDone = true;
      }
      if ((x + i >= 14 || scrabbleBoard[x + i + 1][y].hasTile()) && !rightDone) {
        rightSpace = i;
        rightDone = true;
      }
      if ((y + i >= 14 || scrabbleBoard[x][y + i + 1].hasTile()) && !belowDone) {
        belowSpace = i;
        belowDone = true;
      }
      if ((x - (i + 1) < 0 || scrabbleBoard[x - (i + 1)][y].hasTile()) && !leftDone) {
        leftSpace = i;
        leftDone = true;
      }
    }
  }

  public char getLetter() {
    return letter;
  }

  public int getxPos() {
    return xPos;
  }

  public int getyPos() {
    return yPos;
  }

  public int getAboveSpace() {
    return aboveSpace;
  }

  public int getBelowSpace() {
    return belowSpace;
  }

  public int getLeftSpace() {
    return leftSpace;
  }

  public int getRightSpace() {
    return rightSpace;
  }

  @Override
  public String toString() {
    return "WordPossibility{" +
        "xPos=" + xPos +
        ", yPos=" + yPos +
        ", aboveSpace=" + aboveSpace +
        ", rightSpace=" + rightSpace +
        ", belowSpace=" + belowSpace +
        ", leftSpace=" + leftSpace +
        ", board=" + board +
        '}';
  }
}
