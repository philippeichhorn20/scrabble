package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message which sends an array of tiles that were placed by the player to other players.
 *
 * @author nilschae
 */
public class PlaceTilesMessage extends Message {

  Tile[] tiles;

  public PlaceTilesMessage(String from, Tile[] tilesToPlace) {
    super(MessageType.PLACE_TILES, from);
    this.tiles = tilesToPlace;
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public void setTiles(Tile[] tilesToPlace) {
    this.tiles = tilesToPlace;
  }
}
