package JPokerGame.Panel;

import Common.JPokerInterface;
import Common.JPokerUserTransferObject;
import JPokerGame.JPokerClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel implements ActionListener {
    private final PlayGamePanel parent;
    private final JPokerClient gameClient;

    public GameOverPanel(PlayGamePanel parent, JPokerClient gameClient, JPokerUserTransferObject winner, String winningFormula) {
        this.parent = parent;
        this.gameClient = gameClient;

        setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JLabel winningUsernameLabel = new JLabel("Winner: " + winner.getName());
        winningUsernameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        winningUsernameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel winningFormulaLabel = new JLabel("Formula" + winningFormula);
        winningFormulaLabel.setFont(new Font("Serif", Font.BOLD, 26));
        winningFormulaLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        labelPanel.add(Box.createVerticalGlue());
        labelPanel.add(winningUsernameLabel);
        labelPanel.add(winningFormulaLabel);
        labelPanel.add(Box.createVerticalGlue());

        JButton newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(100, 40));
        newGameButton.addActionListener(this);

        add(labelPanel, BorderLayout.CENTER);
        add(newGameButton, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.setStage(PlayGamePanel.stages.GAME_JOINING);
        gameClient.sendUserMessage();
    }
}
