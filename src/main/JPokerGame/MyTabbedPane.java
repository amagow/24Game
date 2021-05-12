package JPokerGame;

import Common.JPokerInterface;
import Common.JPokerUser;
import Common.JPokerUserTransferObject;
import JPokerGame.Panel.LeaderboardPanel;
import JPokerGame.Panel.PlayGamePanel;
import JPokerGame.Panel.UserProfilePanel;
import apple.laf.JRSUIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;

public class MyTabbedPane extends JPanel implements ActionListener {
    private final JPokerClient parent;
    private final JPokerInterface gameClient;
    private final JPokerUserTransferObject user;
    private final JTabbedPane tabbedPane;
    private final SwingWorker<JPokerUserTransferObject, Void> logoutWorker = new SwingWorker<JPokerUserTransferObject, Void>() {
        @Override
        protected JPokerUserTransferObject doInBackground() {
            try {
                JPokerUserTransferObject user = parent.getUser();
                if (user != null) {
                    gameClient.logout(user.getName());
                    return user;
                }
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, e1.getCause(), "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            try {
                if (get() != null)
                    System.exit(0);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    };
    private PlayGamePanel playGamePanel;

    public MyTabbedPane(JPokerClient parent, JPokerInterface gameProvider, JPokerUserTransferObject user) {
        super(new GridLayout(1, 100));

        this.parent = parent;
        this.user = user;
        this.gameClient = gameProvider;

        tabbedPane = new JTabbedPane();

        JComponent panel1 = new UserProfilePanel(user);
        tabbedPane.addTab("User Profile", null, panel1, "User Profile");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        playGamePanel = new PlayGamePanel(parent);
        tabbedPane.addTab("Play Game", null, playGamePanel, "Play Game");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = new LeaderboardPanel(gameProvider);
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

    public PlayGamePanel getPlayGamePanel() {
        return playGamePanel;
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
        logoutWorker.execute();
    }
}
