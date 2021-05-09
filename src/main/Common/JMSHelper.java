package Common;

import java.io.Serializable;
import java.util.Hashtable;

import jakarta.jms.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSHelper {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 3700;

    private static final String JMS_CONNECTION_FACTORY = "jms/JPokerConnectionFactory";
    private static final String JMS_QUEUE = "jms/JPokerQueue";
    private static final String JMS_TOPIC = "jms/JPokerTopic";

    private Context jndiContext;
    private ConnectionFactory connectionFactory;
    private Connection connection;

    private Session session;
    private Queue queue;
    private Topic topic;

    public JMSHelper() throws NamingException, JMSException {
        this(DEFAULT_HOST);
    }

    public JMSHelper(String host) throws NamingException, JMSException {
        int port = DEFAULT_PORT;

        System.setProperty("org.omg.CORBA.ORBInitialHost", host);
        System.setProperty("org.omg.CORBA.ORBInitialPort", "" + port);

        try {
            jndiContext = new InitialContext();
            connectionFactory = (ConnectionFactory) jndiContext.lookup(JMS_CONNECTION_FACTORY);
            queue = (Queue) jndiContext.lookup(JMS_QUEUE);
            topic = (Topic) jndiContext.lookup(JMS_TOPIC);

            connection = connectionFactory.createConnection();
            connection.start();
        } catch (NamingException e) {
            System.out.println("JNDI error:" + e);
            throw e;
        }


    }

    public Session createSession() throws JMSException {
        if (session != null) {
            return session;
        } else {
            try {
                return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                System.err.println("Failed creating session: " + e);
                throw e;
            }
        }
    }

    public ObjectMessage createMessage(Serializable obj) throws JMSException {
        try {
            return createSession().createObjectMessage(obj);
        } catch (JMSException e) {
            System.err.println("Error preparing message: " + e);
            throw e;
        }
    }

    public MessageProducer createQueueSender() throws JMSException {
        try {
            return createSession().createProducer(queue);
        } catch (JMSException e) {
            System.err.println("Failed sending to queue: " + e);
            throw e;
        }
    }

    public MessageConsumer createQueueReader() throws JMSException {
        try {
            return createSession().createConsumer(queue);
        } catch (JMSException e) {
            System.err.println("Failed reading from queue: " + e);
            throw e;
        }
    }

    public MessageProducer createTopicSender() throws JMSException {
        try {
            return createSession().createProducer(topic);
        } catch (JMSException e) {
            System.err.println("Failed sending to queue: " + e);
            throw e;
        }
    }

    public MessageConsumer createTopicReader() throws JMSException {
        try {
            return createSession().createConsumer(topic);
        } catch (JMSException e) {
            System.err.println("Failed reading from queue: " + e);
            throw e;
        }
    }

    @Override
    public void finalize() {

        try {
            session.close();
            connection.close();
            jndiContext.close();
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }

    }
}