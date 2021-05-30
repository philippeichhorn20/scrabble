package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message sent by a player that sends his tiles to be changed to new ones from the bag.
 *
 * @author nilschae
 */
public class ShuffleTilesMessage extends Message {

  Tile[] toShuffleTiles;

  /**
   * Constructor.
   *
   * @param from The name of the user that send the message.
   * @param toShuffleTiles Tiles that will be exchanged.
   */
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
