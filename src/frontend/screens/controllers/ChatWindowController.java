package frontend.screens.controllers;

/*
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import  javafx.scene.text.Text;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import animatefx.animation.FadeIn;
import frontend.Main;
import frontend.MultiUserChat.Server.Client;
import frontend.MultiUserChat.Server.ClientWindow;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;

public class ChatWindowController {
	@FXML private static TextArea chatTextArea;
	@FXML private Button btnSend;
	@FXML private TextField typeField;
	
	private Client client;
	Socket socket;
	 BufferedReader reader;
	    PrintWriter writer;
	
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					ClientWindow window = new ClientWindow();
					
				}catch(Exception e) {
					e.printStackTrace();	
				}
			}
		});

	}
	
	 public void connectSocket() {
	        try {
	            socket = new Socket("localhost", 8889);
	            System.out.println("Socket is connected with server!");
	            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            writer = new PrintWriter(socket.getOutputStream(), true);
	            this.start();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	@Override
	 public void run() {
	        try {
	            while (true) {
	                String msg = reader.readLine();
	                String[] tokens = msg.split(" ");
	                String cmd = tokens[0];
	                System.out.println(cmd);
	                StringBuilder fulmsg = new StringBuilder();
	                for(int i = 1; i < tokens.length; i++) {
	                    fulmsg.append(tokens[i]);
	                }
	                System.out.println(fulmsg);
	                if (cmd.equalsIgnoreCase(Main.profile.getName() + ":")) {
	                    continue;
	                } else if(fulmsg.toString().equalsIgnoreCase("bye")) {
	                    break;
	                }
	                chatTextArea.appendText(msg + "\n");
	            }
	            reader.close();
	            writer.close();
	            socket.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	public ChatWindowController() {
		initialize(null, null);
		 
		// this is to test if it works. Should be changed to name from user profile later
		//String name = JOptionPane.showInputDialog("Enter Name");
		//client = new Client(name, "localhost", 52864);
	}
	
	@Override
	 public void initialize(URL location, ResourceBundle resources) {
		 connectSocket();
	 }


//Send message to all 
	
	 public void handleSendEvent(MouseEvent event) {
	        send();
	        for(User user : users) {
	            System.out.println(user);
	        }
	    }


	    public void send() {
	        String msg = typeField.getText();
	        writer.println(Main.profile.getName() + ": " + msg);
	       
	        chatTextArea.appendText("Me: " + msg + "\n");
	        typeField.setText("");
	        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
	            System.exit(0);
	        }
	    }
	  
	
	    // Prints message on the CHAT CONSOLE
	public static void printToConsole(String message) {
		
		chatTextArea.setText(chatTextArea.getText() + message + "\n");
 		
	}
	  public void sendMessageByKey(KeyEvent event) {
	        if (event.getCode().toString().equals("ENTER")) {
	            send();
	        }
	    }
}
*/