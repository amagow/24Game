package JPoker24GameServer;

import Common.JPokerUser;
import Common.Messages.RoomIdMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

import java.util.ArrayList;

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

    boolean isUserPlaying(String username) {
        return findRoomByUsername(username) != null;
    }

    void allocateRoom(JPokerUser user) throws JMSException {
        synchronized (rooms) {
            for (JPokerRoom room : rooms) {
                if (!room.isFull())
                    room.addPlayer(user);
            }
        }

        int id = rooms.size();
        JPokerRoom room = new JPokerRoom(id);
        room.addPlayer(user);
        synchronized (rooms) {
            rooms.add(room);
        }
        new Thread(room::ready).start();
        Message message = gameServer.getJmsHelper().createMessage(new RoomIdMessage(id));
        message.setStringProperty("messageTo", user.getName());
        gameServer.getJmsHelper().broadcastMessage(message);
    }

    void removeFromRoom(String username) {
        synchronized (rooms) {
            for (JPokerRoom room : rooms) {
                if (room.hasPlayer(username))
                    room.removePlayer(username);
            }
        }
    }
}
