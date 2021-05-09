package backend.basic;

import backend.network.client.ClientProtocol;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.messages.tiles.ShuffleTilesMessage;
import java.io.IOException;

/*
  @peichhor
  this class represents a Player
 */
public class ClientMatch {

  private static final int round = 0;
  private final Player[] players;
  private final Player player;
  private final ClientProtocol protocol;
  private final String from;
  private ScrabbleBoard scrabbleBoard;
  private int points = 0;

  public ClientMatch(ClientProtocol protocol, Player[] players, String from, Player player) {
    protocol.run();
    this.player = player;
    this.protocol = protocol;
    this.players = players;
    this.from = from;
  }


  public void placeTile(Tile tile, int x, int y) throws IOException {
    ScrabbleBoard.placeTile(tile, x, y);

    protocol.sendToServer(new PlaceTilesMessage(from, tile));

  }

  public void receiveShuffleTile(ReceiveShuffleTilesMessage message) {
    player.updateRack(message.getTilesBefore(), message.getTilesAfter());
  }

  public void shuffleTiles(Tile[] tiles) throws IOException {
    protocol.sendToServer(new ShuffleTilesMessage(from, tiles));
  }

  public String submitTilesOfClient() throws IOException {
    String[][] result = ScrabbleBoard.submitTiles();
    boolean isValid = ScrabbleBoard.inputValudation(result);
    String resultString = "";
    if (isValid) {
      resultString += "the following valid words were added:\n";
      for (int i = 0; i < result[0].length; i++) {
        resultString += result[0][i] + ", explanation: " + result[1][i] + "\n";
      }
      this.points = ScrabbleBoard.getPoints();
      resultString += "they were worth a whopping" + ScrabbleBoard.getPoints()
          + " ! You are now up to " + this.points + " points.";
      ScrabbleBoard.nextTurn();
      protocol.sendToServer(new GameTurnMessage(from));
    } else {
      resultString += "what the heck do you mean by ";
      for (int i = 0; i < result[0].length; i++) {
        if (result[1][i] == "") {
          resultString += result[0][i] + ", ";
        }
      }
    }
    return resultString;
  }

  public void endMatch() {
    protocol.disconnect();
  }
}
