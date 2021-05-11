package JPokerGame.Panel;

import Common.JPokerUserTransferObject;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class PlayingGamePanel extends JPanel {
    private final JPokerUserTransferObject[] players;
    private final String[] cards;
    private final JPokerUserTransferObject user;

    public PlayingGamePanel(JPokerUserTransferObject[] players, String[] cards, JPokerUserTransferObject user) {
        this.players = players;
        this.cards = cards;
        this.user = user;
        
        setLayout(new BorderLayout());
        
        JPanel cardsAndInputPanel = new JPanel(new BorderLayout());

        cardsAndInputPanel.add(new CardsPanel(), BorderLayout.CENTER);
        cardsAndInputPanel.add(new InputPanel(), BorderLayout.SOUTH);

        add(cardsAndInputPanel, BorderLayout.CENTER);
        add(new UsersPanel(), BorderLayout.LINE_END);
    }

    private class UserPanel extends JPanel {
        public UserPanel(JPokerUserTransferObject user) {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1,
                    1, 1, 1, Color.BLACK), BorderFactory.createEmptyBorder(10,
                    10, 10, 10)));


            JLabel wininfo = new JLabel("Wins: " + user.getWins() + "/"
                    + user.getGames() + " " + "Avg: " + user.getAverageTimeToWin() + "s");

            add(new JLabel(user.getName()));
            add(wininfo);
        }
    }

    private class UsersPanel extends JPanel {
        public UsersPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            for (JPokerUserTransferObject player : players) {
                add(new UserPanel(player));
            }
            add(Box.createVerticalGlue());
        }
    }

    private class CardPanel extends JPanel {
        public CardPanel(String card) {
            setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            String url = "images/" + card;
            ImageIcon cardIcon = new ImageIcon(url);
            Image img = cardIcon.getImage();
            Image resized = img.getScaledInstance(90, 120,
                    Image.SCALE_SMOOTH);

            setPreferredSize(new Dimension(90, 110));
            add(new JLabel(new ImageIcon(resized)));
        }
    }

    private class CardsPanel extends JPanel {
        public CardsPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel inner = new JPanel();
            inner.setLayout(new GridLayout(1, 0));
            for (String card : cards) {
                inner.add(new CardPanel(card));
            }

            add(Box.createVerticalGlue());
            add(inner, BoxLayout.Y_AXIS);
            add(Box.createVerticalGlue());
        }
    }

    private class InputPanel extends JPanel {

        public InputPanel() {
            JTextField inputField = new JTextField();
            JLabel result = new JLabel("= ?");
            inputField.setPreferredSize(new Dimension(300, 40));

            add(inputField);
            add(result);
        }
    }
}
