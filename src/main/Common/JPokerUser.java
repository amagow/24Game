package Common;

import java.io.Serializable;

public class JPokerUser implements Serializable {
    private static final long serialVersionUID = -3323598766297035525L;
    String name;
    String password;
    Integer wins;
    Integer games;
    Integer rank;
    Double averageTimeToWin;

    public JPokerUser(String name, String password, Integer wins, Integer games, Integer rank, Double averageTimeToWin) {
        this.name = name;
        this.password = password;
        this.wins = wins;
        this.games = games;
        this.rank = rank;
        this.averageTimeToWin = averageTimeToWin;
    }

    public JPokerUser(String name, String password, Integer rank) {
        super();
        this.name = name;
        this.password = password;
        this.rank = rank;
        this.wins = 0;
        this.games = 0;
        this.averageTimeToWin = .0;
    }

    public String getPassword() {
        return password;
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

    public String getName() {
        return name;
    }

    public boolean verifyPassword(String password) {
        return (this.password.equals(password));
    }

    public String toDescString() {
        return "Name: " + name + "\nRank: " + rank + "\nWins: " + wins + "\nGames: " + games + "\nAvg. Time to Win: "
                + averageTimeToWin + "\nPassword: " + password;
    }
}
