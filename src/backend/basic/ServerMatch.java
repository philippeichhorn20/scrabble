package backend.basic;

import backend.ai.PlayerAI;
import backend.basic.Player.Playerstatus;
import backend.network.client.AIProtocol;
import backend.network.messages.game.GameLooseMessage;
import backend.network.messages.game.GameStartMessage;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.game.GameWinMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.points.SendPointsMessage;
import backend.network.messages.text.HistoryMessage;
import backend.network.messages.tiles.GetNewTilesMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.messages.time.TimeAlertMessage;
import backend.network.messages.time.TimeAlertType;
import backend.network.server.Server;
import backend.network.server.ServerProtocol;
import backend.network.server.ServerSettings;
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
  private int pointlessTurns = 0;

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
    timer = new Timer(this);
  }

  public ServerMatch(Server s) {
    super();
    this.players = new Player[4];
    this.server = s;
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    timer = new Timer(this);
  }
  /*
  this constructor creates a game with a default scrabbleboard, tilebag and adds all the
   players to the game. Players cannot be added with addPlayer() afterwords
   */

  public boolean addPlayer(Player p) {
    if (players[0] == null) {
      players[0] = p;
    } else if (players[1] == null) {
      players[1] = p;
    } else if (players[2] == null) {
      players[2] = p;
    } else if (players[3] == null) {
      players[3] = p;
    } else {
      return false;
    }
    return true;

  }

  public void removePlayer(String player) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x] != null && this.players[x].name.equals(player)) {
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

  public void startTimer() {
    this.timer.start();
  }

  public String getPlayerName() {
    return this.players[currentPlayer].getName();
  }

  public void placeTiles(Tile[] tiles, String from) throws IOException {
    //if (from.equals(Main.lobby.players[this.currentPlayer].name)) {
    if (tiles.length != 0) {
      for (int i = 0; i < tiles.length; i++) {
        this.scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
      }
      if (!this.scrabbleBoard.wordIsConnectedToMiddle(tiles)) {
        server.sendOnlyTo(this.players[this.currentPlayer].name, new PlayFeedbackMessage("server",
            null, false));
        this.scrabbleBoard.dropChangedTiles();
      } else {
        PlayFeedbackMessage message = this.scrabbleBoard.submitTiles(from);
        if (message.isSuccessfulMove()) {
          server.sendToAll( message);
          server.sendOnlyTo(this.players[this.currentPlayer].name,
              new GetNewTilesMessage(this.players[this.currentPlayer].name,
                  this.drawNewTiles(tiles.length)));
          int points = scrabbleBoard.getPoints();
          if (points == 0) {
            pointlessTurns++;
          } else {
            pointlessTurns = 0;
          }
          this.players[this.currentPlayer].addPoints(points);
          server.sendToAll(new SendPointsMessage(this.players[currentPlayer].getName(), points));
          //TODO: send to all but
          server.sendToAllBut(this.players[currentPlayer].name,
              new PlaceTilesMessage(this.players[this.currentPlayer].name, tiles));
          nextPlayer();
        } else {
          server.sendOnlyTo(this.players[this.currentPlayer].name, message);
          this.scrabbleBoard.dropChangedTiles();
        }
      }


    } else {
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
      System.out.print("..." + newTiles[i].getLetter());
    }
    return newTiles;
  }

  public Timer getTimer() {
    return timer;
  }


  /*
    @method stars the match. It triggers the start of the thread, as well as different methods
    */
  /*public void startMatch() {
    int count = 0;
    this.startAiProtocols();
    server.sendToAll(new LobbyInformationMessage("server", this.players));
    for (int i = 0; i < this.players.length; i++) {
      if (this.players[i] != null) {
        Tile[] tiles = new Tile[7];
        for (int x = 0; x < tiles.length; x++) {
          tiles[x] = tileBag.drawTile();
          System.out.println(tiles[x].getValue());
        }
        server.sendOnlyTo(this.players[i].name, new GameStartMessage("server", tiles));
        count++;
      }
    }
    startTimer();
    // server message, find out turn, send turn message && send wait message, send out initial tiles,
    // start game thread programmieren. Diese ruft das auf
    //server.sendToAll(new);
  }*/ //i have something different but it works
  public void startMatch() {
    int count = 0;
    this.startAiProtocols();
    Player[] correctPlayer = new Player[4];

    for(int i = 0; i < 4; i++) {
      Player p = GameInformation.getInstance().getPlayers()[i];
      if(p != null && p.getStatus() == Playerstatus.AI) {
        Player aiPlayer = new Player(GameInformation.getInstance().getPlayers()[i].getName(),
            GameInformation.getInstance().getPlayers()[i].getColor(),
            GameInformation.getInstance().getPlayers()[i].getGames(),
            GameInformation.getInstance().getPlayers()[i].getWins(), Playerstatus.WAIT);

        correctPlayer[i] = aiPlayer;

      } else {
        correctPlayer[i] = GameInformation.getInstance().getPlayers()[i];
      }
    }

    server.sendToAll(new LobbyInformationMessage("server", correctPlayer));
    System.out.println("LOBBY INFORMATION START MATCH MESSAGE SENT CORRECTPLAYERS");
    for (int i = 0; i < this.players.length; i++) {
      if (this.players[i] != null) {
        Tile[] tiles = new Tile[7];
        for (int x = 0; x < tiles.length; x++) {
          tiles[x] = tileBag.drawTile();
          System.out.println(tiles[x].getValue());
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
  public void startAiProtocols(){
    for(Player p : this.players){
      if(p!=null && p.getStatus()==Playerstatus.AI){
        AIProtocol aiProtocol = new AIProtocol(ServerSettings.getLocalHostIp4Address(),ServerSettings.port,p.getName(),(PlayerAI) p);
        aiProtocol.start();
        ((PlayerAI) p).setAiProtocol(aiProtocol);
      }
    }
  }

  public void sendHistoryMessage(String from, String mess) {
    server.sendToAllBut(from, new HistoryMessage(from, mess));
  }

  public void shuffleTilesOfPlayer(String from, Tile[] oldTiles) {
    int playerNum = getPlayersNumber(from);
    if (this.tileBag.size()>7) {
      if (playerNum != currentPlayer) {
        System.out.println("Wrong player, at shuffle request");
      } else {
        Tile[] newTiles = this.players[playerNum].shuffleRack(oldTiles, this.tileBag);
        for(Tile tile : newTiles){
        }
          server.sendOnlyTo(from,
              new ReceiveShuffleTilesMessage("server", newTiles));

      }
    } else {
      server.sendOnlyTo(from,
          new ReceiveShuffleTilesMessage("", oldTiles));
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
    for (int x = 0; x < 4 && !foundNextPlayer; x++) {
      if (this.players[currentPlayer] == null) {
        currentPlayer = (currentPlayer + 1) % 4;
      } else {
        scrabbleBoard.nextTurn();
        timer.nextPlayer();
        server.sendToAll(new GameTurnMessage("server", currentPlayer));
        foundNextPlayer = !foundNextPlayer;
        break;
      }

    }
    if (!foundNextPlayer) {
      System.out.println("no player in game found");
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
  public void sendTimeIsUp() {
    this.server.sendOnlyTo(players[currentPlayer].getName(),
        new TimeAlertMessage("server", TimeAlertType.TIME_OVER));
    this.nextPlayer();
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


  public void gameOver() {
    this.server.sendToAllBut(this.getWinner().getName(), new GameLooseMessage("server"));
    this.server.sendOnlyTo(this.getWinner().getName(),new GameWinMessage("server"));
  }

  public void incrementPointlessTurns() {
    pointlessTurns++;
  }

  public Player[] getPlayers() {
    return players;
  }

  public Player getWinner(){
    Player winner = this.players[0];
    for(int x = 1; x < this.players.length; x++){
      if(players[x] != null){
        if(players[x].getScore() > winner.getScore()){
          winner =this.players[x];
        }
      }
    }
    return winner;
  }
}
