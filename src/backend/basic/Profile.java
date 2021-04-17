package backend.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The profile class is a representation of the data entry from a database.
 *
 * @author mkolinsk
 */
public class Profile {
  private int id;
  private String name;
  private String color;
  private int wins;
  private int games;
  private int points;


  String jdbcUrl = "jdbc:sqlite:src/resources/profilesdb.db";

  public Profile(String name) {
    this.name = name;
    loadProfile();
  }

  private void loadProfile() {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "SELECT rowid, wins, games, points FROM profiles WHERE name = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, name);
      ResultSet result = stmt.executeQuery();
      while (result.next()) {
        id = result.getInt("rowid");
        wins = result.getInt("wins");
        games = result.getInt("games");
        points = result.getInt("points");
      }

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  public String getName() {
    return name;
  }

  /*@author nilschae*/
  public String getColor() { return this.color;}

  public int getWins() {
    return wins;
  }

  public int getGames() {
    return games;
  }

  public int getPoints() {
    return points;
  }

  public int getId() {
    return id;
  }

  /**
   * Updates the name of a profile in the database.
   *
   * @param name New name of the profile.
   * @param id  Profile's unique id.
   */
  public void setName(String name, int id) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "UPDATE profiles SET name = ? WHERE rowid = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setString(1, name);
      stmt.setInt(2, id);
      stmt.executeUpdate();

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    this.name = name;
  }

  /**
   * Updates the wins statistic in the database.
   *
   * @param wins  Number of wins.
   * @param id  Profile's unique id.
   */
  public void setWins(int wins, int id) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "UPDATE profiles SET wins = ? WHERE rowid = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, wins);
      stmt.setInt(2, id);
      stmt.executeUpdate();

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    this.wins = wins;
  }

  /**
   * Updates games statistic in the database.
   *
   * @param games Number of games.
   * @param id Profile's unique id.
   */
  public void setGames(int games, int id) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "UPDATE profiles SET games = ? WHERE rowid = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, games);
      stmt.setInt(2, id);
      stmt.executeUpdate();

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    this.games = games;
  }

  /**
   * Updates the points in the database.
   *
   * @param points Number of points.
   * @param id  Profile's unique id.
   */
  public void setPoints(int points, int id) {
    try {
      Connection connection = DriverManager.getConnection(jdbcUrl);
      String sql = "UPDATE profiles SET points = ? WHERE rowid = ?";
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, points);
      stmt.setInt(2, id);
      stmt.executeUpdate();

    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    this.points = points;
  }

  /*@author nilschae*/
  public void setColor(String hexColor) {
    this.color = hexColor;
  }
}
