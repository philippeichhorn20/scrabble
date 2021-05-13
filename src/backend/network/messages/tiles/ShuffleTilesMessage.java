package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which ask the server to shuffle the tiles of the player
 * @author nilschae
 * @version 1.0*/
public class ShuffleTilesMessage extends Message {

  Tile[] oldTiles;
  Tile[] saveTiles;

  public ShuffleTilesMessage(String from, Tile[] oldTiles, Tile[] saveTiles) {
    super(MessageType.SHUFFLE_TILES, from);
    this.oldTiles = oldTiles;
    this.saveTiles = saveTiles;
  }

  public Tile[] getOldTiles() {
    return oldTiles;
  }

  public void setOldTiles(Tile[] oldTiles) {
    this.oldTiles = oldTiles;
  }

  public Tile[] getSaveTiles() {
    return saveTiles;
  }

  public void setSaveTiles(Tile[] saveTiles) {
    this.saveTiles = saveTiles;
  }
}
