package backend.network.messages.text;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * This message sends a history message, which will be shown on the log panel.
 *
 * @author mkolinsk
 */
public class HistoryMessage extends Message {
  String mess;

  public HistoryMessage(String from, String message) {
    super(MessageType.HISTORY, from);
    this.mess = message;
  }

  /**
   * returns the message.
   *
   * @return the message
   */
  public String getMessage() {
    return mess;
  }

  /**
   * Sets the message.
   *
   * @param mess The message
   */
  public void setMessage(String mess) {
    this.mess = mess;
  }
}
