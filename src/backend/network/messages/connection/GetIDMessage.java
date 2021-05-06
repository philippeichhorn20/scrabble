package backend.network.messages.connection;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/* A message to request an ID from the server
 * @author nilschae
 * @version 1.0*/
public class GetIDMessage extends Message {
  String tmpId = "";

  public GetIDMessage(String from, String tmpId) {
    super(MessageType.GET_ID, from);
    this.tmpId = tmpId;
  }

  public String getTmpId() {
    return this.tmpId;
  }

  public void setTmpId(String newTmpId) {
    this.tmpId = newTmpId;
  }
}
