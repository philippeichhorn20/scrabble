package backend.network.messages;

/*A Message to show that the connection is refused
* @author nilschae
* @version 1.0*/
public class ConnectionRefusedMessage extends Message{
  String errorMessage = "";

  public ConnectionRefusedMessage(String from, String error) {
    super(MessageType.CONNECTION_REFUSED, from);
    errorMessage = error;
  }
}
