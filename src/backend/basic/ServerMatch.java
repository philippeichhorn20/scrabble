package backend.basic;

import backend.network.messages.game.GameStartMessage;
import backend.network.messages.game.GameTurnMessage;
import backend.network.messages.game.LobbyInformationMessage;
import backend.network.messages.points.PlayFeedbackMessage;
import backend.network.messages.points.SendPointsMessage;
import backend.network.messages.tiles.GetNewTilesMessage;
import backend.network.messages.tiles.PlaceTilesMessage;
import backend.network.messages.tiles.ReceiveShuffleTilesMessage;
import backend.network.server.Server;
import backend.network.server.ServerProtocol;
import frontend.Main;
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

public class ServerMatch extends Match{

  private final TileBag tileBag = new TileBag();
  private final int round = 0;
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
  public ServerMatch(Server s) {
    super();
    this.server = s;
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    timer = new Timer();

  }


  /*
  this constructor creates a game with a default scrabbleboard, tilebag and adds all the
   players to the game. Players cannot be added with addPlayer() afterwords
   */




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

  public String getPlayerName() {
    return Main.lobby.players[currentPlayer].getName();
  }

  public void placeTiles(Tile[] tiles, String from) throws IOException {
    String[][] feedback = new String[0][0];
    if (from.equals(Main.lobby.players[this.currentPlayer].name)) {
      if(tiles.length != 0){
        for (int i = 0; i < tiles.length; i++) {
          scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
        }
        feedback = scrabbleBoard.submitTiles();
        if (scrabbleBoard.inputValudation(feedback)) {
          System.out.println("input was valid");
          server.sendOnlyTo(Main.lobby.players[this.currentPlayer].name,
              new PlayFeedbackMessage("server", feedback, true));
          server.sendOnlyTo(Main.lobby.players[this.currentPlayer].name,
              new GetNewTilesMessage(Main.lobby.players[this.currentPlayer].name,
                  drawNewTiles(tiles.length)));
          int points = scrabbleBoard.getPoints();
          Main.lobby.players[this.currentPlayer].addPoints(points);
          server
              .sendToAll(new SendPointsMessage(Main.lobby.players[currentPlayer].getName(), points));
          server.sendToAllBut(Main.lobby.players[this.currentPlayer].name,
              new PlaceTilesMessage(Main.lobby.players[this.currentPlayer].name, tiles));
          nextPlayer();
      }else {
          System.out.println("input was invalid");
          server.sendOnlyTo(Main.lobby.players[this.currentPlayer].name,
              new PlayFeedbackMessage("server", feedback, false));
        }

      }
    } else {
      System.out.println("wrong player requested game move: Place Tiles");
    }

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
    timer.start();
    server.sendToAll(new LobbyInformationMessage("server", Main.lobby.players));
   System.out.println("Players in Lobby: "+ Main.lobby.players.length);
    int count = 0;
    for (int i = 0; i < Main.lobby.players.length; i++) {
      Tile[] tiles = new Tile[8];
      for (int x = 0; x < tiles.length; x++) {
        tiles[x] = tileBag.drawTile();
      }
      if (Main.lobby.players[i] != null) {
        server.sendOnlyTo(Main.lobby.players[i].name, new GameStartMessage("server", tiles));
        System.out.println("send GameStartMessage to" + Main.lobby.players[i].name);
        count++;
      }
      System.out.println("player count:" + count);
    }
    nextPlayer();
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
      if (Main.lobby.players[playerNum].shuffleRack(oldTiles, this.tileBag)) {
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", Main.lobby.players[playerNum].getRack()));
      } else {
        System.out.println("couldn't shuffle since bag was empty");
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", Main.lobby.players[playerNum].getRack()));
      }
    }
  }

  /*
  @method finds the player with the inputted name. Returns the number of him in the array. If not found, returns -1
   */
  public int getPlayersNumber(String name) {
    for (int x = 0; x < Main.lobby.players.length; x++) {
      if (Main.lobby.players[x].name.equals(name)) {
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
    } while (notActivePlayers < 4 && Main.lobby.players[nextPlayer] != null);

    if (Main.lobby.players[nextPlayer].checkTimer()) {
      currentPlayer = nextPlayer;
    }
    scrabbleBoard.nextTurn();
    server.sendToAll(new GameTurnMessage("server", currentPlayer));
  }


  public void sendPlacedTilesToClients(Tile[] tiles) {
    server.sendToAllBut(Main.lobby.players[this.currentPlayer].name, new PlaceTilesMessage("server",
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
