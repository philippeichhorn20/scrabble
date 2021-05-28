package backend.network.messages.text;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

public class HistoryMessage extends Message {
  String mess;

  public HistoryMessage(String from, String message) {
    super(MessageType.HISTORY, from);
    this.mess = message;
  }

  public String getMessage() {
    return mess;
  }
  public void setMessage(String mess){
    this.mess = mess;
  }
}
