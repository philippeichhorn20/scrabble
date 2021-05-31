package backend.basic;

import frontend.screens.controllers.GameScreenController;
import java.io.Serializable;

/**
 * This class represents a clock.
 *
 * @author peichhor
 */
public class Timer extends Thread implements Serializable {

  private long timerOverall = 0; // in seconds
  private long timerCurrentPlayer = 0; // in seconds
  private boolean isRunning = false;
  private GameScreenController gameScreenController;
  private ServerMatch serverMatch;
  private ClientMatch clientMatch;
  private int[] clientTimes = new int[4];
  private int currentPlayer;

  /**
   * Constructor with server match initialization.
   *
   * @param serverMatch server match.
   */
  public Timer(ServerMatch serverMatch) {
    this.serverMatch = serverMatch;
  }

  /**
   * Constructor with client match initialization.
   *
   * @param clientMatch
   */
  public Timer(ClientMatch clientMatch) {
    this.clientMatch = clientMatch;
  }

  public Timer() {}

  /**
   * This starts the timer.
   */
  @Override
  public void run() {
    for (int x = 0; x < clientTimes.length; x++) {
      clientTimes[x] = 0;
    }
    isRunning = true;
    while (isRunning) {
      try {
        sleep(1000);
        timerOverall += 1;
        clientTimes[currentPlayer] += 1;
      } catch (InterruptedException exe) {
        exe.printStackTrace();
      }
      if (clientMatch != null && clientTimes[currentPlayer] == 9 * 60) {
        clientMatch.oneMinuteAlert();
      } else if (clientMatch != null && clientTimes[currentPlayer] == 9.5 * 60) {
        clientMatch.thirtySecondsAlert();
      } else if (serverMatch != null && clientTimes[currentPlayer] == 10 * 60) {
        serverMatch.gameOver();
      }
    }
  }

  public long getTimerOverall() {
    return timerOverall;
  }

  public void setTimerTo(int player) {
    currentPlayer = player;
  }

  public long getTimerCurrentPlayer() {
    return 60*10-clientTimes[currentPlayer];
  }

  public long getTimerCurrentPlayerString() {
    return timerCurrentPlayer;
  }

  public void setTimerCurrentPlayer(long timerCurrentPlayer) {
    this.timerCurrentPlayer = timerCurrentPlayer;
  }

  public void setGameScreenController(GameScreenController gameScreenController) {
    this.gameScreenController = gameScreenController;
  }

  public GameScreenController getGameScreenController() {
    return gameScreenController;
  }

  public void setTimerOverall(long timerOverall) {
    this.timerOverall = timerOverall;
  }

  public void stopTimer() {
    this.isRunning = false;
  }

  public void setClientMatch(ClientMatch clientMatch) {
    this.clientMatch = clientMatch;
  }
}
