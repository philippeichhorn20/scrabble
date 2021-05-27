package backend.network.messages.points;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/*
@author peichhor
 */
public class PlayFeedbackMessage extends Message {

  String feedback;
  boolean successfulMove;

  public PlayFeedbackMessage(String from, String feedback, boolean successfulMove) {
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

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
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
