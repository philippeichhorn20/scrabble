package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which ask the server to shuffle the tiles of the player
 * @author nilschae
 * @version 1.0*/
public class ShuffleTilesMessage extends Message {

  Tile[] toShuffleTiles;


  public ShuffleTilesMessage(String from, Tile[] toShuffleTiles) {
    super(MessageType.SHUFFLE_TILES, from);
    this.toShuffleTiles = toShuffleTiles;

  }

  public Tile[] getToShuffleTiles() {
    return toShuffleTiles;
  }

  public void setToShuffleTiles(Tile[] saveTiles) {
    this.toShuffleTiles = saveTiles;
  }
}
