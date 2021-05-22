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

  static String url = "jdbc:sqlite:src/resources/wordsList.db";

  /*
  looks up the given word in the database. If it exists it returns the decsription of the word,
  if not it returns null
  The function ignores weather the word is in small or capital letters to reduce errors
   */
  static String findWord(String word) {
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
      if (rs.first()) {
        System.out.println(word + " not found");
        return "";
      } else {
        return rs.getString(0);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return "";
  }

  /*
  @author jawinter
  This function returns true if the string is in dictionary
   */
  public static boolean checkWord(String word) {
    boolean exists = false;
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
      if (rs.next()) {
        exists = true;
      } else {
        exists = false;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return exists;
  }
  static void importTextToDB() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (final ClassNotFoundException e) {
      System.out.println(e);
    }
    try (Connection conn = DriverManager.getConnection(WordCheckDB.url)) {
      java.sql.Statement stm = conn.createStatement();
      stm.executeQuery("CREATE TABLE dictionary(word VARCHAR(255), description VARCHAR(255));");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }


}
