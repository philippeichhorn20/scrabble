package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which indicates that it's some other players turn
* @author nilschae
* @version 1.0*/
public class GameWaitMessage extends Message {

  public GameWaitMessage(String from) {
    super(MessageType.GAME_WAIT, from);
  }
}
