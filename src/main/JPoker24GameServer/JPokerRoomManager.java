package JPoker24GameServer;

import Common.JPokerUser;

import java.util.ArrayList;

public class JPokerRoomManager {

    private final ArrayList<JPokerRoom> rooms = new ArrayList<>();

    JPokerRoom findRoomByUsername(String username) {
        for (JPokerRoom room : rooms) {
            if (room.hasPlayer(username))
                return room;
        }
        return null;
    }

    boolean isUserPlaying(String username) {
        return findRoomByUsername(username) != null;
    }

    void allocateRoom(JPokerUser user) {
        synchronized (rooms) {
            for (JPokerRoom room : rooms) {
                if (!room.isFull())
                    room.addPlayer(user);
            }
        }

        JPokerRoom room = new JPokerRoom();
        room.addPlayer(user);
        synchronized (rooms) {
            rooms.add(room);
        }
        new Thread(room::ready).start();
    }

    void removeFromRoom(String username){
        synchronized (rooms) {
            for (JPokerRoom room : rooms) {
                if (room.hasPlayer(username))
                    room.removePlayer(username);
            }
        }
    }
}
