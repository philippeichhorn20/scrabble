package frontEnd.MultiUerChat.Client;

import java.awt.EventQueue;
import java.awt.Frame;
import javax.swing.UIManager;

public class ClientChatWindow {

	private Frame frame;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChatWindow window = new ClientChatWindow();
					window.frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}catch(Exception e) {
					e.printStackTrace();	
				}
			}
		});

	}

	public ClientChatWindow() {
		initialize();
	}

	private void initialize() {
		frame = new Frame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}
