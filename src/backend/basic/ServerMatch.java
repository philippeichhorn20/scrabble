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
import backend.network.server.Server;
import backend.network.server.ServerProtocol;
import backend.network.server.ServerSettings;
import java.io.IOException;


/**
 * players: the players that are participating in the game * scrabbleboard: an instance of the
 * physical scrabble board with some additions * to represent the actions of the current turn *
 * tileBag the bag that conatins the tiles that were not pulled yet * currentPlayer conatins the
 * number in the players array which * points at the player who's turn it is * roundNum number of
 * the current round.
 *
 * @author peichhor
 * @version 1.0
 */


public class ServerMatch {

  private final TileBag tileBag = new TileBag();
  private final int round = 0;
  private final ScrabbleBoard scrabbleBoard;
  private final Timer timer;
  private int currentPlayer = 0;
  private Server server;
  private ServerProtocol protocol;
  private Player[] players = new Player[4];
  private boolean isFirstMove = true;
  private int pointlessTurns = 0;

  /**
   * * ServerMatch this constructor creates a game with a default scrabbleboard, tilebag and * adds
   * only the hosting player to the game. Use addPlayer() to add up to 3 players afterwords
   *
   * @param s       the server of the instance
   * @param players who has already joined
   */
  public ServerMatch(Server s, Player[] players) {
    super();
    this.players = players;
    this.server = s;
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    timer = new Timer(this);
  }

  /**
   * Constructor when players are not determined yet.
   *
   * @param s the server instance
   */
  public ServerMatch(Server s) {
    super();
    this.players = new Player[4];
    this.server = s;
    scrabbleBoard = new ScrabbleBoard();
    scrabbleBoard.setUpScrabbleBoard();
    timer = new Timer(this);
  }

