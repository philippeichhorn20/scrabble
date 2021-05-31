package backend.basic;

import backend.music.Music;
import frontend.chat.Chat;
import frontend.screens.controllers.GameScreenController;

/**
 * GameInformation is a singleton class that was created with intent of transferring data between
 * different screens.
 *
 * @author mkolinsk
 */
public final class GameInformation {
  private Profile profile;
  private Player host;
  private Player[] players = new Player[4];
  private ClientMatch clientmatch;
  private Chat chat = new Chat();
  private Music music;

  /**
   * Starts playing music.
   *
   * @author vivanova
   *
   */
  public void startMusic() {
    music = new Music();
  }

  /**
   *  Returns music.
   *
   * @author vivanova */
  public Music getMusic() {
	  if(music == null) {
		  startMusic();
		  return music;
	  }
    return music;
  }

  public GameScreenController getGsc() {
    return gsc;
  }

  public void setGsc(GameScreenController gsc) {
    this.gsc = gsc;
  }

  private GameScreenController gsc;

  public Player getHost() {
    return host;
  }

  /**
   * Adds a player p into the Lobby. If 4 players are already in it does nothing.
   *
   *
   * @param p player that will be added.
   */
  public void addPlayer(Player p) {
    if (players[0] == null) {
      players[0] = p;
    } else if (players[1] == null) {
      players[1] = p;
    } else if (players[2] == null) {
      players[2] = p;
    } else if (players[3] == null) {
      players[3] = p;
    } else {
      System.out.println("4 Players already!");
    }
  }

  /**
   * Removes player from the players array - the lobby.
   *
   * @param player Name of the player that needs to be removed.
   */
  public void removePlayer(String player) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x] != null && this.players[x].name.equals(player)) {
        this.players[x] = null;
        break;
      }
    }
  }

  public void setHost(Player host) {
    this.host = host;
  }

  public Player[] getPlayers() {
    return players;
  }

  public void setPlayers(Player[] players) {
    this.players = players;
  }

  public ClientMatch getClientmatch() {
    return clientmatch;
  }

  public void setClientmatch(ClientMatch clientmatch) {
    this.clientmatch = clientmatch;
  }

  public ServerMatch getServermatch() {
    return servermatch;
  }

  public void setServermatch(ServerMatch servermatch) {
    this.servermatch = servermatch;
  }

  private ServerMatch servermatch;
  private static final GameInformation INSTANCE = new GameInformation();

  private GameInformation() {}

  public static GameInformation getInstance() {
    return INSTANCE;
  }

  public void setProfile(Profile p) {
    this.profile = p;
  }

  public Profile getProfile() {
    return this.profile;
  }

  public Chat getChat() {
    return this.chat;
  }
}
