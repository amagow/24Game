package JPoker24GameServer;

import Common.JMSHelper;
import Common.Messages.RoomIdMessage;
import Common.Messages.UserMessage;
import jakarta.jms.*;

import javax.naming.NamingException;

public class JMSHelperServer extends JMSHelper {
    private final MessageConsumer queueReader;
    private final MessageProducer topicSender;

    public JMSHelperServer() throws NamingException, JMSException {
        queueReader = this.createQueueReader();
        topicSender = this.createTopicSender();
    }

    public MessageConsumer getQueueReader() {
        return queueReader;
    }

    public MessageProducer getTopicSender() {
        return topicSender;
    }

    public UserMessage receiveMessage(MessageConsumer queueReader) throws JMSException {
        try {
            System.out.println("JMSServer: Start receiving message");
            Message message = queueReader.receive();
            Object objectMessage = ((ObjectMessage) message).getObject();
            if (objectMessage instanceof UserMessage){
                return (UserMessage) objectMessage;
            }
            else{
                System.err.println(objectMessage);
            }
        } catch (JMSException e) {
            System.err.println("JMSServer: Failed to receive message " + e);
            throw e;
        }
        return null;
    }

    public void broadcastMessage(Message jmsMessage) throws JMSException {
        try {
            topicSender.send(jmsMessage);
            System.out.println("JMSServer: Finish broadcast message \n" + jmsMessage);
        } catch (JMSException e) {
            System.err.println("JMSServer: Failed to broadcast message " + e);
            throw e;
        }
    }
}
