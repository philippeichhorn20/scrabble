package backend.ai;

import backend.basic.Matchfield;
import backend.basic.Matchfield.Premiumstatus;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile.Tilestatus;
import java.util.ArrayList;

/*
@author jawinter
@version 1.0
@description This class is responsible for finding possible placements of tiles to identify possible
combinations for words, which can be laid.
 */
public class Brain {

  /*
  Method returns List of slots where player could place a word
  @param board is the current scrabbleBoard
   */
  public ArrayList<Matchfield>  getPlayableSlots(ScrabbleBoard board){
    ArrayList<Matchfield> playableSlots = new ArrayList<Matchfield>();
    for(int x=0;x<15;x++) {
      for(int y=0;y<15;y++) {
        Matchfield[] neighbors = getNeighbors(board,x,y);
        if(!(!neighbors[0].hasTile() && !neighbors[1].hasTile() && !neighbors[2].hasTile() && !neighbors[3].hasTile())) {
          playableSlots.add(board.getScrabbleBoard()[x][y]);
        }
      }
    }
    return playableSlots;
  }

  /*
  Method is used for a given matchfield and checks, where a matchfield has a neighboring non-empty
  matchfield.
  @param board is the current board played on
  xPos and yPos give access to the matchfield which is checked for neighbors (we assume coord is
  allowed)
  return value neighbors is an array of length 4, the array fields are empty if no tile is on the
  matchfield or filled with the neighboring tile. [top] [right] [bottom] [left] -> if possibleSlot
  has a neighbor above neighbors[0] returns the tile
   */
  public static Matchfield[] getNeighbors(ScrabbleBoard board, int xPos, int yPos){
    Matchfield[][] boardArray = board.getScrabbleBoard();
    Matchfield[] neighbors = new Matchfield[4];
    Matchfield current = boardArray[xPos][yPos];
    for(int i=0;i<4;i++){
    switch(i) {
      case 0:
        if(yPos-1>=0) {
          if(boardArray[xPos][yPos-1].hasTile()) {
            neighbors[i] = new Matchfield(xPos,yPos-1, Premiumstatus.NOPREMIUM);
            neighbors[i].setTile(boardArray[xPos][yPos-1].getTile());
          }
        }
        break;
      case 1:
        if(xPos+1<15) {
          if(boardArray[xPos+1][yPos].hasTile()) {
            neighbors[i] = new Matchfield(xPos+1,yPos, Premiumstatus.NOPREMIUM);
            neighbors[i].setTile(boardArray[xPos+1][yPos].getTile());
          }
        }
        break;
      case 2:
        if(yPos+1<15) {
          if(boardArray[xPos][yPos+1].hasTile()) {
            neighbors[i] = new Matchfield(xPos,yPos+1, Premiumstatus.NOPREMIUM);
            neighbors[i].setTile(boardArray[xPos][yPos+1].getTile());
          }
        }
        break;
      case 3:
        if(xPos-1>=0) {
          if(boardArray[xPos-1][yPos].hasTile()) {
            neighbors[i] = new Matchfield(xPos-1,yPos, Premiumstatus.NOPREMIUM);
            neighbors[i].setTile(boardArray[xPos-1][yPos].getTile());
          }
        }
        break;
    }
    }
    return neighbors;
  }

}
