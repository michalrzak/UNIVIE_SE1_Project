package games;

import player.PlayerController;
import player.helpers.PlayerInformation;
import player.helpers.ServerUniquePlayerIdentifier;

public class Game {

	final private long created = System.currentTimeMillis();

	final private PlayerController players = new PlayerController();

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		return players.registerPlayer(playerInf);
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
