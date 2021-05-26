package backend.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/* @author peichhor
 * @version 1.0
 * @description a class which checks, weather a word is valid or not
 * @param url safes the url to the database
 * */
public class WordCheckDB {

  public HashSet<String> words = new HashSet<String>();


  public static void main(String[] args) {
    System.out.println(findWord("apple"));
  }

  static String url = "jdbc:sqlite:src/resources/wordsList.db";
  static String urlTxt = "src/resources/ScrablleWordsFile.txt";

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
          .executeQuery("SELECT * FROM words WHERE (word = '" + word + "');");
      if (rs.getString(1) == "") {
        System.out.println(word + " not found");
        return "";
      } else {
        return rs.getString(0);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    return "";
  }

  /*
  @author jawinter
  This function returns true if the string is in database consisting of dictionary
   */
  public boolean checkWord(String word) {
    boolean exists = false;
    if(this.words.contains(word.toUpperCase())) {
      exists = true;
    }
    return exists;
  }

  public void readDictionary(String textFile) {
    try{
    File dic = new File("src/resources/dictionary.txt");
    FileReader fr = new FileReader(dic);
    BufferedReader br = new BufferedReader(fr);
    String z;
      while((z=br.readLine())!=null) {
        String[] array = z.split("\\t");
        if(array.length>1) {
          this.words.add(array[0]);
        }
        }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static void importTextToDB() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (final ClassNotFoundException e) {
      System.out.println(e);
    }
    try (Connection conn = DriverManager.getConnection(WordCheckDB.url)) {
      java.sql.Statement stm = conn.createStatement();
      stm.execute("DROP TABLE IF EXISTS words;");
      stm.execute("CREATE TABLE words (word VARCHAR(100) PRIMARY KEY)");
      PreparedStatement ps = conn.prepareStatement("INSERT INTO words (word) values (?);");
      System.out.println("loading started..");

      File file = new File(urlTxt);    //creates a new file instance
      FileReader fr = new FileReader(file);
      BufferedReader reader = new BufferedReader(fr);
      String line;
      conn.setAutoCommit(false);
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
        ps.setString(1, line);
        ps.execute();
      }
      conn.commit();
      conn.setAutoCommit(true);

      System.out.println("database intialized..");

      /*
        stm.execute(" LOAD DATA LOCAL INFILE  words"
          + "FROM '"+ urlTxt +"'"
          + "WITH "
          + "    FIELDTERMINATOR = '\t'"
          + "    ROWTERMINATOR = '\\n'"
          + "  ");
       */

      System.out.println("database was written");
    } catch (SQLException e) {
      System.out.println("struggle with sql");
      System.out.println(e.getMessage());
      e.printStackTrace();
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }


}
