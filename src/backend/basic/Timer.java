package backend.basic;

/*
@peichhor
This class represents a clock
@param timerOverall keeps track of how long the game has been going on
@param timerOverall keeps track of how long this turn has been going on
@param isRunning keeps track of weather or not the game is running
 */
public class Timer extends Thread {

  long timerOverall = 0; //in seconds
  long timerCurrentPlayer = 0; //in seconds
  boolean isRunning = false;

  /*
 this function runs the Thread that updates the timers
   */
  @Override
  public void run() {
    isRunning = true;
    while (isRunning) {
      try {
        sleep(1000);
        timerOverall += 1;
        timerCurrentPlayer += 1;
      } catch (InterruptedException exe) {
        exe.printStackTrace();
      }
    }
  }

  public long getTimerOverall() {
    return timerOverall;
  }

  public long getTimerCurrentPlayer() {
    return timerCurrentPlayer;
  }

  /*
  is called when the next turn is triggered. It resets the timer of current turn
   */
  public void nextPlayer() {
    timerCurrentPlayer = 0;
  }

  public void stopTimer() {
    this.isRunning = false;
  }
}
