package backend.basic;

import backend.network.server.Server;
import backend.network.server.ServerSettings;

public class Lobby {

  ServerMatch match;
  Server server;
  Player[] players = new Player[4];
  String ip = "";

  public Lobby(Player hostPlayer) {
    server = new Server();
    this.addPlayer(hostPlayer);
    ip = ServerSettings.getLocalHostIp4Address();
    server = new Server();
    Runnable r = new Runnable(){
      public void run(){
        server.listen();
      }
    };
    new Thread(r).start();
  }

  public void newMatch() {
    match = new ServerMatch(players);
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

  public String getIp() {
    return this.ip;
  }

  public Server getServer() {
    return server;
  }
}
