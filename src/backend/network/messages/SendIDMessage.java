package backend.network.messages;

/*A message for the server to give away temporary ids
 * @author nilschae
 * @version 1.0*/
public class SendIDMessage extends Message {
  String id = "", tmpId = "";

  public SendIDMessage(String from, String id, String tmpId) {
    super(MessageType.SEND_ID, from);
    this.id = id;
    this.tmpId = tmpId;
  }

  public String getId() {
    return this.id;
  }

  public String getTmpId() {
    return this.tmpId;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public void setTmpId(String newTmpId) {
    this.tmpId = newTmpId;
  }
}
