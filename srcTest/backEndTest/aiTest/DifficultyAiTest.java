package backEndTest.aiTest;

import static backend.ai.PlayerAI.getRandomLetter;

import backend.ai.EasyAI;
import backend.ai.HardAI;
import backend.ai.PlayerAI;
import backend.basic.GameInformation;
import backend.basic.Player.Playerstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;
import backend.network.client.AIProtocol;
import backend.network.server.Server;
import backend.network.server.ServerSettings;
import org.junit.jupiter.api.Test;
import backend.ai.PlayerAI;


public class DifficultyAiTest {

  @Test
  void testHardEasy() {
    /*EasyAI easyAI = new EasyAI("easyAI");
    HardAI hardAI = new HardAI("hardAI");

    Server server = new Server();
    Runnable r = new Runnable() {
      @Override
      public void run() {
        server.listen();
      }
    };
    new Thread(r).start();
    AIProtocol easyAiProtocol = new AIProtocol(ServerSettings.getLocalHostIp4Address(),ServerSettings.port,"easyBoi");
    AIProtocol hardAiProtocol = new AIProtocol(ServerSettings.getLocalHostIp4Address(),ServerSettings.port,"hardBoi");
*/
    //server.stopServer();
  }

}
