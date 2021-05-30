package backend.network.messages.tiles;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message which provides the player with new tiles from the bag.
 *
 * @author nilschae
 */
public class ReceiveShuffleTilesMessage extends Message {

  Tile[] rack;

  public ReceiveShuffleTilesMessage(String from, Tile[] rack) {
    super(MessageType.RECEIVE_SHUFFLE_TILES, from);
    this.rack = rack;
  }

  public Tile[] getRack() {
    return rack;
  }

  public void setRack(Tile[] rack) {
    this.rack = rack;
  }

  @Override
  public String getFrom() {
    return super.getFrom();
  }

  @Override
  public void setFrom(String name) {
    super.setFrom(name);
  }
}
