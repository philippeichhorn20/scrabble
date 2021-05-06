package backend.network.messages.connection;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message to request connection to the server
 * @author nilschae
 * @version 1.0*/
public class ConnectMessage extends Message {

  public ConnectMessage(String from) {
    super(MessageType.CONNECT, from);
  }
}
