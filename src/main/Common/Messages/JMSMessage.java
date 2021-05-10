package Common.Messages;

import Common.JPokerUserTransferObject;

import java.io.Serializable;

public class JMSMessage implements Serializable {
    private JPokerUserTransferObject user;

    public JMSMessage(JPokerUserTransferObject user) {
        this.user = user;
    }

    public JPokerUserTransferObject getUser() {
        return user;
    }
}
