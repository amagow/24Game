package JPokerGame.Panel;

import javax.swing.*;
import java.awt.*;

public class WaitingGamePanel extends JPanel {
    private JLabel waitingLabel = new JLabel("Waiting for players...");

    public WaitingGamePanel() {
        super(new BorderLayout());
        waitingLabel.setHorizontalAlignment(JLabel.CENTER);
        add(waitingLabel, BorderLayout.CENTER);
    }
}
