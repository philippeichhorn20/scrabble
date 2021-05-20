package backend.basic;

import backend.network.server.Server;
import backend.network.server.ServerSettings;

public class Lobby {

  ServerMatch match;
  Server server;
  Player[] players = new Player[4];
  Player hostPlayer;
  String ip = "";

  public Lobby(Player hostPlayer) {
    this.addPlayer(hostPlayer);
    this.hostPlayer = hostPlayer;
    ip = ServerSettings.getLocalHostIp4Address();
  }

  public void newMatch() {
    this.server.setServerMatch(new ServerMatch());
    this.server.getServerMatch().addServer(this.server);
    this.server.getServerMatch().startMatch();
  }

  public boolean addPlayer(Player player) {
    for (int i = 0; i < players.length; i++) {
      if (players[i] == null) {
        players[i] = player;
        return true;
      }
    }
    return false;
  }

  public void removePlayer(String player) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x].name.equals(player)) {
        this.players[x] = null;
      }
    }
  }

  public String getIp() {
    return this.ip;
  }

  public Server getServer() {
    return server;
  }

  public void setServer(Server server) {
    this.server = server;
  }
}
