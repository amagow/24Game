package Common.Messages;

import Common.JPokerUser;
import Common.JPokerUserTransferObject;

import java.util.ArrayList;

public class RoomIdMessage extends BaseMessage {
    private final int roomId;
    private final JPokerUserTransferObject[] players;

    public RoomIdMessage(int roomId, JPokerUserTransferObject[] players) {
        this.roomId = roomId;
        this.players = players;
    }

    public JPokerUserTransferObject[] getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "" + roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}
