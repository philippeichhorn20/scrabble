package backend.basic;

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
public class Match {

  private final TileBag tileBag;
  private final int round = 0;
  private Player[] players = new Player[4];
  private ScrabbleBoard scrabbleBoard;
  private int currentPlayer = 0;

  /*
  this constructor creates a game with a default scrabbnleboard, tilebag and adds only the
  hosting player to the game. Use addPlayer() to add up to 3 players afterwords
   */
  public Match(Player playerHost) {
    ScrabbleBoard.setUpScrabbleBoard();
    scrabbleBoard = new ScrabbleBoard();
    tileBag = new TileBag();
    players[0] = playerHost;
  }

  /*
  this constructor creates a game with a default scrabbnleboard, tilebag and adds all the
   players to the game. Players cannot be added with addPlayer() afterwords
   */
  public Match(Player[] players) {
    ScrabbleBoard.setUpScrabbleBoard();
    tileBag = new TileBag();
    this.players = players;
  }


  /*
  @method runs constantly and manages the timer of the player of the current turn
   */
  public void checkTimer() {
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

  /*
    @method checks if Tiles are left in the bag
     */
  public boolean checkTileBag() {
    return tileBag.size() > 0;
  }

  public int getRound() {
    return this.round;
  }

  public TileBag getTileBag() {
    return this.tileBag;
  }

  public Player getCurrentPlayer() {
    return players[currentPlayer];
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
