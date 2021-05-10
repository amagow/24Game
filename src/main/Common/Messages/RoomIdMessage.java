package Common.Messages;

public class RoomIdMessage extends BaseMessage {
    private final int roomId;

    public RoomIdMessage(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "" + roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}
