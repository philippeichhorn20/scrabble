package backend.ai;

import backend.basic.Player;

/*
@author jawinter
EasyAI uses Brain to find simple possible moves.
 */

public class EasyAI extends Player {

  public Brain easyBrain = new Brain();

  public EasyAI(String name, String color, Playerstatus status) {
    super(name, color, status);
  }
}
