package JPoker24GameServer;

import Common.JMSHelper;
import Common.Messages.JMSMessage;
import jakarta.jms.*;

import javax.naming.NamingException;

public class JMSHelperServer extends JMSHelper {
    MessageConsumer queueReader;
    MessageProducer topicSender;

    public JMSHelperServer() throws NamingException, JMSException {
        queueReader = this.createQueueReader();
        topicSender = this.createTopicSender();
    }

    public JMSMessage receiveMessage(MessageConsumer queueReader) throws JMSException {
        try {
            System.out.println("JMSServer: Start receiving message");
            Message jmsMessage = queueReader.receive();
            return (JMSMessage) ((ObjectMessage) jmsMessage).getObject();
        } catch (JMSException e) {
            System.err.println("JMSServer: Failed to receive message " + e);
            throw e;
        }
    }

    public void broadcastMessage(MessageProducer topicSender, Message jmsMessage) throws JMSException {
        try {
            System.out.println("JMSServer: Start broadcast message");
            topicSender.send(jmsMessage);
            System.out.println("JMSServer: Finish broadcast message");
        } catch (JMSException e) {
            System.err.println("JMSServer: Failed to broadcast message " + e);
            throw e;
        }
    }
}
