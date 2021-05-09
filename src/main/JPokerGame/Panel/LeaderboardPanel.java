package JPokerGame.Panel;

import javax.swing.*;
import java.awt.*;

public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel() {
        super(false);
        this.setLayout(new GridLayout(1, 0));
        String[] columnNames = {"Rank", "Player", "Games Won", "Games Played", "Avg. Time to Win"};
        Object[][] data = {{1, "Player 1", 10, 20, 14.5}, {2, "Player 2", 9, 14, 14.5},
                {3, "Player 3", 8, 15, 14.5}, {4, "Player 4", 7, 11, 14.5}, {5, "Player 5", 7, 12, 14.5},
                {6, "Player 6", 6, 10, 14.5}, {7, "Player 7", 5, 10, 14.5}, {8, "Player 8", 5, 12, 14.5},
                {9, "Player 10", 2, 5, 14.5}, {10, "Player 9", 0, 2, 14.5}};

        final JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);
    }

}