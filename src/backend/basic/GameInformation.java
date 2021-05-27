package backend.basic;

public final class GameInformation {
  private Profile profile;
  private Player host;
  private Player[] players = new Player[4];
  private ClientMatch clientmatch;

  public Player getHost() {
    return host;
  }
  public void addPlayer(Player p){
    if (players[0] == null) {
      players[0] = p;
      }
    else if(players[1]==null){
      players[1]=p;
    }else if(players[2]==null){
      players[2]=p;
    }else if(players[3]==null){
      players[3]=p;
    }else{
      System.out.println("4 Players already!");
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
