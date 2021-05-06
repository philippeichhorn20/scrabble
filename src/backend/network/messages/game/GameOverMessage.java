package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

public class GameOverMessage extends Message {

  /* Message which indicates the end of a game
  * @author nilschae
  * @version 1.0*/
  public GameOverMessage(String from) {
    super(MessageType.GAME_OVER, from);
  }
}
