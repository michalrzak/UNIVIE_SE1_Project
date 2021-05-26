package games;

import map.HalfMapData;
import map.MapController;
import player.PlayerController;
import player.helpers.PlayerInformation;
import player.helpers.ServerUniquePlayerIdentifier;

public class Game {

	final private long created = System.currentTimeMillis();

	final private PlayerController players = new PlayerController();
	final private MapController map = new MapController();

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		return players.registerPlayer(playerInf);
	}

	public void receiveHalfMap(ServerUniquePlayerIdentifier playerID, HalfMapData hmData) {
		players.checkPlayerTurn(playerID);
		map.receiveHalfMap(hmData);
	}

	public boolean hasStarted() {
		// TODO: make this meaningfull
		return false;
	}

	public long getTimeAlive() {
		return System.currentTimeMillis() - created;
	}

}
