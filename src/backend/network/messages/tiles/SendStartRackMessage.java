package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*
Message, which sends random tiles to player at the start of the game.
@author jawinter
 */
public class SendStartRackMessage extends Message {

  private Tile[] tiles;

  public SendStartRackMessage(String from, Tile[] startingTiles) {
    super(MessageType.SEND_START_RACK, from);
    this.tiles = startingTiles;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public void setTiles(Tile[] startingTiles) {
    this.tiles = startingTiles;
  }
}
