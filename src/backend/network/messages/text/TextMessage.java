package backend.network.messages.text;

import backend.network.messages.Message;
import backend.network.messages.MessageType;

/**
 * 
 * @author vivanova
 *
 */
public class TextMessage extends Message {
	private String text = "";

	public TextMessage(String from, String text) {
		super(MessageType.TEXT, from);
		this.text = new String(text);
	}
	

	  public void setText(String text) {
		  this.text = text;
	  }

	  public String getText() {
		  return this.text;
	  }
	
}
