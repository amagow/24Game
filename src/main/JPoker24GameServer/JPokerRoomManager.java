package JPoker24GameServer;

import Common.JPokerUser;
import Common.Messages.RoomIdMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class JPokerRoomManager {
    private final JPokerServer gameServer;
    private final ArrayList<JPokerRoom> rooms = new ArrayList<>();

    public JPokerRoomManager(JPokerServer gameServer) {
        this.gameServer = gameServer;
    }

    JPokerRoom findRoomByUsername(String username) {
        for (JPokerRoom room : rooms) {
            if (room.hasPlayer(username))
                return room;
        }
        return null;
    }

    public boolean isUserPlaying(String username) {
        return findRoomByUsername(username) != null;
    }

    private boolean containsRoomId(ArrayList<JPokerRoom> rooms, int id) {
        for (JPokerRoom room : rooms) {
            if (room.getRoomId() == id) {
                return true;
            }
        }
        return false;
    }

    private int generateId() {
        Random r = new Random();
        Integer id;
        do {
            id = r.nextInt(Integer.MAX_VALUE);
        } while (containsRoomId(rooms, id));
        return id;
    }

    void allocateRoom(JPokerUser user) throws JMSException {
        boolean isAdded = false;
        JPokerRoom chosenRoom = null;

        synchronized (rooms) {
            for (JPokerRoom room : rooms) {
                if (room.isFull() || room.isStarted())
                    continue;
                room.addPlayer(user);
                chosenRoom = room;
                isAdded = true;
            }
        }

        if (!isAdded) {
            Integer id = generateId();
            assert id >= 0;

            JPokerRoom room = new JPokerRoom(gameServer, id);
            chosenRoom = room;
            room.addPlayer(user);
            synchronized (rooms) {
                rooms.add(room);
            }
            new Thread(room::ready).start();
        }

        Message message = gameServer.getJmsHelper().createMessage(new RoomIdMessage(chosenRoom.getRoomId(), chosenRoom.getPlayers()));
        message.setStringProperty("messageTo", user.getName());
        gameServer.getJmsHelper().broadcastMessage(message);
    }

    void removeFromRoom(String username) {
        synchronized (rooms) {
            Iterator<JPokerRoom> iter = rooms.iterator();

            while (iter.hasNext()) {
                JPokerRoom room = iter.next();
                if (room.hasPlayer(username))
                    room.removePlayer(username);
                if (room.isEmpty()) {
                    iter.remove();
                }
            }
        }
    }
}
