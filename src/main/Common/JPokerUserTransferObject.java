package Common;

public class JPokerUserTransferObject {
	String name;
	String password;
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
}
