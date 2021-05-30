package backend.network.messages;

import java.io.Serializable;

/**
 * Standard message that mostly gets overwritten by specific message types.
 *
 * @author nilschae
 */
public class Message implements Serializable {
  private MessageType messageType;
  private String from;

  /**
   * Standard constructor.
   *
   * @param type Type of the message.
   * @param from The name of the user who sent the message.
   */
  public Message(MessageType type, String from) {
    this.messageType = type;
    this.from = new String(from);
  }

  public MessageType getMessageType() {
    return this.messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String name) {
    this.from = name;
  }
}
