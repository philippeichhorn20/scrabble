package frontend.MultiUserChat.Server;

import java.awt.Event;

import backend.basic.GameInformation;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author vivanova
 *
 */
public class Chat{
	private TextArea textArea;
	private Stage window;
	
	public Chat() {
			window = new Stage();
		    window.initModality(Modality.WINDOW_MODAL);
	        window.setTitle("Chat");
	        window.setMinHeight(250);
	        window.setMinWidth(400);
	        textArea = new TextArea();
	        textArea.setEditable(false);
	        TextField inputField = new TextField();
	        
	        Button btnClose = new Button("Close");
	        btnClose.setOnAction(e -> window.close());
	        Button btnSend = new Button("Send");
	        btnSend.setOnAction(e -> {
	            String chatMessage = inputField.getText();
	            if (chatMessage != "") {
	            	GameInformation.getInstance().getClientmatch().sendChatMessage(chatMessage);
		            inputField.setText("");
	            }
	        });
	        VBox layout = new VBox(10);
	        inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent ke) {
				     if (ke.getCode().equals(KeyCode.ENTER))
				     {
				       btnSend.fire();
				     }

					
				}
	        	
	        });
	        layout.getChildren().addAll(textArea,inputField,btnSend,btnClose);
	        layout.setAlignment(Pos.TOP_CENTER);
	        Scene scene = new Scene(layout);
	        window.setScene(scene);
	}

	public void fillTextArea(String from, String text) {
		this.textArea.appendText(from + ": " + text + "\n");
		System.out.println();
	}

	public void display() {
		window.showAndWait();
	}


}
