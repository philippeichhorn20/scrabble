package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which gives the player the shuffeld tiles
* @author nilschae
* @version 1.0*/
public class ReceiveShuffleTilesMessage extends Message {
  Tile[] tiles;

  public ReceiveShuffleTilesMessage(String from, Tile[] shuffledTiles) {
    super(MessageType.RECEIVE_SHUFFLE_TILES, from);
    this.tiles = shuffledTiles;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public void setTiles(Tile[] shuffledTiles) {
    this.tiles = shuffledTiles;
  }
}
