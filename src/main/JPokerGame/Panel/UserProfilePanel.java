package JPokerGame.Panel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserProfilePanel extends JPanel {
    public UserProfilePanel() {
        super(false);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        int wins = 10;
        int games = 20;
        double timeToWin = 15.5;
        int rank = 10;

        this.setLayout(new GridLayout(0, 1));
        String[] sArr = {"Kevin", "Number of wins : " + wins, "Number of games : " + games,
                "Average time to win: " + timeToWin, "Rank: #" + rank};

        for (int i = 0; i < sArr.length; i++) {
            JLabel label = new JLabel(sArr[i]);
            if (i == 0 || i == sArr.length - 1)
                label.setFont(new Font("", Font.BOLD, 20));
            this.add(label);
        }
    }
}