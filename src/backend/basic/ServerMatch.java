package backend.basic;

import backend.network.messages.game.LobbyInformationMessage;
import backend.network.server.Server;

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

  private static final TileBag tileBag = new TileBag();
  private static final int round = 0;
  private static Player[] players = new Player[4];
  private static ScrabbleBoard scrabbleBoard;
  private static int currentPlayer = 0;
  private static Server server;
  private static Thread timer;

  /*
  this constructor creates a game with a default scrabbnleboard, tilebag and adds only the
  hosting player to the game. Use addPlayer() to add up to 3 players afterwords
   */
  public ServerMatch(Player playerHost) {
    ScrabbleBoard.setUpScrabbleBoard();
    scrabbleBoard = new ScrabbleBoard();
    players[0] = playerHost;
  }

  /*
  this constructor creates a game with a default scrabbnleboard, tilebag and adds all the
   players to the game. Players cannot be added with addPlayer() afterwords
   */
  public ServerMatch(Player[] players) {
    ScrabbleBoard.setUpScrabbleBoard();
    ServerMatch.players = players;
  }

  /*
    @method checks if Tiles are left in the bag
     */
  public static boolean checkTileBag() {
    return tileBag.size() > 0;
  }

  public static int getRound() {
    return round;
  }

  public static TileBag getTileBag() {
    return tileBag;
  }

  public static Player getCurrentPlayer() {
    return players[currentPlayer];
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
    ScrabbleBoard.nextTurn();
  }

  public static Player[] getPlayers() {
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
  public void sendOutPlayerInfos() {
    server.sendToAll(new LobbyInformationMessage("Host"));
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
}
