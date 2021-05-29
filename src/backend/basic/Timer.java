package backend.basic;

import frontend.screens.controllers.GameScreenController;
import java.io.Serializable;

/*
@peichhor
This class represents a clock
@param timerOverall keeps track of how long the game has been going on
@param timerOverall keeps track of how long this turn has been going on
@param isRunning keeps track of weather or not the game is running
 */
public class Timer extends Thread implements Serializable {

  private long timerOverall = 0; //in seconds
  private long timerCurrentPlayer = 0; //in seconds
  private boolean isRunning = false;
  private GameScreenController gameScreenController;
  private ServerMatch serverMatch;

  public Timer(ServerMatch serverMatch){
    this.serverMatch = serverMatch;
  }
  public Timer(){

  }

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
      if (gameScreenController!= null && timerCurrentPlayer == 60 * 9) {
        gameScreenController.newHistoryMessage("you have 60 seconds left");
      }else if (gameScreenController!= null && timerCurrentPlayer == 60 * 9.5){
        gameScreenController.newHistoryMessage("you have 30 seconds left");
      }else if(serverMatch!= null && timerCurrentPlayer == 60 * 10){
        serverMatch.gameOver();
      }else if(serverMatch!= null && timerCurrentPlayer == 60 * 10){
        serverMatch.sendTimeIsUp();
      }
    }
  }

  public long getTimerOverall() {
    return timerOverall;
  }

  public long getTimerCurrentPlayer() {
    return timerCurrentPlayer;
  }

  public String getTimerCurrentPlayerString() {
    String timeString = "";
    if(timerCurrentPlayer > 60){
      timeString += timerCurrentPlayer / 60+" minutes and " + timerCurrentPlayer % 60 +" seconds left";
    }else{
      timeString += timerCurrentPlayer +" seconds left";
    }
    return timeString;
  }

  /*
  is called when the next turn is triggered. It resets the timer of current turn
   */
  public void nextPlayer() {
    timerCurrentPlayer = 0;
  }

  public void setTimerCurrentPlayer(long timerCurrentPlayer) {
    this.timerCurrentPlayer = timerCurrentPlayer;
  }

  public void setGameScreenController(
      GameScreenController gameScreenController) {
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
}
