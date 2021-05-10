package JPokerGame.Panel;

import Common.JPokerUserTransferObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserProfilePanel extends JPanel {
    private final JLabel[] labels = new JLabel[5];
    private JPokerUserTransferObject user;

    public UserProfilePanel(JPokerUserTransferObject user) {
        super(false);

        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new GridLayout(0, 1));

        this.user = user;
        String username = user.getName();
        int wins = user.getWins();
        int games = user.getGames();
        double timeToWin = user.getAverageTimeToWin();
        int rank = user.getRank();

        String[] sArr = {username, "Number of wins : " + wins, "Number of games : " + games,
                "Average time to win: " + timeToWin, "Rank: #" + rank};

        for (int i = 0; i < sArr.length; i++) {
            labels[i] = new JLabel(sArr[i]);
            if (i == 0 || i == sArr.length - 1)
                labels[i].setFont(new Font("", Font.BOLD, 20));
            add(labels[i]);
        }
    }
}