package backend.network.messages.tiles;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*
Message telling game a player wants to pass his move.
 */

public class PassMessage extends Message {

  public PassMessage(MessageType type, String from) {
    super(MessageType.PASS, from);
  }
}
