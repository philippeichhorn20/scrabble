package backend.network.messages.game;

import backend.basic.Player;
import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * A message which sends out information about the players in the lobby.
 *
 * @author nilschae
 */
public class LobbyInformationMessage extends Message {

  Player[] players;

  public LobbyInformationMessage(String from, Player[] players) {
    super(MessageType.GAME_INFO, from);
    this.players = players;
  }

  public Player[] getPlayers() {
    return players;
  }
}
