package backend.network.messages.game;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message which indicates the start of the game.
 *
 * @author nilschae
 */
public class GameStartMessage extends Message {

  private Tile[] tiles;

  public GameStartMessage(String from, Tile[] tiles) {
    super(MessageType.GAME_START, from);
    this.tiles = tiles;
  }

  @Override
  public String getFrom() {
    return super.getFrom();
  }

  @Override
  public void setFrom(String name) {
    super.setFrom(name);
  }

  public Tile[] getTiles() {
    return tiles;
  }

  public void setTiles(Tile[] tiles) {
    this.tiles = tiles;
  }
}
