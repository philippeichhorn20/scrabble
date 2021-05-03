package backend.network.messages.connection;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

public class ShutDownMessage extends Message {

  public ShutDownMessage(String from) {
    super(MessageType.SERVER_SHUTDOWN, from);
  }
}
