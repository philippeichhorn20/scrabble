package backEndTest.aiTest;

import static backend.ai.PlayerAI.getRandomLetter;

import backend.ai.EasyAI;
import backend.ai.HardAI;
import backend.ai.PlayerAI;
import backend.basic.GameInformation;
import backend.basic.Player;
import backend.basic.Player.Playerstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.ServerMatch;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import backend.network.client.AIProtocol;
import backend.network.server.Server;
import backend.network.server.ServerSettings;
import frontend.Main;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import backend.ai.PlayerAI;

/**
 * Test should empirically prove which AI is stronger in terms of scrabble playing
 */
public class DifficultyAiTest {

  @Test
  /**
   * Let easy AI play against hard AI several times
   */
  void testHardEasy() {
    EasyAI easyAI = new EasyAI("easyAI");
    HardAI hardAI = new HardAI("hardAI");

    Server server = new Server();
    Runnable r = new Runnable() {
      @Override
      public void run() {
        server.listen();
      }
    };
    new Thread(r).start();
    ServerMatch serverMatch = new ServerMatch(server);
    GameInformation.getInstance().setServermatch(serverMatch);


    AIProtocol easyAiProtocol = new AIProtocol(ServerSettings.getLocalHostIp4Address(),ServerSettings.port,"easyBoi",easyAI);
    AIProtocol hardAiProtocol = new AIProtocol(ServerSettings.getLocalHostIp4Address(),ServerSettings.port,"hardBoi",hardAI);


    easyAiProtocol.start();
    hardAiProtocol.start();
    for(int i = 0;i<100000;i++) {
      System.out.print("");
    }
    serverMatch.startMatch();






    //server.stopServer();
  }

}
