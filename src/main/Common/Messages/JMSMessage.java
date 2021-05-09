package Common.Messages;

import Common.JPokerUserTransferObject;

public class JMSMessage {
    private JPokerUserTransferObject user;

    public JMSMessage(JPokerUserTransferObject user) {
        this.user = user;
    }

    public JPokerUserTransferObject getUser() {
        return user;
    }
}
