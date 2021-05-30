package backend.network.messages.time;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * Message to prompt the client to synchronize the time a player has left in their turn with the
 * server's.
 *
 * @author nilschae
 */
public class TimeSyncMessage extends Message {
  double time;

  public TimeSyncMessage(String from, double time) {
    super(MessageType.TIME_SYNC, from);
    this.time = time;
  }

  public double getTime() {
    return time;
  }

  public void setTime(double time) {
    this.time = time;
  }
}
