package JPoker24GameServer;

import Common.JMSHelper;
import Common.JPokerUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class JPokerRoom {
    private final long startTime;
    private int timeout = 10;
    private ArrayList<JPokerUser> players = new ArrayList<>(4);

    protected JPokerRoom() {
        this.startTime = new Date().getTime();
    }

    protected JPokerRoom(int timeout) {
        this.startTime = new Date().getTime();
        this.timeout = timeout;
    }

    boolean hasPlayer(String username) {
        for (JPokerUser player : players) {
            if (player.getName().equals(username))
                return true;
        }
        return false;
    }

    boolean isFull() {
        return players.size() >= 4;
    }

    void addPlayer(JPokerUser player) {
        if (players.size() >= 4) {
            System.err.println("The room already has 4 users");
            return;
        }
        players.add(player);
    }

    void removePlayer(String username) {
        players.removeIf(user -> user.getName().equals(username));
    }

    void ready() {
        System.out.println("Ready " + "" + this);
        while (true) {
            long currTime = new Date().getTime();
            if (players.size() == 4 || players.size() >= 2 && currTime - startTime > 10) {
                break;
            }
        }
        start();
    }

    void start() {
        System.out.println("Game started");
    }
}
