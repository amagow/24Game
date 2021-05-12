package JPokerGame.Panel;

import Common.JPokerInterface;
import Common.JPokerUserTransferObject;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel(JPokerInterface gameProvider) {
        super(false);

        try {
            JPokerUserTransferObject[] users = gameProvider.getLeaderBoard();

            this.setLayout(new GridLayout(1, 0));
            String[] columnNames = new String[]{"Rank", "Player", "Games Won", "Games Played", "Avg. Time to Win"};

            ArrayList<Object[]> data = new ArrayList<>();
            for (int i = 0; i < users.length - 1; i++) {
                Object[] userArr = new Object[6];
                userArr[0] = users[i].getRank();
                userArr[1] = users[i].getName();
                userArr[2] = users[i].getWins();
                userArr[3] = users[i].getGames();
                userArr[4] = String.format("%.2fs", users[i].getAverageTimeToWin());

                data.add(userArr);
            }

            final JTable table = new JTable(data.stream().toArray(Object[][]::new), columnNames);
            table.setPreferredScrollableViewportSize(new Dimension(500, 300));
            table.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}