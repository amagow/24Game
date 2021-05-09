package JPokerGame;

import Common.JPokerInterface;
import JPokerGame.Panel.LeaderboardPanel;
import JPokerGame.Panel.PlayGamePanel;
import JPokerGame.Panel.UserProfilePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MyTabbedPane extends JPanel implements ActionListener {

    private JPokerInterface gameClient;

    public MyTabbedPane(JPokerInterface gameClient) {
        super(new GridLayout(1, 100));
        this.gameClient = gameClient;
        JTabbedPane tabbedPane = new JTabbedPane();

        JComponent panel1 = new UserProfilePanel();
        tabbedPane.addTab("User Profile", null, panel1, "User Profile");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = new PlayGamePanel();
        tabbedPane.addTab("Play Game", null, panel2, "Play Game");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = new LeaderboardPanel();
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
