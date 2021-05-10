package JPokerGame;

import Common.JMSHelper;
import Common.JPokerUserTransferObject;
import Common.Messages.JMSMessage;
import jakarta.jms.*;

import javax.naming.NamingException;
import java.io.Serializable;

class JMSHelperClient extends JMSHelper implements MessageListener {
    MessageProducer queueSender;
    MessageConsumer topicReader;

    public JMSHelperClient() throws JMSException, NamingException {
        queueSender = this.createQueueSender();
        topicReader = this.createTopicReader();
        topicReader.setMessageListener(this);
    }

    public void sendMessage(JPokerUserTransferObject user) {
        JMSMessage userMessage = new JMSMessage(user);
        try {
            Message message = this.createMessage(userMessage);
            queueSender.send(message);
            System.out.println("JMSClient: Message send" + message);
        } catch (JMSException e) {
            System.err.println("JMSClient: Failed to send message");
        }
    }

    @Override
    public void onMessage(Message message) {

    }
}
