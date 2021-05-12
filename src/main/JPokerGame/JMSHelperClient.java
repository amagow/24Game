package JPokerGame;

import Common.JMSHelper;
import Common.JPokerUserTransferObject;
import Common.Messages.CardsMessage;
import Common.Messages.GameOverMessage;
import Common.Messages.RoomIdMessage;
import Common.Messages.UserMessage;
import JPokerGame.Panel.GameOverPanel;
import JPokerGame.Panel.PlayGamePanel;
import jakarta.jms.*;

import javax.naming.NamingException;
import java.util.Arrays;

class JMSHelperClient extends JMSHelper {
    private final JPokerUserTransferObject user;
    private final MessageProducer queueSender;
    private final MessageConsumer topicReader;
    private final JPokerClient gameClient;

    public JMSHelperClient(JPokerClient gameClient, JPokerUserTransferObject user) throws JMSException, NamingException {
        this.user = user;
        this.gameClient = gameClient;

        queueSender = this.createQueueSender();
        topicReader = this.createTopicReader();

        topicReader.setMessageListener(new TopicReader());
    }

    public void sendMessage(JPokerUserTransferObject user) {
        UserMessage userMessage = new UserMessage(user);
        try {
            Message message = this.createMessage(userMessage);
            queueSender.send(message);
        } catch (JMSException e) {
            System.err.println("JMSClient: Failed to send message");
        }
    }

    public class TopicReader implements MessageListener {

        @Override
        public void onMessage(Message message) {
            try {
                Object objectMessage = ((ObjectMessage) message).getObject();
                if (objectMessage instanceof RoomIdMessage) {
                    RoomIdMessage roomIdMessage = (RoomIdMessage) objectMessage;

                    if (gameClient.getRoomId() < 0 && gameClient.getUser() != null &&
                            Arrays
                                    .stream(roomIdMessage.getPlayers())
                                    .anyMatch(player -> player.getName().equals(gameClient.getUser().getName()))
                    ) {
                        gameClient.setRoomId(roomIdMessage.getRoomId());
                    }
                }
                if (objectMessage instanceof CardsMessage) {
                    CardsMessage cardsMessage = (CardsMessage) objectMessage;
                    if (gameClient.getRoomId() == cardsMessage.getRoomId()) {
                        PlayGamePanel panel = gameClient.getTabbedPane().getPlayGamePanel();
                        panel.createPlayingGamePanel(cardsMessage);
                    }
                }
                if (objectMessage instanceof GameOverMessage) {
                    GameOverMessage gameOverMessage = (GameOverMessage) objectMessage;
                    if (gameClient.getRoomId() == gameOverMessage.getRoomId() &&
                            Arrays
                                    .stream(gameOverMessage.getPlayers())
                                    .anyMatch(player -> player.getName().equals(gameClient.getUser().getName()))) {
                        gameClient.setRoomId(-1);
                        PlayGamePanel panel = gameClient.getTabbedPane().getPlayGamePanel();
                        panel.createGameOverPanel(gameOverMessage);
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
