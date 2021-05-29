package frontend.screens.controllertools;

import backend.basic.Tile;
import backend.basic.Tile.Tilestatus;

/*@autor nilschae
* @version 1.0
* @description a class to hold different variables for the letter set screen*/
public final class LetterSetHolder {
  private final static LetterSetHolder INSTANCE = new LetterSetHolder();
  private Tile[] tileSet;
  private char[] possibleLetters = {
      'A','B','C','D','E','F','G',
      'H','I','J','K','L','M','N',
      'O','P','Q','R','S','T','U',
      'V','W','X','Y','Z'
  };

  private LetterSetHolder() {
    setDefaultLetterSet();
  }

  public static LetterSetHolder getInstance(){
    return INSTANCE;
  }

  /*@description create a default letter set with 100 tiles*/
  private void setDefaultLetterSet() {
    tileSet = new Tile[100];
    for (int i = 0; i < tileSet.length; i++) {
      if (i < 9) {
        tileSet[i] = new Tile('A', 1);
      } else if (i < 11) {
        tileSet[i] = new Tile('B', 3);
      } else if (i < 13) {
        tileSet[i] = new Tile('C', 3);
      } else if (i < 17) {
        tileSet[i] = new Tile('D', 2);
      } else if (i < 29) {
        tileSet[i] = new Tile('E', 1);
      } else if (i < 31) {
        tileSet[i] = new Tile('F', 4);
      } else if (i < 34) {
        tileSet[i] = new Tile('G', 2);
      } else if (i < 36) {
        tileSet[i] = new Tile('H', 4);
      } else if (i < 45) {
        tileSet[i] = new Tile('I', 1);
      } else if (i < 46) {
        tileSet[i] = new Tile('J', 8);
      } else if (i < 47) {
        tileSet[i] = new Tile('K', 5);
      } else if (i < 51) {
        tileSet[i] = new Tile('L', 1);
      } else if (i < 53) {
        tileSet[i] = new Tile('M', 3);
      } else if (i < 59) {
        tileSet[i] = new Tile('N', 1);
      } else if (i < 67) {
        tileSet[i] = new Tile('O', 1);
      } else if (i < 69) {
        tileSet[i] = new Tile('P', 3);
      } else if (i < 70) {
        tileSet[i] = new Tile('Q', 10);
      } else if (i < 76) {
        tileSet[i] = new Tile('R', 1);
      } else if (i < 80) {
        tileSet[i] = new Tile('S', 1);
      } else if (i < 86) {
        tileSet[i] = new Tile('T', 1);
      } else if (i < 90) {
        tileSet[i] = new Tile('U', 1);
      } else if (i < 92) {
        tileSet[i] = new Tile('V', 4);
      } else if (i < 94) {
        tileSet[i] = new Tile('W', 4);
      } else if (i < 95) {
        tileSet[i] = new Tile('X', 8);
      } else if (i < 97) {
        tileSet[i] = new Tile('Y', 4);
      } else if (i < 98) {
        tileSet[i] = new Tile('Z', 10);
      } else {
        tileSet[i] = new Tile(true, Tilestatus.INBAG);
      }
    }
  }

  public char[] getPossibleLetters() {
    return possibleLetters;
  }

  public Tile[] getTileSet() {
    return tileSet;
  }

  public void setTileSet(Tile[] tileSet) {
    this.tileSet = tileSet;
  }
}
