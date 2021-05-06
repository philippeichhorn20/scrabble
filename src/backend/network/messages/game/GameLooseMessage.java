package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which indicates that a player has lost
* @author nilschae
* @version 1.0*/
public class GameLooseMessage extends Message {

  public GameLooseMessage(String from) {
    super(MessageType.GAME_LOOSE, from);
  }
}
