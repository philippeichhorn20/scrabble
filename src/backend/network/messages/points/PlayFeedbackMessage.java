package backend.network.messages.points;

import backend.network.messages.Message;
import backend.network.messages.MessageType;
import java.util.ArrayList;

/*
@author peichhor
 */
public class PlayFeedbackMessage extends Message {

  ArrayList<String> feedback;
  boolean successfulMove;

  public PlayFeedbackMessage(String from, ArrayList<String> feedback, boolean successfulMove) {
    super(MessageType.PLAY_FEEDBACK, from);
      this.feedback = feedback;
      this.successfulMove = successfulMove;


  }






  @Override
  public String getFrom() {
    return super.getFrom();
  }

  @Override
  public void setFrom(String name) {
    super.setFrom(name);
  }

  public ArrayList<String> getFeedback() {
    return feedback;
  }

  public void setFeedback(ArrayList<String> feedback) {
    this.feedback = feedback;
  }

  @Override
  public MessageType getMessageType() {
    return super.getMessageType();
  }

  public void setSuccessfulMove(boolean successfulMove) {
    this.successfulMove = successfulMove;
  }

  public boolean isSuccessfulMove() {
    return successfulMove;
  }
}
