package backend.network.messages.game;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*Message which indicates when it is a players turn
* @author nilschae
* @version 1.0*/
public class GameTurnMessage extends Message {

  int nowTurn; //indicates which players turn it is (in order of array)

  public GameTurnMessage(String from, int nowTurn) {
    super(MessageType.GAME_TURN, from);
    this.nowTurn = nowTurn;
  }
}
