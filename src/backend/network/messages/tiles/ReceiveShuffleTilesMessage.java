package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which gives the player the shuffeld tiles
 * @author nilschae
 * @version 1.0*/
public class ReceiveShuffleTilesMessage extends Message {

  Tile[] tilesBefore;
  Tile[] tilesAfter;

  public ReceiveShuffleTilesMessage(String from, Tile[] shuffledTilesBefore,
      Tile[] shuffledTilesAfter) {
    super(MessageType.RECEIVE_SHUFFLE_TILES, from);
    this.tilesBefore = shuffledTilesBefore;
    this.tilesAfter = shuffledTilesBefore;
  }

  public Tile[] getTilesBefore() {
    return this.tilesBefore;
  }

  public void setTilesBefore(Tile[] shuffledTiles) {
    this.tilesBefore = shuffledTiles;
  }

  public Tile[] getTilesAfter() {
    return this.tilesAfter;
  }

  public void setTilesAfter(Tile[] shuffledTiles) {
    this.tilesAfter = shuffledTiles;
  }
}
