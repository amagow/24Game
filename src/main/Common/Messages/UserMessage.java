package Common.Messages;

import Common.JPokerUserTransferObject;

public class UserMessage extends BaseMessage {
    private JPokerUserTransferObject user;

    public UserMessage(JPokerUserTransferObject user) {
        this.user = user;
    }

    public JPokerUserTransferObject getUser() {
        return user;
    }
}
