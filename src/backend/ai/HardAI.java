package backend.ai;

import backend.basic.Player;
import backend.basic.ScrabbleBoard;

public class HardAI extends Player {

  Brain hardBrain;

  public HardAI(String name, String color, Playerstatus status, ScrabbleBoard board) {
    super(name, color, status);
    this.hardBrain = new Brain(board);
  }
}
