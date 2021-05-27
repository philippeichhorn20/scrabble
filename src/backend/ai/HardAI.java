package backend.ai;

import java.util.Random;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;
/*
 * @author vivanova
 */
public class HardAI extends Player {

  Brain hardBrain;

  public HardAI(String name, String color, Playerstatus status, ScrabbleBoard board) {
    super(name, color, status);
    this.hardBrain = new Brain(board);
}
  
  //TODO Play only on my turn Method
  
  
  public void play() throws Exception {
	  //TODO add a time before we skip turn

	  // Possible moves to make
	  var moves = hardBrain.getPlayableWords(getRack(), hardBrain.getWordPossibilities());
	  int points = 0;
	  PossibleWord pick = null;
	  // Create an iterator to move thru the TreeSet
	  var it = moves.iterator(); 
	  // We loop until the Tree set has a next member. 
	  while(it.hasNext()) {
		  // Save current word in Temporary Storage
		  var temp =  it.next();
		  // If we haven't picked a word or if we find a word with more points
		  if(pick == null || points < temp.getPoints()) {
			  // We save it.
			  pick = temp;
		  }
		  
	  }
	  // If our word has been picked we retrieve the tiles
	  if(pick  != null ) {
		  var word = pick.getTile();
		  // TODO send words to Server
	  }
	  else {
		  throw new Exception("Word null Exception");
	  }
	  // add tiles 
	  
	  
  }
}
