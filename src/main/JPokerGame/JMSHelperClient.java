package JPokerGame;

import Common.JMSHelper;
import Common.JPokerUserTransferObject;
import Common.Messages.RoomIdMessage;
import Common.Messages.UserMessage;
import jakarta.jms.*;

import javax.naming.NamingException;

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
                    gameClient.setRoomId(roomIdMessage.getRoomId());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
