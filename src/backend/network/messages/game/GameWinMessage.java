package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message which indicates that the game is won.
 *
 * @author nilschae
 */
public class GameWinMessage extends Message {

  public GameWinMessage(String from) {
    super(MessageType.GAME_WIN, from);
  }
}
