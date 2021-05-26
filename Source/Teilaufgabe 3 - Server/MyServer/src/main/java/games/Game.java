package games;

import games.helpers.ServerUniquePlayerIdentifier;

public class Game {

	final private long created = System.currentTimeMillis();

	final private PlayerController players = new PlayerController();

	public void registerPlayer(ServerUniquePlayerIdentifier playerID) {
		players.registerPlayer(playerID);
	}

	public void receiveHalfMap() {
		// TODO: this
		return;
	}

	public boolean hasStarted() {
		// TODO: make this meaningfull
		return false;
	}

	public long getTimeAlive() {
		return System.currentTimeMillis() - created;
	}

}
