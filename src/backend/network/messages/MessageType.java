package backend.network.messages;

/*Types of Messages which can be sent
* @athor nilschae
* @version 1.0*/
public enum MessageType {
  CONNECT, DISCONNECT, CONNECTION_REFUSED, SERVER_SHUTDOWN,
  SEND_ID, GET_ID
}
