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
    private final long startTime;
    private final int maxPlayers = 4;
    private int timeout = 10;
    private boolean started = false;
    private ArrayList<JPokerUser> players = new ArrayList<>(4);

    protected JPokerRoom(JPokerServer gameServer, int roomId) {
        this.startTime = new Date().getTime();
        this.gameServer = gameServer;
        this.roomId = roomId;
    }

    protected JPokerRoom(JPokerServer gameServer, int roomId, int timeout) {
        this.startTime = new Date().getTime();
        this.roomId = roomId;
        this.gameServer = gameServer;
        this.timeout = timeout;
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
    }

    public void ready() {
        while (true) {
            long currTime = new Date().getTime();
            if (isFull() || players.size() >= 2 && currTime - startTime > timeout) {
                break;
            }
        }
        start();
    }

    private void start() {
        System.out.println("Game started");

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

        for (String card : cards)
            System.out.println(card);

        CardsMessage cardsMessage = new CardsMessage(roomId, cards, cardNumbers,
                players.stream().map(JPokerUserTransferObject::new).toArray(JPokerUserTransferObject[]::new));
        try {
            Message message = gameServer.getJmsHelper().createMessage(cardsMessage);
            gameServer.getJmsHelper().broadcastMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
