package gamedata.game;

import gamedata.map.HalfMapData;
import gamedata.map.MapController;
import gamedata.player.PlayerController;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

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
		players.nextTurn();
	}

	public boolean hasStarted() {
		// TODO: make this meaningfull
		return false;
	}

	public long getTimeAlive() {
		return System.currentTimeMillis() - created;
	}

	public GameAccesser getGameInformation(ServerUniquePlayerIdentifier playerID) {
		return null;
	}

}
