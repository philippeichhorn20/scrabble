package backEndTest.aiTest;

import backend.ai.Brain;
import backend.ai.EasyAI;
import backend.ai.WordPossibility;
import backend.basic.Matchfield;
import backend.basic.Player.Playerstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BrainTest {

  @Test
  void test() {
    ScrabbleBoard testBoard = new ScrabbleBoard();
    testBoard.setUpScrabbleBoard();
    testBoard.placeTile(new Tile('A', 3, Tilestatus.ONBOARD), 3, 3);
    testBoard.placeTile(new Tile('B', 4, Tilestatus.ONBOARD), 3, 1);
    testBoard.placeTile(new Tile('C', 4, Tilestatus.ONBOARD), 0, 1);
    testBoard.placeTile(new Tile('A', 3, Tilestatus.ONBOARD), 7, 8);
    testBoard.placeTile(new Tile('P', 3, Tilestatus.ONBOARD), 8, 8);
    testBoard.placeTile(new Tile('F', 3, Tilestatus.ONBOARD), 9, 8);
    testBoard.placeTile(new Tile('E', 3, Tilestatus.ONBOARD), 10, 8);
    testBoard.placeTile(new Tile('L', 3, Tilestatus.ONBOARD), 11, 8);

    Matchfield[] result = Brain.getNeighbors(testBoard, 3, 2);
    Matchfield[] result2 = Brain.getNeighbors(testBoard, 13, 13);
    for (int i = 0; i < 4; i++) {
      if (result[i] != null && result[i].hasTile()) {
        System.out.print(result[i].getTile().getLetter() + " " + result[i].getTile().getX() + " ");
      }
    }
    System.out.println(" Neighbors of second Try");
    for (int i = 0; i < 4; i++) {
      if (result2[i] != null && result2[i].hasTile()) {
        System.out.print(result2[i].getTile().getLetter() + " " + result2[i].getTile().getX());
      }
    }
    Matchfield[] emptyList = new Matchfield[4];
    EasyAI firstAI = new EasyAI("computer1", "#000000", Playerstatus.WAIT);
    ArrayList<WordPossibility> list = firstAI.easyBrain.getWordPossibilities(testBoard);

    System.out.println(list.size());
    testBoard.printScrabbleBoard();
    WordPossibility wordP = new WordPossibility(3, 2, testBoard);
    WordPossibility wordP2 = new WordPossibility(0, 0, testBoard);
    WordPossibility wordP3 = new WordPossibility(12, 8, testBoard);
    WordPossibility wordP4 = new WordPossibility(14, 14, testBoard);
    WordPossibility wordP5 = new WordPossibility(0, 14, testBoard);
    WordPossibility wordP6 = new WordPossibility(14, 0, testBoard);
    WordPossibility wordP7 = new WordPossibility(10, 7, testBoard);
  }
}
