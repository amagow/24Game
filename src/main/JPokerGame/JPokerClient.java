package JPokerGame;

import Common.JPokerInterface;

import java.awt.BorderLayout;
import java.rmi.Naming;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JPokerClient  extends JFrame implements Runnable{
	private JPokerInterface gameProvider;
	private String username = "";
	
	public JPokerClient() {
		try {
			gameProvider = (JPokerInterface) Naming.lookup("Server.JPokerServer");
		} catch (Exception e) {
			System.err.println("Failed accessing RMI: " + e);
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new JPokerClient());
	}
	@Override
	public void run() {
		JPokerClient frame = new JPokerClient();

		RegisterDialog registerDialog = new RegisterDialog(frame, false, gameProvider);
		LoginDialog loginDialog = new LoginDialog(frame, false, registerDialog, gameProvider);
		MyTabbedPane tabbedPane = new MyTabbedPane(gameProvider);
		
		loginDialog.setVisible(true);
		
		frame.setTitle("JPoker 24-Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(tabbedPane, BorderLayout.CENTER);
		
		frame.pack();
	}
	public void setUsername(String text) {
		username = text;
		
	}
	public String getUsername() {
		return username;
	}
}
