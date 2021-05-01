package backend.network.messages.time;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which alerts the player for a specific time event
* @author nilschae
* @version 1.0*/
public class TimeAlertMessage extends Message {
  TimeAlertType alertType;

  public TimeAlertMessage(String from, TimeAlertType alertType) {
    super(MessageType.TIME_ALERT, from);
    this.alertType = alertType;
  }

  public TimeAlertType getAlertType() {
    return this.alertType;
  }

  public void setAlertType(TimeAlertType alertType) {
    this.alertType = alertType;
  }
}
