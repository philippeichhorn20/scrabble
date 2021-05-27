package backend.ai;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;
import backend.basic.Tile;
import java.util.ArrayList;
import java.util.TreeSet;

/*
 * @author jawinter
 * @description Class represents AI with hard difficulty
 */
public class HardAI extends PlayerAI {


  public HardAI(String name) {
    super(name);
}
  //choose best Tiles to place
  public void handleTurn(){

    //If placetiles or shuffleTiles dont forget to set them null

  }

  public Brain getHardBrain() {
    return this.brain;
  }
//TODO Play only on my turn Method
  
  /*
  public void play() throws Exception {
	  //TODO add a time before we skip turn

	  // Possible moves to make
	  TreeSet<PossibleWord> moves = hardBrain.getPlayableWords(getRack(), hardBrain.getWordPossibilities());
	  int points = 0;
	  PossibleWord pick = null;
	  // Create an iterator to move thru the TreeSet
	  Iterator it = moves.iterator();
	  // We loop until the Tree set has a next member. 
	  while(it.hasNext()) {
		  // Save current word in Temporary Storage
      PossibleWord temp = (PossibleWord) it.next();
		  // If we haven't picked a word or if we find a word with more points
		  if(pick == null || points < temp.getPoints()) {
			  // We save it.
			  pick = temp;
		  }
		  
	  }
	  // If our word has been picked we retrieve the tiles
	  if(pick  != null ) {
		  ArrayList<Tile> word = pick.getTile();
		  // TODO send words to Server
	  }
	  else {
		  throw new Exception("Word null Exception");
	  }
	  // add tiles 
	  
	  
  }

   */
}
