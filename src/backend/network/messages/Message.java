package backend.network.messages;

import java.io.Serializable;

/*
* @author nilschae
* @description standart message for every other message to extend
* */
public class Message implements Serializable {
  private MessageType messageType;
  private String from;

  /*Constructor for standard message
  * @param type the type of message which will be send
  * @param from a string to save from who the message is*/
  public Message(MessageType type, String from) {
    this.messageType = type;
    this.from = new String(from);
  }

  public MessageType getMessageType() {
    return this.messageType;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String name){
    this.from = name;
  }

}
