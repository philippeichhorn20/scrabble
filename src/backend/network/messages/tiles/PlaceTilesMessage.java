package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which prompt the server to send to every client which tiles a player placed
* @author nilschae
 * @version 1.0*/
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
