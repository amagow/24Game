package JPokerGame.Panel;

import JPokerGame.JPokerClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGamePanel extends JPanel implements ActionListener {
    private final JPokerClient gameClient;
    private final JButton newGameButton = new JButton("New Game");

    private final PlayGamePanel parent;

    public NewGamePanel(PlayGamePanel parent, JPokerClient gameClient) {
        super(false);

        this.parent = parent;
        this.gameClient = gameClient;

        setLayout(new BorderLayout());
        add(newGameButton, BorderLayout.CENTER);

        newGameButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.setStage(PlayGamePanel.stages.GAME_JOINING);
        gameClient.sendUserMessage();
    }
}
