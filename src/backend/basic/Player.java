package backend.basic;

import java.io.Serializable;

/**
 * Class that represents a player in the game of scrabble.
 *
 * @author nilschae
 */
public class Player implements Serializable {

  String name;
  long score;
  String color;
  Tile[] rack = new Tile[7];
  Playerstatus status;
  int games;
  int wins;

  /**
   * Constructor.
   *
   * @param name name of the player.
   * @param color color he likes.
   * @param games amount of games played.
   * @param wins amount of wins played.
   * @param status playerstatus.
   */
  public Player(String name, String color, int games, int wins, Playerstatus status) {
    this.name = name;
    this.color = color;
    this.score = 0;
    this.status = status;
    this.games = games;
    this.wins = wins;
  }

  /**
   * A method to shuffle chosen tiles from the players rack and drawing new random tile instead.
   *
   * @param tilesToShuffel tiles to be exchanged.
   * @param bag tilebag
   * @return new tiles.
   */
  public Tile[] shuffleRack(Tile[] tilesToShuffel, TileBag bag) {
    Tile[] newTiles = new Tile[tilesToShuffel.length];
    for (int i = 0; i < tilesToShuffel.length; i++) {
      newTiles[i] = bag.drawTile();
      bag.insertTileToBag(tilesToShuffel[i]);
    }
    /*
    int count = 0;
    for (int i = 0; i < this.rack.length; i++) {
      if (this.rack[i] == null) {
        this.rack[i] = newTiles[count];
        count++;
      }
    }
     */
    return newTiles;
  }

  /*Add points to the players score*/
  public void addPoints(long points) {
    this.score += points;
  }


  /**
   * A method which fills the rack with tiles from tilebag.
   *
   * @param bag tilebag
   * @return tile array.
   */
  public Tile[] fillRack(TileBag bag) {
    if (!bag.isEmpty()) {
      for (int i = 0; i < rack.length; i++) {
        if (rack[i] == null) {
          if (bag.isEmpty()) {
            break;
          } else {
            rack[i] = bag.drawTile();
          }
        }
      }
    }
    return null;
  }

  /**
   * Draw a specific tile from players rack.
   *
   * @param tileToDraw tile that needs to be drawn.
   * @return that tile.
   */
  public Tile drawTileFromRack(Tile tileToDraw) {
    for (int i = 0; i < this.rack.length; i++) {
      if (rack[i].equals(tileToDraw)) {
        Tile foundTile = rack[i];
        rack[i] = null;
        return foundTile;
      }
    }
    return null;
  }

  /*
  this methode updates the tile rack. It looks weather a Tile was requested to be exchanged.
  If so it replaces it with a new Tile.
  This method is stable concerning tilesBefore and the rack. i.e.: if Tile a is in front of Tile b
  in rack, it expects Tile a to be in front of Tile b in tilesBefore as well.
   */
  public void updateRack(Tile[] newRack) {
    this.rack = newRack;
  }

  public Tile getTileOnPositionInRack(int pos) {
    return this.rack[pos];
  }

  public void setStatus(Playerstatus status) {
    this.status = status;
  }

  public int getWins() {
    return wins;
  }

  public int getGames() {
    return games;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getName() {
    return this.name;
  }

  public long getScore() {
    return this.score;
  }

  public String getColor() {
    return this.color;
  }

  public Tile[] getRack() {
    return this.rack;
  }

  public Playerstatus getStatus() {
    return this.status;
  }

  /**
   * Puts a tile back on rack.
   *
   * @param tile the tile that gets put back on rack.
   */
  public void putBackOnRack(Tile tile) {
    for (int x = 0; x < rack.length; x++) {
      if (this.rack[x] == null) {
        this.rack[x] = tile;
        break;
      }
    }
  }

  public void setTimerPersonalTimerToZero() {}

  public void setTimerToZero() {}

  /**
   * Enum of the playerstatus.
   */
  public enum Playerstatus {
    OUTOFGAME,
    WAIT,
    TURN,
    AI
  }

  /**
   * Returns the name of the player.
   *
   * @return returns the name.
   */
  public String toString() {
    if (this.name == null) {
      return "null";
    } else {
      return this.name;
    }
  }

  /**
   * Returns if the players rack is empty.
   *
   * @param gts graphictiles.
   * @return boolean.
   */
  public static boolean isEmptyRack(GraphicTile[] gts) {
    for (int x = 0; x < gts.length; x++) {
      if (gts[x].isVisible()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return the number of players in the lobby.
   *
   * @param players the whole lobby
   * @return number of players that aren't == null;
   */
  public static int getPlayerCount(Player[] players) {
    int count = 0;
    for (int x = 0; x < players.length; x++) {
      if (players[x] != null) {
        x++;
      }
    }
    return count;
  }
}
