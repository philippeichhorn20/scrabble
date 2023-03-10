package backend.network.messages.time;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message which alerts the player of a specific time event.
 *
 * @author nilschae
 */
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
