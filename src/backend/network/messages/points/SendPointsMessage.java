package backend.network.messages.points;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which transfer points a player made with a turn
*  @author nilschae
*  @version 1.0*/
public class SendPointsMessage extends Message {
  int points = 0;

  public SendPointsMessage(String from, int amountOfPoints) {
    super(MessageType.SEND_POINTS, from);
    this.points = amountOfPoints;
  }
  public SendPointsMessage(String from) {
    super(MessageType.SEND_POINTS, from);
  }

  public int getPoints() {
    return this.points;
  }

  public void setPoints(int amountOfPoints) {
    this.points = amountOfPoints;
  }

  public void addPoints(int amountOfPoints) {
    this.points += amountOfPoints;
  }

  public void multiplyPoints(int multiplier) {
    this.points *= multiplier;
  }
}
