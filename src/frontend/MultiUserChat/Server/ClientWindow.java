package frontend.MultiUserChat.Server;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
/** @author vivanova
* @version 1.0
*/
public class ClientWindow {

	private Frame frame;
	private TextField messageField;
	private static TextArea textArea = new TextArea();
	
	private Client client;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					ClientWindow window = new ClientWindow();
					window.frame.setVisible(true);
				}catch(Exception e) {
					e.printStackTrace();	
				}
			}
		});

	}

	public ClientWindow() {
		initialize();
		 
		// this is to test if it works. Should be changed to name from user profile later
		String name = JOptionPane.showInputDialog("Enter Name");
		client = new Client(name, "localhost", 52864);
	}

	private void initialize() {
		frame = new Frame();
		frame.setResizable(true);
		frame.setTitle("Chat\r\n");
		frame.setBounds(100, 100, 638, 472);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		((JFrame) frame).getContentPane().setLayout(new BorderLayout(0,0));
		
		textArea.setEditable(false);
		
		ScrollPane scrollPane = new ScrollPane(textArea);
		//((JFrame) frame).getContentPane().add(scrollPane);
		
		Panel panel = new Panel();
	    ((JFrame) frame).getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		TextField messageField = new TextField();
		//panel.add(messageField);
		//messageField.setColumns(50);
		
		Button btnSend = new Button("Send");
		btnSend.addActionListener(e -> {
			if(!messageField.getText().equals("")) {			
			
			client.send(messageField.getText());
			messageField.setText("");
			}
		});
		
		panel.add(btnSend);
		
		frame.setLocationRelativeTo(null);

	}
	
	public static void printToConsole(String message) {
		
		textArea.setText(textArea.getText() + message + "\n");
 		
	}
}

