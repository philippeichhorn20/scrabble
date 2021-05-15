package backEndTest.basicTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.basic.ServerMatch;
import backend.basic.Tile;
import org.junit.jupiter.api.Test;

public class ServerMatchTest {
  @Test
  void test() {
    Player testPlayer = new Player("testName","testColor", Playerstatus.TURN);
    ServerMatch testServerMatch = new ServerMatch(testPlayer);
  Tile[] testDrawNewTiles = testServerMatch.drawNewTiles(5);
  assertEquals(5, testDrawNewTiles);
  }


}
