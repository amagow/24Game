package Common;

import java.io.Serializable;

public class JPokerUserTransferObject implements Serializable {
    String name;
    Integer wins;
    Integer games;
    Integer rank;
    Double averageTimeToWin;

    public JPokerUserTransferObject(JPokerUser user) {
        super();
        this.name = user.getName();
        this.rank = user.getRank();
        this.wins = user.getWins();
        this.games = user.getGames();
        this.averageTimeToWin = user.getAverageTimeToWin();
    }

    public String getName() {
        return name;
    }

    public Integer getWins() {
        return wins;
    }

    public Integer getGames() {
        return games;
    }

    public Integer getRank() {
        return rank;
    }

    public Double getAverageTimeToWin() {
        return averageTimeToWin;
    }
}
