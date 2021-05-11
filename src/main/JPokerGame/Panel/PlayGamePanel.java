package JPokerGame.Panel;

import Common.Messages.CardsMessage;
import JPokerGame.JPokerClient;

import javax.swing.*;
import java.awt.*;

public class PlayGamePanel extends JPanel {
    private final JPokerClient gameClient;
    private final NewGamePanel newGameCard;
    private final WaitingGamePanel waitingGameCard = new WaitingGamePanel();

    private stages stage = stages.INITIAL;

    public PlayGamePanel(JPokerClient gameClient) {
        super(new CardLayout());

        this.gameClient = gameClient;
        newGameCard = new NewGamePanel(this, gameClient);

        add("" + stages.INITIAL, newGameCard);
        add("" + stages.GAME_JOINING, waitingGameCard);
    }

    public void setStage(stages stage) {
        this.stage = stage;
        CardLayout cl = (CardLayout) (this.getLayout());
        cl.show(this, "" + stage);
    }

    public void createPlayingGamePanel(CardsMessage message) {
        PlayingGamePanel panel = new PlayingGamePanel(gameClient.getGameProvider(), message.getUsers(), message.getCards(), gameClient.getUser());
        add("" + stages.GAME_PLAYING, panel);
        setStage(PlayGamePanel.stages.GAME_PLAYING);
    }

    public enum stages {
        INITIAL,
        GAME_JOINING,
        GAME_PLAYING,
        GAME_OVER
    }
}
