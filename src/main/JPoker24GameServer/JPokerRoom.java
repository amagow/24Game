package JPoker24GameServer;

import Common.JPokerUser;
import Common.JPokerUserTransferObject;
import Common.Messages.CardsMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class JPokerRoom {
    private final JPokerServer gameServer;
    private final int roomId;
    private final long readyTime;
    private final int maxPlayers = 4;
    private long startTime;
    private long timeToWin;
    private int timeout = 10000;
    private boolean started = false;
    private ArrayList<JPokerUser> players = new ArrayList<>(4);
    private String[] cards;
    private Integer[] cardNumbers;
    protected JPokerRoom(JPokerServer gameServer, int roomId) {
        this.readyTime = new Date().getTime();
        this.gameServer = gameServer;
        this.roomId = roomId;
    }

    protected JPokerRoom(JPokerServer gameServer, int roomId, int timeout) {
        this.readyTime = new Date().getTime();
        this.roomId = roomId;
        this.gameServer = gameServer;
        this.timeout = timeout;
    }

    public long getTimeToWin() {
        return timeToWin;
    }

    public JPokerUserTransferObject[] getPlayers() {
        return players.stream().map(JPokerUserTransferObject::new).toArray(JPokerUserTransferObject[]::new);
    }

    public Integer[] getCardNumbers() {
        return cardNumbers;
    }

    public String[] getCards() {
        return cards;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean hasPlayer(String username) {
        for (JPokerUser player : players) {
            if (player.getName().equals(username))
                return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return players.size() == 0;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean isStarted() {
        return started;
    }

    public void addPlayer(JPokerUser player) {
        if (players.size() >= 4) {
            System.err.println("The room already has 4 users");
            return;
        }
        players.add(player);
    }

    public void removePlayer(String username) {
        players.removeIf(user -> user.getName().equals(username));

        CardsMessage cardsMessage = new CardsMessage(roomId, cards, cardNumbers,
                players.stream().map(JPokerUserTransferObject::new).toArray(JPokerUserTransferObject[]::new));
        try {
            Message message = gameServer.getJmsHelper().createMessage(cardsMessage);
            gameServer.getJmsHelper().broadcastMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void ready() {
        while (true) {
            long currTime = new Date().getTime();
            if (isFull() || (players.size() >= 2 && currTime - readyTime > timeout)) {
                break;
            }
        }
        start();
    }

    private void start() {
        startTime = new Date().getTime();

        started = true;
        int numCards = 4;
        int i = 0;
        char[] prefix = {'a', 'b', 'c', 'd'};
        Integer[] cardNumbers = new Integer[numCards];
        String[] cards = new String[numCards];

        do {
            Random rand = new Random();
            int cardSuit = rand.nextInt(4);
            Integer cardNumber = rand.nextInt(13) + 1;

            if (Arrays.stream(cardNumbers).noneMatch(cardNumber::equals)) {
                cardNumbers[i] = cardNumber;
                cards[i] = prefix[cardSuit] + Integer.toString(cardNumber) + ".png";
                i++;
            }

        } while (i != numCards);

        this.cards = cards;
        this.cardNumbers = cardNumbers;

        CardsMessage cardsMessage = new CardsMessage(roomId, cards, cardNumbers,
                players.stream().map(JPokerUserTransferObject::new).toArray(JPokerUserTransferObject[]::new));
        try {
            Message message = gameServer.getJmsHelper().createMessage(cardsMessage);
            gameServer.getJmsHelper().broadcastMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void endGame() {
        timeToWin = new Date().getTime() - startTime;
    }
}
