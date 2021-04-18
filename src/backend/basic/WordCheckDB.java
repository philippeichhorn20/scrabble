package backend.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/* @author peichhor
 * @version 1.0
 * @description a class which checks, weather a word is valid or not
 * @param url safes the url to the database
 * */
public class WordCheckDB {

  static String url = "jdbc:sqlite:ScrabbleWordsDB";

  /*
  looks up the given word in the database. If it exists it returns the decsription of the word,
  if not it returns null
  The function ignores weather the word is in small or capital letters to reduce errors
   */
  static boolean findWord(String word) {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (final ClassNotFoundException e) {
      System.out.println(e);
    }
    try (Connection conn = DriverManager.getConnection(WordCheckDB.url)) {
      java.sql.Statement stm = conn.createStatement();
      word = word.toUpperCase();
      ResultSet rs = stm
          .executeQuery("SELECT description FROM words WHERE (word = '" + word + "');");
      return rs != null;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /*
  TODO: this function will search for a word that can be build with the given Tiles in the bag
  and on the board
   */
  static boolean findWordWithTiles(ArrayList<Tile> tilesOnScrabbleBoard) {
    boolean wordFound = false;
    for (int i = 0; i < tilesOnScrabbleBoard.size() && !wordFound; i++) {

    }
    return false;
  }

}
