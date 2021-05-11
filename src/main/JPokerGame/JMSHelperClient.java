package JPokerGame;

import Common.JMSHelper;
import Common.JPokerUserTransferObject;
import Common.Messages.CardsMessage;
import Common.Messages.RoomIdMessage;
import Common.Messages.UserMessage;
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
            System.out.println("JMSClient: Message send" + message);
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

                    for (JPokerUserTransferObject u : roomIdMessage.getPlayers())
                        System.out.println(u.getName());

                    System.out.println(
                            Arrays
                                    .stream(roomIdMessage.getPlayers())
                                    .anyMatch(player -> player.getName().equals(gameClient.getUser().getName()))
                    );

                    if (gameClient.getRoomId() < 0 && gameClient.getUser() != null &&
                            Arrays
                                    .stream(roomIdMessage.getPlayers())
                                    .anyMatch(player -> player.getName().equals(gameClient.getUser().getName()))
                    ) {
                        gameClient.setRoomId(roomIdMessage.getRoomId());
                        System.out.println(gameClient.getRoomId());
                    }
                }
                if (objectMessage instanceof CardsMessage) {
                    CardsMessage cardsMessage = (CardsMessage) objectMessage;
                    System.out.println(cardsMessage.getRoomId());
                    if (gameClient.getRoomId() == cardsMessage.getRoomId()) {
                        PlayGamePanel panel = gameClient.getTabbedPane().getPlayGamePanel();
                        panel.createPlayingGamePanel(cardsMessage);
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
