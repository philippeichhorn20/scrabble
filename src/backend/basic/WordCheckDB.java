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

  public static HashSet<String> words = new HashSet<String>();



  public static String url = "jdbc:sqlite:src/resources/wordsList.db";
  public static String urlTxt = "src/resources/dictionary.txt";
  public static String newUrl;
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
      stm.execute("PRAGMA  case_sensitive_like = true;");
      ResultSet rs = stm
          .executeQuery("SELECT * FROM words WHERE "
              + "word LIKE '" + word + "\t%'"
              + " OR word LIKE '%\t" + word + "\t%' "
              + " OR word LIKE '% " + word + "' ;");
      rs.next();
      if(rs.isClosed()){
        return "";
      }
      String result = rs.getString(1);
      conn.close();
      stm.close();
      if(result == ""){

      }
      return result;


    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "";
  }

  /*
  @author jawinter
  This function returns true if the string is in database consisting of dictionary
   */
  static public boolean checkWord(String word) {
    boolean exists = false;
    if(words.contains(word.toUpperCase())) {
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


  public static void importTextToDB() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (final ClassNotFoundException e) {
      System.out.println(e);
    }
    int count = 0;
    try (Connection conn = DriverManager.getConnection(WordCheckDB.url)) {
      java.sql.Statement stm = conn.createStatement();
      stm.execute("DROP TABLE IF EXISTS words;");
      stm.execute("CREATE TABLE words (word VARCHAR(100) PRIMARY KEY)");
      PreparedStatement ps = conn.prepareStatement("INSERT INTO words (word) values (?);");

      File file = new File(urlTxt);    //creates a new file instance
      FileReader fr = new FileReader(file);
      BufferedReader reader = new BufferedReader(fr);
      String line;
      conn.setAutoCommit(false);
      while ((line = reader.readLine()) != null) {
        count++;

        ps.setString(1, line);
        ps.execute();
      }
      conn.commit();
      conn.setAutoCommit(true);



      /*
        stm.execute(" LOAD DATA LOCAL INFILE  words"
          + "FROM '"+ urlTxt +"'"
          + "WITH "
          + "    FIELDTERMINATOR = '\t'"
          + "    ROWTERMINATOR = '\\n'"
          + "  ");
       */

      fr.close();
      reader.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public static void loadNewLibrary(String path){
    newUrl = path;
    try{
      importTextToDB();
    }catch (Exception e){
      newUrl = urlTxt;
      importTextToDB();
    }
  }


}
