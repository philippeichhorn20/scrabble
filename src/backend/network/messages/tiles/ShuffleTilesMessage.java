package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which ask the server to shuffle the tiles of the player
* @author nilschae
* @version 1.0*/
public class ShuffleTilesMessage extends Message {
  Tile[] tiles;

  public ShuffleTilesMessage(String from, Tile[] tilesToShuffle) {
    super(MessageType.SHUFFLE_TILES, from);
    this.tiles = tilesToShuffle;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public void setTiles(Tile[] tilesToShuffle) {
    this.tiles = tilesToShuffle;
  }
}
