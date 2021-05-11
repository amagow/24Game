package JPokerGame.Panel;

import Common.JPokerInterface;
import Common.JPokerUserTransferObject;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class PlayingGamePanel extends JPanel {
    private final JPokerUserTransferObject[] players;
    private final String[] cards;
    private final JPokerUserTransferObject user;
    private final JPokerInterface gameProvider;

    public PlayingGamePanel(JPokerInterface gameProvider, JPokerUserTransferObject[] players, String[] cards, JPokerUserTransferObject user) {
        this.gameProvider = gameProvider;
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

    private static class UserPanel extends JPanel {
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

    private static class CardPanel extends JPanel {
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

    private class InputPanel extends JPanel implements ActionListener {
        private final JTextField inputField = new JTextField();
        private final JLabel result = new JLabel("= ?");

        public InputPanel() {

            inputField.setPreferredSize(new Dimension(300, 40));
            inputField.addActionListener(this);

            add(inputField);
            add(result);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String validatedAnswer = gameProvider.getAnswer(user, inputField.getText());
                System.out.println(validatedAnswer);
                if (validatedAnswer.equals("?"))
                    JOptionPane.showMessageDialog(null, "The string can only contain cards shown on the screen.",
                            "Invalid String", JOptionPane.ERROR_MESSAGE);
                else
                    result.setText(validatedAnswer);
            } catch (ScriptException | RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }
}
