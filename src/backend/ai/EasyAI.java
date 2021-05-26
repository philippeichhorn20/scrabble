package backend.ai;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;

/*
@author jawinter
EasyAI uses Brain to find simple possible moves.
 */

public class EasyAI extends Player {

  public Brain easyBrain;
  public Tile[] tilesOnHand = new Tile[7];

  public EasyAI(String name, String color, Playerstatus status, ScrabbleBoard board) {
    super(name, color, status);
    this.easyBrain = new Brain(board);

  }

}
