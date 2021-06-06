package gamedata.game;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.PlayerInvalidTurn;
import gamedata.map.FullMapData;
import gamedata.map.HalfMapData;
import gamedata.map.MapController;
import gamedata.player.Player;
import gamedata.player.PlayerController;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class Game implements GameAccesser {

	private final long created = System.currentTimeMillis();
	private final PlayerController players = new PlayerController();
	private final MapController map = new MapController();

	private static Logger logger = LoggerFactory.getLogger(Game.class);

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		return players.registerPlayer(playerInf);
	}

	public void receiveHalfMap(ServerUniquePlayerIdentifier playerID, HalfMapData hmData) {
		if (!players.checkPlayerTurn(playerID)) {
			logger.warn("A player with playerID: " + playerID.getPlayerIDAsString()
					+ "; tried sending a HalfMap, but it was not his turn! It was ");
			throw new PlayerInvalidTurn(
					"It is not the players with playerID: " + playerID.getPlayerIDAsString() + "; turn!");
		}
		map.receiveHalfMap(hmData);
		players.nextTurn();
	}

	public boolean checkPlayer(ServerUniquePlayerIdentifier playerID) {
		return players.checkPlayer(playerID);
	}

	public boolean hasStarted() {
		// TODO: make this meaningfull
		return false;
	}

	public long getTimeAlive() {
		return System.currentTimeMillis() - created;
	}

	@Override
	public Collection<Player> getPlayers() {
		return players.getPlayers();
	}

	@Override
	public Optional<FullMapData> getFullMap() {
		return map.getFullMap();
	}

}
