package backend.basic;

import backend.network.messages.game.GameStartMessage;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.points.SendPointsMessage;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.tiles.GetNewTilesMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.server.Server;
import backend.network.server.ServerProtocol;
import java.io.IOException;

/*
@author peichhor
@version 1.0
@param players: the players that are participating in the game
@param scrabbleboard: an instance of the physical scrabble board with some additions
to represent the actions of the current turn
@param tileBag the bag that conatins the tiles that were not pulled yet
@param currentPlayer conatins the number in the players array which
points at the player who's turn it is
@param roundNum number of the current round
 */

public class ServerMatch {

  private final TileBag tileBag = new TileBag();
  private final int round = 0;
  private final ScrabbleBoard scrabbleBoard;
  private int currentPlayer = 0;
  private final Timer timer;
  private Server server;
  private ServerProtocol protocol;
  private Player[] players = new Player[4];
  private boolean isFirstMove = true;
  /*
  @method
  this constructor creates a game with a default scrabbleboard, tilebag and adds only the
  hosting player to the game. Use addPlayer() to add up to 3 players afterwords
   */
  public ServerMatch(Server s, Player[] players) {
    super();
    this.players = players;
    this.server = s;
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    timer = new Timer();
  }

  public ServerMatch(Server s, Player player) {
    super();
    this.players = new Player[4];
    this.players[0] = player;
    this.server = s;
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    timer = new Timer();
  }
  /*
  this constructor creates a game with a default scrabbleboard, tilebag and adds all the
   players to the game. Players cannot be added with addPlayer() afterwords
   */

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
        break;
      }
    }
  }


  /*
    @method checks if Tiles are left in the bag
     */
  public boolean checkTileBag() {
    return tileBag.size() > 0;
  }

  public int getRound() {
    return round;
  }

  public TileBag getTileBag() {
    return tileBag;
  }

  public void startTimer(){
    this.timer.start();
  }

  public String getPlayerName() {
    return this.players[currentPlayer].getName();
  }

  public void placeTiles(Tile[] tiles, String from) throws IOException {
    //if (from.equals(Main.lobby.players[this.currentPlayer].name)) {
      if(tiles.length != 0){
        for (int i = 0; i < tiles.length; i++) {
          this.scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
        }
        if(!this.scrabbleBoard.wordIsConnectedToMiddle(tiles)){
          System.out.println("wrong first move detected");
          server.sendOnlyTo(this.players[this.currentPlayer].name, new PlayFeedbackMessage("server", "Start the Game by placing a word over the center matchfield", false));
        }else{
          System.out.println("everything alrighty");
          PlayFeedbackMessage message = this.scrabbleBoard.submitTiles(from);
          if (message.isSuccessfulMove()) {
            System.out.println("input was valid");
            server.sendToAll(message);
            server.sendOnlyTo(this.players[this.currentPlayer].name,
                new GetNewTilesMessage(this.players[this.currentPlayer].name,
                    this.drawNewTiles(tiles.length)));
            int points = scrabbleBoard.getPoints();
            this.players[this.currentPlayer].addPoints(points);
            System.out.println("points received"+ points);
            server.sendToAll(new SendPointsMessage(this.players[currentPlayer].getName(), points));
            //TODO: send to all but
            server.sendToAll(
                new PlaceTilesMessage(this.players[this.currentPlayer].name, tiles));
            nextPlayer();
          }else{
            server.sendOnlyTo(this.players[this.currentPlayer].name, message);
            System.out.println("input was invalid");
            this.scrabbleBoard.dropChangedTiles();
          }
        }


      }else{
        System.out.println("no tiles were found");
      }
   // } else {
    //  System.out.println("wrong player requested game move: Place Tiles");
   // }

  }

  //Method gives back field with random tiles with the size of needed tiles
  public Tile[] drawNewTiles(int amountNeeded) {
    Tile[] newTiles = new Tile[amountNeeded];
    for (int i = 0; i < amountNeeded; i++) {
      newTiles[i] = this.tileBag.drawTile();
    }
    return newTiles;
  }

  public Timer getTimer() {
    return timer;
  }




  /*
    @method stars the match. It triggers the start of the thread, as well as different methods
    */
  public void startMatch() {
    server.sendToAll(new LobbyInformationMessage("server", this.players));
    int count = 0;
    for (int i = 0; i < this.players.length; i++) {
      if (this.players[i] != null) {
      Tile[] tiles = new Tile[8];
      for (int x = 0; x < tiles.length; x++) {
        tiles[x] = tileBag.drawTile();
      }
        server.sendOnlyTo(this.players[i].name, new GameStartMessage("server", tiles));
        count++;
      }
    }
    startTimer();
    // server message, find out turn, send turn message && send wait message, send out initial tiles,
    // start game thread programmieren. Diese ruft das auf
    //server.sendToAll(new);
  }
  public void sendHistoryMessage(String from,String mess){
    server.sendToAllBut(from,new HistoryMessage(from,mess));
  }
  public void shuffleTilesOfPlayer(String from, Tile[] oldTiles, Tile[] saveTiles) {
    int playerNum = getPlayersNumber(from);
    if (playerNum == -1) {
      System.out.println("Player not found, at shuffle request");
    } else if (playerNum != currentPlayer) {
      System.out.println("Wrong player, at shuffle request");
    } else {
      if (this.players[playerNum].shuffleRack(oldTiles, this.tileBag)) {
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", this.players[playerNum].getRack()));
      } else {
        System.out.println("couldn't shuffle since bag was empty");
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", this.players[playerNum].getRack()));
      }
    }
  }

  /*
  @method finds the player with the inputted name. Returns the number of him in the array. If not found, returns -1
   */
  public int getPlayersNumber(String name) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x].name.equals(name)) {
        return x;
      }
    }
    return -1;
  }

  public void sendTurn() {
    //schikct turn raus
  }

  //

  /*
@method ends the match. It triggers the end of the thread, as well as different methods
 */
  public void endMatch() {
    timer.stopTimer();
  }

  /*
   @method runs constantly and manages the timer of the player of the current turn
    */
  public void nextPlayer() {
    isFirstMove = false;
    int notActivePlayers = 0;
    currentPlayer = (currentPlayer + 1) % 4;
    boolean foundNextPlayer = false;
    System.out.println("die spieler: "+this.players);
    for(int x = 0; x < 3 && !foundNextPlayer; x++){
      if(this.players[currentPlayer] == null){
        currentPlayer = (currentPlayer + 1) % 4;
      }else{
        scrabbleBoard.nextTurn();
        timer.nextPlayer();
        server.sendToAll(new GameTurnMessage("server", currentPlayer));
        foundNextPlayer = !foundNextPlayer;
        break;
      }

    }
    if(!foundNextPlayer){
      System.out.println("no player in game found");
      //TODO: end game
    }
  }


  public void sendPlacedTilesToClients(Tile[] tiles) {
    server.sendToAllBut(this.players[this.currentPlayer].name, new PlaceTilesMessage("server",
        scrabbleBoard.lastPlacedTiles()));
  }

  /*
  send current player profiles to All (occures when somebody joins or leaves)
   */
  public void sendOutPlayerInfos(Player[] players) {
    server.sendToAll(new LobbyInformationMessage("Host", players));
  }

  /*
  send Game start information
   */
  public void itIsYourTurn() {
  }

  /*
  @method runs constantly and manages the timer of the player of the current turn
   */
  public void checkTimer() {

  }

  /*
  this function adds a player to the game. If all places are already occupied, the
  function return false. this indicates, that the game is already full and the player cannot join
   */


  public ScrabbleBoard getScrabbleBoard() {
    return scrabbleBoard;
  }

  public Server getServer() {
    return server;
  }

  public void setServer(Server server) {
    this.server = server;
  }

  public ServerProtocol getProtocol() {
    return protocol;
  }

  public void setProtocol(ServerProtocol protocol) {
    this.protocol = protocol;
  }

  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

}
