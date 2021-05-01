package backend.network.messages.connection;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*A Message to inform the server that a client disconnects
* @author nilschae
* @version 1.0*/
public class DisconnectMessage extends Message {

  public DisconnectMessage(String from) {
    super(MessageType.DISCONNECT, from);
  }
}
