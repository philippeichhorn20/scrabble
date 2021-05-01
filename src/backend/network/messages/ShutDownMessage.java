package backend.network.messages;

public class ShutDownMessage extends Message{


  public ShutDownMessage(String from) {
    super(MessageType.SERVER_SHUTDOWN, from);
  }
}
