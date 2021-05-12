package Common.Messages;

import Common.JPokerUserTransferObject;

public class GameOverMessage extends BaseMessage {
    private final int roomId;
    private final JPokerUserTransferObject[] players;
    private final JPokerUserTransferObject winner;
    private final String winningFormula;

    public GameOverMessage(int roomId, JPokerUserTransferObject[] players, JPokerUserTransferObject winner, String winningFormula) {
        this.roomId = roomId;
        this.players = players;
        this.winner = winner;
        this.winningFormula = winningFormula;
    }

    public String getWinningFormula() {
        return winningFormula;
    }

    public int getRoomId() {
        return roomId;
    }

    public JPokerUserTransferObject[] getPlayers() {
        return players;
    }

    public JPokerUserTransferObject getWinner() {
        return winner;
    }
}
