package JPokerGame;

import Common.JPokerInterface;
import JPokerGame.JPokerClient;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MyTabbedPane extends JPanel implements ActionListener {

	private JPokerInterface gameClient;

	public MyTabbedPane(JPokerInterface gameClient) {
		super(new GridLayout(1, 100));
		this.gameClient = gameClient;
		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent panel1 = userProfilePanel();
		tabbedPane.addTab("User Profile", null, panel1, "User Profile");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeTextPanel("Panel #2");
		tabbedPane.addTab("Play Game", null, panel2, "Play Game");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		JComponent panel3 = leaderBoardPanel();
		tabbedPane.addTab("Leader Board", null, panel3, "Leader Board");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		JComponent temp = new JPanel();
		tabbedPane.addTab("Logout", null, temp, "Logout Client");

		JButton logoutButton = new JButton("Logout");
		logoutButton.setContentAreaFilled(false);
		logoutButton.setBorderPainted(false);
		logoutButton.addActionListener(this);
		tabbedPane.setTabComponentAt(3, logoutButton);
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		// Add the tabbed pane to this panel.
		add(tabbedPane);
	}

	private JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	private JComponent userProfilePanel() {
		JPanel panel = new JPanel(false);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		int wins = 10;
		int games = 20;
		double timeToWin = 15.5;
		int rank = 10;

		panel.setLayout(new GridLayout(0, 1));
		String[] sArr = { "Kevin", "Number of wins : " + wins, "Number of games : " + games,
				"Average time to win: " + timeToWin, "Rank: #" + rank };

		for (int i = 0; i < sArr.length; i++) {
			JLabel label = new JLabel(sArr[i]);
			if (i == 0 || i == sArr.length - 1)
				label.setFont(new Font("", Font.BOLD, 20));
			panel.add(label);
		}
		return panel;
	}

	private JComponent leaderBoardPanel() {
		JPanel panel = new JPanel(false);
		panel.setLayout(new GridLayout(1, 0));
		String[] columnNames = { "Rank", "Player", "Games Won", "Games Played", "Avg. Time to Win" };
		Object[][] data = { { 1, "Player 1", 10, 20, 14.5 }, { 2, "Player 2", 9, 14, 14.5 },
				{ 3, "Player 3", 8, 15, 14.5 }, { 4, "Player 4", 7, 11, 14.5 }, { 5, "Player 5", 7, 12, 14.5 },
				{ 6, "Player 6", 6, 10, 14.5 }, { 7, "Player 7", 5, 10, 14.5 }, { 8, "Player 8", 5, 12, 14.5 },
				{ 9, "Player 10", 2, 5, 14.5 }, { 10, "Player 9", 0, 2, 14.5 } };

		final JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 300));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JPokerClient client = (JPokerClient) SwingUtilities.getAncestorOfClass(JPokerClient.class, this);
		new Thread(() -> {
			try {
				String username = client.getUsername();
				gameClient.logout(username);
				System.exit(0);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, e1.getCause(), "Information",
						JOptionPane.INFORMATION_MESSAGE);
				e1.printStackTrace();
			}
		}).start();
	}
}
