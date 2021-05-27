package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*
Message (used when player demands new tiles or placed tiles) provides player needed amount of tiles.
@author jawinter
 */
public class GetNewTilesMessage extends Message {

  private Tile[] tiles;
  /*
  @param newTiles will be filled with the tiles given to player
   */
  public GetNewTilesMessage(String from,Tile[] newTiles) {
    super(MessageType.GET_NEW_TILES, from);
  }

  public Tile[] getTiles() {
    return this.tiles;
  }

  public void setTiles(Tile[] newTiles) {
    this.tiles = newTiles;
  }
}
