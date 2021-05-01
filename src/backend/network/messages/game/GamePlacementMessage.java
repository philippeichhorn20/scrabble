package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/* Message to show which placement a player have at the end of the game
* @author nilschae
* @version 1.0*/
public class GamePlacementMessage extends Message {
  int placement;
  int playerNumber;

  public GamePlacementMessage(String from, int placement, int playerNumber) {
    super(MessageType.GAME_PLACEMENT, from);
    this.placement = placement;
    this.playerNumber = playerNumber;
  }

  public int getPlacement() {
    return this.placement;
  }

  public int getPlayerNumber() {
    return this.playerNumber;
  }

  public void setPlacement(int placement) {
    this.placement = placement;
  }

  public void setPlayerNumber(int playerNumber) {
    this.playerNumber = playerNumber;
  }
}
