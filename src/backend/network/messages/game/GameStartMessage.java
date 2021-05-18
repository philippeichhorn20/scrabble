package backend.network.messages.game;

import backend.basic.Tile;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which indicates the beginning of a game
* @author nilschae
 * @version 1.0*/
public class GameStartMessage extends Message {

  Tile[] tiles;

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
