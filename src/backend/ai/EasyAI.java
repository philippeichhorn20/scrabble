package backend.ai;

import java.util.Random;
import backend.basic.Player;
import backend.basic.ScrabbleBoard;

/*
@author vivanova
EasyAI uses Brain to find simple possible moves.
 */

public class EasyAI extends Player {

  public Brain easyBrain;
  private Random r = new Random(System.currentTimeMillis());

  public EasyAI(String name, String color, Playerstatus status, ScrabbleBoard board) {
    super(name, color, status);
    this.easyBrain = new Brain(board);
  }
  
  public void play() throws Exception {
	  var moves = easyBrain.getPlayableWords(getRack(), easyBrain.getWordPossibilities());
	  // Introduce randomness
	  var random = r.nextInt(moves.size()-1)+1;
	  // Introduce a counter so we can 
	  int counter = 0;
	  PossibleWord pick = null;
	  var it = moves.iterator(); 
	  // Loop through words until we are finished with the array or until we hit the random number.
	  while(it.hasNext() && (counter != random)) {
		  pick = it.next();
		  counter++;
	  }
	  // If word is empty throw Exception
	  if(pick  != null ) {
		  var word = pick.getTile();
	  }
	  else {
		  throw new Exception("Word null Exception");
	  }
	  // add tiles 
	  
	  
  }
}