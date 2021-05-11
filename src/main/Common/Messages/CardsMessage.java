package Common.Messages;

import Common.JPokerUserTransferObject;

public class CardsMessage extends BaseMessage {
    private final int roomId;
    private final String[] cards;
    private final Integer[] numbers;
    private final JPokerUserTransferObject[] users;

    public CardsMessage(int roomId, String[] cards, Integer[] numbers, JPokerUserTransferObject[] users) {
        this.roomId = roomId;
        this.cards = cards;
        this.numbers = numbers;
        this.users = users;
    }

    public JPokerUserTransferObject[] getUsers() {
        return users;
    }

    public int getRoomId() {
        return roomId;
    }

    public String[] getCards() {
        return cards;
    }

    public Integer[] getNumbers() {
        return numbers;
    }
}
