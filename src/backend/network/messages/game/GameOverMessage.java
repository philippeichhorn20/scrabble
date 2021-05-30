package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;
/**
 * Message which indicates the end of a game.
 *
 *  @author nilschae
 *
 */

public class GameOverMessage extends Message {

  /**
   * Constructor.
   *
   * @param from The user's name that sent it.
   */
  public GameOverMessage(String from) {
    super(MessageType.GAME_OVER, from);
  }
}
