package backend.network.messages.connection;

import backend.basic.Player;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message to request connection to the server
 * @author nilschae
 * @version 1.0*/
public class ConnectMessage extends Message {

  private Player player;
  public ConnectMessage(String from, Player player) {
    super(MessageType.CONNECT, from);
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
