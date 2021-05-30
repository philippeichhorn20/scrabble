package backend.network.messages.tiles;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/** Message telling the game the player has passed the turn. */
public class PassMessage extends Message {

  public PassMessage(String from) {
    super(MessageType.PASS, from);
  }
}
