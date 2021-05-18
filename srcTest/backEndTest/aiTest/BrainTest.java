package backEndTest.aiTest;

import backend.ai.Brain;
import backend.basic.Matchfield;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class BrainTest {

  @Test
  void test() {
    ScrabbleBoard testBoard = new ScrabbleBoard();
    testBoard.setUpScrabbleBoard();
    testBoard.placeTile(new Tile('A',3, Tilestatus.ONBOARD),3,3);
    testBoard.placeTile(new Tile('B',4,Tilestatus.ONBOARD),3,1);
    Matchfield[] result = Brain.getNeighbors(testBoard,3,2);
    for(int i=0;i<4;i++) {
      if(result[i]!=null && result[i].hasTile()) {
        System.out.println(result[i].getTile().getLetter() + " "+ result[i].getTile().getX());
      }
    }
  }
}
