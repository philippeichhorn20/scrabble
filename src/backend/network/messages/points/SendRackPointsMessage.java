package backend.network.messages.points;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which prompt the client to send the the points left on the rack when the game is over
* @author nilschae
* @version 1.0*/
public class SendRackPointsMessage extends Message {

  public SendRackPointsMessage(String from) {
    super(MessageType.SEND_RACK_POINTS, from);
  }
}
