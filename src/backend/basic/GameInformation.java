package backend.basic;

public final class GameInformation {
  private Profile profile;
  private Player host;
  private Player[] players;
  private ClientMatch clientmatch;

  public Player getHost() {
    return host;
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
  private final static GameInformation INSTANCE = new GameInformation();


  private GameInformation() {}

  public static GameInformation getInstance(){
    return INSTANCE;
  }
  public void setProfile(Profile p){
    this.profile = p;
  }
  public Profile getProfile() {
    return this.profile;
  }
}
