package JPokerGame.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayGamePanel extends JPanel implements ActionListener {
    JButton newGameButton = new  JButton("New Game");
    public PlayGamePanel() {
        super(false);
        this.setLayout(new BorderLayout());
        this.add(newGameButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
