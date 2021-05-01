package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which indicates the beginning of a game
* @author nilschae
* @version 1.0*/
public class GameStartMessage extends Message {

  public GameStartMessage(String from) {
    super(MessageType.GAME_START, from);
  }
}
