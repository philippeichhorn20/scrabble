package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which indicates that a player has won
* @author nilschae
* @version 1.0*/
public class GameWinMessage extends Message {

  public GameWinMessage(String from) {
    super(MessageType.GAME_WIN, from);
  }
}
