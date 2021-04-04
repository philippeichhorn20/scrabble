package backEnd.basic;

import java.sql.*;

//@author mkolinsk

public class Profile {
    private int id;
    private String name;
    private int wins;
    private int games;
    private int points;

    String jdbcUrl = "jdbc:sqlite:/IntelliJ/scrabble14-master/scrabble14-master/src/resources/profilesdb.db"; //source of the database, will probably need to change that

    public Profile(String name) {
        this.name = name;
        loadProfile();
    }
    private void loadProfile(){
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "SELECT rowid, wins, games, points FROM profiles WHERE name = ?" ;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,name);
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

    public String getName(){
        return name;
    }
    public int getWins(){
        return wins;
    }
    public int getGames(){
        return games;
    }
    public int getPoints() {
        return points;
    }
    public int getId(){
        return id;
    }
    public void setName(String name,int id) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "UPDATE profiles SET name = ? WHERE rowid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setInt(2,id);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        this.name = name;
    }

    public void setWins(int wins,int id) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "UPDATE profiles SET wins = ? WHERE rowid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,wins);
            stmt.setInt(2,id);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        this.wins = wins;
    }

    public void setGames(int games,int id) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "UPDATE profiles SET games = ? WHERE rowid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,games);
            stmt.setInt(2,id);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        this.games = games;
    }

    public void setPoints(int points) {
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql = "UPDATE profiles SET points = ? WHERE rowid = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,points);
            stmt.setInt(2,id);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        this.points = points;
    }


}
