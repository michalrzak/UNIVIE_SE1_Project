package gamedata.game;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.PlayerInvalidTurn;
import gamedata.map.HalfMapData;
import gamedata.map.ISFullMapAccesser;
import gamedata.map.MapController;
import gamedata.player.IPlayerAccesser;
import gamedata.player.PlayersController;
import gamedata.player.helpers.ESPlayerGameState;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class Game implements IGameAccesser {

	private final long created = System.currentTimeMillis();
	private final PlayersController players = new PlayersController();
	private final MapController map = new MapController();

	private static Logger logger = LoggerFactory.getLogger(Game.class);

	public SUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		return players.registerPlayer(playerInf);
	}

	public void receiveHalfMap(SUniquePlayerIdentifier playerID, HalfMapData hmData) {
		if (!players.checkPlayerTurn(playerID)) {
			logger.warn("A player with playerID: " + playerID.getPlayerIDAsString()
					+ "; tried sending a HalfMap, but it was not his turn! It was ");
			throw new PlayerInvalidTurn(
					"It is not the players with playerID: " + playerID.getPlayerIDAsString() + "; turn!");
		}
		map.receiveHalfMap(hmData);
		players.nextTurn();
	}

	public boolean checkPlayer(SUniquePlayerIdentifier playerID) {
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
	public Collection<IPlayerAccesser> getPlayers() {
		return players.getPlayers();
	}

	@Override
	public Optional<ISFullMapAccesser> getFullMap() {
		return map.getFullMap();
	}

	@Override
	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID) {
		return players.getPlayerState(playerID);
	}

	@Override
	public SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer) {
		return players.getOtherPlayer(myPlayer);
	}

}
