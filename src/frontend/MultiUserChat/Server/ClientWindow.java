package frontend.MultiUserChat.Server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

//<<<<<<< Upstream, based on origin/master
import java.awt.FlowLayout;
//=======
//>>>>>>> 2be3e88 #27 fixed some errors. More to go
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/** @author vivanova
* @version 1.0
*/
public class ClientWindow {

	private JFrame frame;
	private JTextField textField;
	private static JTextArea textArea = new JTextArea();
	
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
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Chat\r\n");
		frame.setBounds(100, 100, 638, 472);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0,0));
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
	    frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(50);
		
		JButton btnSend = new JButton("Send");
		
		btnSend.addActionListener(e -> {
			if(!textField.getText().equals("")) {			
			
			client.send(textField.getText());
			textField.setText("");
			}
		});
		
		panel.add(btnSend);
		
		frame.setLocationRelativeTo(null);

	}
	
	public static void printToConsole(String message) {
		
		textArea.setText(textArea.getText() + message + "\n");
 		
	}
}

