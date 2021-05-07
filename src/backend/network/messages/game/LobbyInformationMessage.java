package backend.network.messages.game;

import backend.basic.Player;
import backend.basic.ServerMatch;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*
Message which gives the information about players to the clients
 * @author peichhor
 * @version 1.0*/

public class LobbyInformationMessage extends Message {

  Player[] players;

  public LobbyInformationMessage(String from) {
    super(MessageType.GAME_INFO, from);
    players = ServerMatch.getPlayers();
  }

}
