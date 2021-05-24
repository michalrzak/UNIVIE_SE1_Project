package server.games.helpers;

public class ServerUniqueGameIdentifier {

	static final private int GAMEID_LENGTH = 5;

	final private String gameID;

	public ServerUniqueGameIdentifier() {
		RandomString rand = new RandomString();
		gameID = rand.nextString(GAMEID_LENGTH);
	}

	public String getIDAsString() {
		return gameID;
	}

	@Override
	public int hashCode() {
		return gameID.hashCode();
	}

}
