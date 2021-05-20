package frontend.screens.controllers;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import frontend.MultiUserChat.Server.Client;
import frontend.MultiUserChat.Server.ClientWindow;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

public class ChatWindowController {
	@FXML private static TextArea chatTextArea;
	@FXML private Button bttnSend;
	@FXML private TextFlow typeField;
	

	public void send(ActionEvent e) throws IOException {
			
		bttnSend.addActionListener(e -> {
			if(!messageField.getText().equals("")) {			
			
			client.send(messageField.getText());
			messageField.setText("");
			}
		});
		

	}
	
	public static void printToConsole(String message) {
		
		chatTextArea.setText(chatTextArea.getText() + message + "\n");
 		
	}
}



}
