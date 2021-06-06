package gamedata.game.helpers;

public class SUniqueGameIdentifier {

	static final private int GAMEID_LENGTH = 5;

	final private String gameID;

	public SUniqueGameIdentifier() {
		RandomString rand = new RandomString();
		gameID = rand.nextString(GAMEID_LENGTH);
	}

	public SUniqueGameIdentifier(String id) {
		gameID = id;
	}

	public String getIDAsString() {
		return gameID;
	}

	@Override
	public int hashCode() {
		return gameID.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SUniqueGameIdentifier))
			return false;

		SUniqueGameIdentifier arg = (SUniqueGameIdentifier) obj;

		return gameID.equals(arg.gameID);
	}

}
