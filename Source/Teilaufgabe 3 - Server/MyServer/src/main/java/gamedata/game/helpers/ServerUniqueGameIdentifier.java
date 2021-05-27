package gamedata.game.helpers;

public class ServerUniqueGameIdentifier {

	static final private int GAMEID_LENGTH = 5;

	final private String gameID;

	public ServerUniqueGameIdentifier() {
		RandomString rand = new RandomString();
		gameID = rand.nextString(GAMEID_LENGTH);
	}

	public ServerUniqueGameIdentifier(String id) {
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
		if (!(obj instanceof ServerUniqueGameIdentifier))
			return false;

		ServerUniqueGameIdentifier arg = (ServerUniqueGameIdentifier) obj;

		return gameID.equals(arg.gameID);
	}

}