  /**
   * adds a player in the next free spot in player array.
   *
   * @param p the player which need to be added
   * @return returns true if array is full
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

  /**
   * removes players from player array.
   *
   * @param player the player that need to be removed
   */
  public void removePlayer(String player) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x] != null && this.players[x].name.equals(player)) {
        this.players[x] = null;
        break;
      }
    }
  }


  /**
   * checks if tiles are left in the instances tile bag.
   *
   * @return weather tiles are left or not
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

  /**
   * starts the timer.
   */
  public void startTimer() {
    this.timer.start();
  }

  public String getPlayerName() {
    return this.players[currentPlayer].getName();
  }

  /**
   * this method handles incoming requests for Game Moves. It then returns a message with the word
   * explanation, if the input was successful. Otherwise a message is deployed explaining why the
   * move could not be performed
   *
   * @param tiles the tiles that were placed
   * @param from  the user who placed the tiles
   * @throws IOException if messages could not be send
   */
  public void placeTiles(Tile[] tiles, String from) throws IOException {
    //if (from.equals(Main.lobby.players[this.currentPlayer].name)) {
    if (tiles.length != 0) {
      for (int i = 0; i < tiles.length; i++) {
        this.scrabbleBoard.placeTile(tiles[i], tiles[i].getX(), tiles[i].getY());
      }
      if (!this.scrabbleBoard.wordIsConnectedToMiddle(tiles)) {
        //word input is structrually wrong
        server.sendOnlyTo(this.players[this.currentPlayer].name, new PlayFeedbackMessage("server",
            null, false));
        this.scrabbleBoard.dropChangedTiles();
      } else {
        //word input is technically right, but the words are not in the selected database
        PlayFeedbackMessage message = this.scrabbleBoard.submitTiles(from);
        if (message.isSuccessfulMove()) {
          server.sendToAll(message);
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
    }


  }

  /**
   * Method gives back field with random tiles with the size of needed tiles.
   *
   * @param amountNeeded the amount of new tiles needed
   * @return the tiles that were drawn
   */
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


  /**
   * starts the matches, including starting the ai-protocolls.
   */

  public void startMatch() {
    int count = 0;
    this.startAiProtocols();
    Player[] correctPlayer = new Player[4];

    for (int i = 0; i < 4; i++) {
      Player p = GameInformation.getInstance().getPlayers()[i];
      if (p != null && p.getStatus() == Playerstatus.AI) {
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
    for (int i = 0; i < this.players.length; i++) {
      if (this.players[i] != null) {
        Tile[] tiles = new Tile[7];
        for (int x = 0; x < tiles.length; x++) {
          tiles[x] = tileBag.drawTile();
        }
        server.sendOnlyTo(this.players[i].name, new GameStartMessage("server", tiles));
        count++;
      }
    }
    startTimer();
  }

  /**
   * starts the protocols of the AIPLayers that were added.
   */
  public void startAiProtocols() {
    for (Player p : this.players) {
      if (p != null && p.getStatus() == Playerstatus.AI) {
        AIProtocol aiProtocol = new AIProtocol(ServerSettings.getLocalHostIp4Address(),
            ServerSettings.port, p.getName(), (PlayerAI) p);
        aiProtocol.start();
        ((PlayerAI) p).setAiProtocol(aiProtocol);
      }
    }
  }

  /**
   * informs the other players about a new history message.
   *
   * @param from the sender
   * @param mess the content
   */
  public void sendHistoryMessage(String from, String mess) {
    server.sendToAllBut(from, new HistoryMessage(from, mess));
  }

  /**
   * receives Tiles from Player in @param oldTiles. If the bag has less than 7 Tiles left, the old
   * tiles will be returned. Otherwise tiles are drawn from the bag and send back to the client
   *
   * @param from     the sender
   * @param oldTiles the tiles that need to be swapped
   */
  public void shuffleTilesOfPlayer(String from, Tile[] oldTiles) {
    int playerNum = getPlayersNumber(from);
    if (this.tileBag.size() > 7) {
      if (playerNum == currentPlayer) {

        Tile[] newTiles = this.players[playerNum].shuffleRack(oldTiles, this.tileBag);
        for (Tile tile : newTiles) {
        }
        server.sendOnlyTo(from,
            new ReceiveShuffleTilesMessage("server", newTiles));

      }
    } else {
      server.sendOnlyTo(from,
          new ReceiveShuffleTilesMessage("", oldTiles));
    }
  }

  /**
   * finds the player with the inputted name.
   *
   * @param name the string that describes the player
   * @return the number of him in the array. If not found, returns -1
   */
  public int getPlayersNumber(String name) {
    for (int x = 0; x < this.players.length; x++) {
      if (this.players[x].name.equals(name)) {
        return x;
      }
    }
    return -1;
  }

  /**
   * finds out who is the next player in line and send the GameTurnMessages to the players.
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
        timer.setTimerTo(currentPlayer);
        server.sendToAll(new GameTurnMessage("server", currentPlayer));
        foundNextPlayer = !foundNextPlayer;
        break;
      }
    }
    if (!foundNextPlayer) {
      System.out.println("no player in game found");
    }
  }

  /**
   * finds the winner.
   *
   * @return the winner, the player with the most points
   */
  public Player getWinner() {
    Player winner = this.players[0];
    for (int x = 1; x < this.players.length; x++) {
      if (players[x] != null) {
        if (players[x].getScore() > winner.getScore()) {
          winner = this.players[x];
        }
      }
    }
    return winner;
  }

  /**
   * sends out a message to the players, if they have won or not.
   */
  public void gameOver() {
    this.server.sendToAllBut(this.getWinner().getName(), new GameLooseMessage("server"));
    this.server.sendOnlyTo(this.getWinner().getName(), new GameWinMessage("server"));
  }

  //getter and setter


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

  /**
   * increments the pointless turns.
   */
  public void incrementPointlessTurns() {
    pointlessTurns++;
  }


  public Player[] getPlayers() {
    return players;
  }


}
