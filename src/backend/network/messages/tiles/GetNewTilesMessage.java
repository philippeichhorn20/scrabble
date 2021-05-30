package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message that is sent when a player demands new tiles, either at the start of the game or when
 * he places a word and needs new tiles.
 *
 * @author jawinter
 */
public class GetNewTilesMessage extends Message {

  private Tile[] tiles;

  /**
   * Constructor.
   *
   * @param from The name of the player that sends the message.
   * @param newTiles The new tiles.
   */
  public GetNewTilesMessage(String from, Tile[] newTiles) {
    super(MessageType.GET_NEW_TILES, from);
    tiles = newTiles;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public void setTiles(Tile[] newTiles) {
    this.tiles = newTiles;
  }
}
