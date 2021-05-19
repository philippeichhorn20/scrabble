package backend.ai;

import backend.basic.Player;

public class HardAI extends Player {

  Brain hardBrain = new Brain();

  public HardAI(String name, String color, Playerstatus status) {
    super(name, color, status);
  }
}
