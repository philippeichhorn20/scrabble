package backEndTest.aiTest;

import backend.ai.EasyAI;
import backend.ai.PossibleWord;
import backend.basic.Player.Playerstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrainTest {

  ScrabbleBoard board = new ScrabbleBoard();
  EasyAI easyAI = new EasyAI("AiPlayer");
  Tile[] testHand1 = new Tile[7];
  Tile[] testHand2 = new Tile[7];
  Tile[] testHand3 = new Tile[7];

  @BeforeEach
  void prepare() {
    board.placeTile(new Tile('H', 4, Tilestatus.ONBOARD), 9, 1);
    board.placeTile(new Tile('D', 2, Tilestatus.ONBOARD), 12, 1);
    board.placeTile(new Tile('E', 1, Tilestatus.ONBOARD), 9, 2);
    board.placeTile(new Tile('R', 1, Tilestatus.ONBOARD), 12, 2);
    board.placeTile(new Tile('O', 1, Tilestatus.ONBOARD), 13, 2);
    board.placeTile(new Tile('E', 1, Tilestatus.ONBOARD), 14, 2);
    board.placeTile(new Tile('D', 2, Tilestatus.ONBOARD), 9, 3);
    board.placeTile(new Tile('A', 1, Tilestatus.ONBOARD), 12, 3);
    board.placeTile(new Tile('G', 2, Tilestatus.ONBOARD), 9, 4);
    board.placeTile(new Tile('R', 1, Tilestatus.ONBOARD), 10, 4);
    board.placeTile(new Tile('A', 1, Tilestatus.ONBOARD), 11, 4);
    board.placeTile(new Tile('Y', 4, Tilestatus.ONBOARD), 12, 4);
    board.placeTile(new Tile('P', 3, Tilestatus.ONBOARD), 6, 5);
    board.placeTile(new Tile('U', 1, Tilestatus.ONBOARD), 7, 5);
    board.placeTile(new Tile('C', 3, Tilestatus.ONBOARD), 8, 5);
    board.placeTile(new Tile('E', 1, Tilestatus.ONBOARD), 9, 5);
    board.placeTile(new Tile('E', 1, Tilestatus.ONBOARD), 6, 6);
    board.placeTile(new Tile('P', 3, Tilestatus.ONBOARD), 6, 7);
    board.placeTile(new Tile('O', 1, Tilestatus.ONBOARD), 7, 7);
    board.placeTile(new Tile('O', 1, Tilestatus.ONBOARD), 8, 7);
    board.placeTile(new Tile('R', 1, Tilestatus.ONBOARD), 9, 7);
    board.placeTile(new Tile('H', 4, Tilestatus.ONBOARD), 8, 8);
    board.placeTile(new Tile('A', 1, Tilestatus.ONBOARD), 9, 8);
    board.placeTile(new Tile('R', 1, Tilestatus.ONBOARD), 10, 8);
    board.placeTile(new Tile('D', 2, Tilestatus.ONBOARD), 11, 8);
    board.placeTile(new Tile('I', 1, Tilestatus.ONBOARD), 9, 9);
    board.placeTile(new Tile('O', 1, Tilestatus.ONBOARD), 11, 9);
    board.placeTile(new Tile('A', 1, Tilestatus.ONBOARD), 7, 10);
    board.placeTile(new Tile('S', 1, Tilestatus.ONBOARD), 8, 10);
    board.placeTile(new Tile('S', 1, Tilestatus.ONBOARD), 9, 10);
    board.placeTile(new Tile('U', 1, Tilestatus.ONBOARD), 10, 10);
    board.placeTile(new Tile('R', 1, Tilestatus.ONBOARD), 11, 10);
    board.placeTile(new Tile('E', 1, Tilestatus.ONBOARD), 12, 10);
    board.placeTile(new Tile('V', 4, Tilestatus.ONBOARD), 7, 11);
    board.placeTile(new Tile('I', 1, Tilestatus.ONBOARD), 9, 11);
    board.placeTile(new Tile('A', 1, Tilestatus.ONBOARD), 3, 12);
    board.placeTile(new Tile('L', 1, Tilestatus.ONBOARD), 4, 12);
    board.placeTile(new Tile('I', 1, Tilestatus.ONBOARD), 5, 12);
    board.placeTile(new Tile('B', 3, Tilestatus.ONBOARD), 6, 12);
    board.placeTile(new Tile('I', 1, Tilestatus.ONBOARD), 7, 12);
    board.placeTile(new Tile('N', 1, Tilestatus.ONBOARD), 9, 12);
    board.placeTile(new Tile('I', 1, Tilestatus.ONBOARD), 10, 12);
    board.placeTile(new Tile('L', 1, Tilestatus.ONBOARD), 11, 12);
    board.placeTile(new Tile('D', 2, Tilestatus.ONBOARD), 7, 13);

    Tile n = new Tile('N', 1);
    Tile g = new Tile('G', 2);
    Tile v = new Tile('V', 4);
    Tile m = new Tile('M', 3);
    Tile k = new Tile('K', 5);
    Tile i = new Tile('I', 1);
    Tile c = new Tile('C', 3);
    Tile a = new Tile('A', 1);
    Tile b = new Tile('B', 3);
    Tile d = new Tile('D', 2);
    Tile e = new Tile('E', 1);
    Tile f = new Tile('F', 4);
    Tile s = new Tile('S', 1);
    Tile[] fff = new Tile[]{n, g, v, m, k, i, c};
    testHand1 = new Tile[]{a, b, c, d, e, f, g};
    testHand2 = new Tile[]{i, m, k, a, e, c, s};
    testHand3 = new Tile[]{n, a, f, g, g, a, s};
    easyAI.setTiles(fff);
  }

  @Test
  void testPointsCalculation() {
    easyAI.getEasyBrain().setScrabbleboard(board);
    TreeSet<PossibleWord> wordsTest = easyAI.getEasyBrain().getPlayableWords(easyAI.getTiles());
    Iterator<PossibleWord> it = wordsTest.iterator();
    PossibleWord ginch = new PossibleWord("", 0, new ArrayList<Tile>());
    int ginchPoints = 0;
    while (it.hasNext()) {
      String z = it.next().getWord();
      System.out.println(z);
      if (z.equals("GINCH")) {
        ginchPoints = ginch.getPoints();
      }
    }
    Assert.assertEquals(33, ginchPoints);
  }

  @Test
  void testIfFirstMove() {
    ScrabbleBoard newBoard = new ScrabbleBoard();
    easyAI.getEasyBrain().setScrabbleboard(newBoard);
    Tile[][] tileHands = new Tile[][]{testHand1, testHand2, testHand3};
    for (int i = 0; i < tileHands.length; i++) {
      System.out.println("Try with testHand number" + (i + 1) + " ");
      easyAI.setTiles(tileHands[i]);
      TreeSet<PossibleWord> firstMove = easyAI.getEasyBrain().getPlayableWords(easyAI.getTiles());
      Assert.assertTrue(firstMove.size() > 0);
      Iterator<PossibleWord> it = firstMove.iterator();
      while (it.hasNext()) {
        System.out.println(it.next());
      }
    }

    TreeSet<PossibleWord> firstMove = easyAI.getEasyBrain().getPlayableWords(easyAI.getTiles());
    Assert.assertTrue(firstMove.size() > 0);
    Iterator<PossibleWord> it = firstMove.iterator();
    while (it.hasNext()) {
      System.out.println(it.next());
    }
  }
}
