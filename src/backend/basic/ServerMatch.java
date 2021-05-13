package backend.basic;

import backend.network.messages.game.GameStartMessage;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.points.SendPointsMessage;
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
  private final Player[] players = new Player[4];
  private final ScrabbleBoard scrabbleBoard;
  private int currentPlayer = 0;
  private final Timer timer;
  private Server server;
  private ServerProtocol protocol;

  /*
  @method
  this constructor creates a game with a default scrabbleboard, tilebag and adds only the
  hosting player to the game. Use addPlayer() to add up to 3 players afterwords
   */
  public ServerMatch(Player playerHost) {
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    players[0] = playerHost;
    timer = new Timer();
    server = new Server();
    Runnable r = new Runnable() {
      public void run() {
        server.listen();
      }
    };
    new Thread(r).start();
  }


  /*
  this constructor creates a game with a default scrabbnleboard, tilebag and adds all the
   players to the game. Players cannot be added with addPlayer() afterwords
   */
  public ServerMatch(Player[] players) {
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    players = players;
    timer = new Timer();
    server = new Server();
    Runnable r = new Runnable() {
      public void run() {
        server.listen();
      }
    };
    new Thread(r).start();
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

  public Player getCurrentPlayer() {
    return players[currentPlayer];
  }

  public void removePlayer(String player) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x].name.equals(player)) {
        this.players[x] = null;
      }
    }
  }

  public String getPlayerName() {
    return players[this.currentPlayer].name;
  }

  public void placeTiles(Tile[] tiles, String from) throws IOException {
    if (from.equals(players[this.currentPlayer].name)) {
      for (int i = 0; i < tiles.length; i++) {
        scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
      }
      String[][] feedback = scrabbleBoard.submitTiles();
      if (scrabbleBoard.inputValudation(feedback)) {
        server.sendOnlyTo(players[this.currentPlayer].name,
            new PlayFeedbackMessage("server", feedback, true));
        int points = scrabbleBoard.getPoints();
        players[this.currentPlayer].addPoints(points);
        server.sendToAll(new SendPointsMessage(this.getPlayerName(), points));
        server.sendToAllBut(players[this.currentPlayer].name,
            new PlaceTilesMessage(players[this.currentPlayer].name, tiles));
        nextPlayer();
      } else {
        server.sendOnlyTo(players[this.currentPlayer].name,
            new PlayFeedbackMessage("server", feedback, true));
      }
    } else {
      System.out.println("wrong player requested game move: Place Tiles");
    }
  }

  public Timer getTimer() {
    return timer;
  }

  /*
    @method stars the match. It triggers the start of the thread, as well as different methods
     */
  public void startMatch() {
    timer.start();
    server.sendToAll(new LobbyInformationMessage("server", players));
    for (int i = 0; i < players.length; i++) {
      Tile[] tiles = new Tile[8];
      for (int x = 0; x < tiles.length; x++) {
        tiles[x] = tileBag.drawTile();
      }
      server.sendOnlyTo(players[i].name, new GameStartMessage("server", tiles));
    }

    // server message, find out turn, send turn message && send wait message, send out initial tiles,
    // start game thread programmieren. Diese ruft das auf
    //server.sendToAll(new);
  }

  public void shuffleTilesOfPlayer(String from, Tile[] oldTiles, Tile[] saveTiles) {
    int playerNum = getPlayersNumber(from);
    if (playerNum == -1) {
      System.out.println("Player not found, at shuffel request");
    } else if (playerNum != currentPlayer) {
      System.out.println("Wrong plasyer, at shuffel request");
    } else {
      if (players[playerNum].shuffleRack(oldTiles, this.tileBag)) {
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", players[playerNum].getRack()));
      } else {
        System.out.println("couldn't shuffle since bag was empty");
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", players[playerNum].getRack()));
      }
    }
  }

  /*
  @method finds the player with the inputted name. Returns the number of him in the array. If not found, returns -1
   */
  public int getPlayersNumber(String name) {
    for (int x = 0; x < players.length; x++) {
      if (players[x].name.equals(name)) {
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
    int notActivePlayers = 0;
    int nextPlayer = 4 % (currentPlayer + 1);
    do {
      notActivePlayers++;
      nextPlayer = 4 % (nextPlayer + 1);
    } while (notActivePlayers < 4 && players[nextPlayer] != null);

    if (players[nextPlayer].checkTimer()) {
      currentPlayer = nextPlayer;
    }
    scrabbleBoard.nextTurn();
    server.sendOnlyTo(this.players[this.currentPlayer].name, new GameTurnMessage("server"));
  }

  public Player[] getPlayers() {
    return players;
  }

  /*
  match logic:
  0. Send player profiles to all
  0,5. buildRacks, send startGame message
  1. Send out message to player whos turn it is
  2a. Player places tiles -> submits tiles -> player receives info if input is valid -> either: back to 2 or: update leaderboard and nextPlayer()
  2b. Draws new Tiles -> nextPlayer()
  2b. does Nothing -> nextPlayer()
  3. again 1.
   */
/*
adds a server to the match
 */
  public void addServer(Server s) {
    server = s;
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
  public boolean addPlayer(Player player) {
    for (int i = 0; i < players.length; i++) {
      if (players[i] == null) {
        players[i] = player;
        return true;
      }
    }
    return false;
  }

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
