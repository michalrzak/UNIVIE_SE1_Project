package gamedata.game;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GameNotReadyException;
import exceptions.PlayerInvalidTurn;
import gamedata.map.ISFullMapAccesser;
import gamedata.map.MapController;
import gamedata.map.SHalfMap;
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

	public void receiveHalfMap(SUniquePlayerIdentifier playerID, SHalfMap hmData) {
		if (!getReady()) {
			System.err.println("TEST!@$");
			logger.warn("Tried tp send a HalfMap to a game that is not ready");
			throw new GameNotReadyException("Tried to send a HalfMap to a game that is not ready");
		}

		if (!players.checkPlayerTurn(playerID)) {
			players.setAsLooser(playerID);
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

	public void setLooser(SUniquePlayerIdentifier playerID) {
		players.setAsLooser(playerID);
	}

	public long getTimeAlive() {
		return System.currentTimeMillis() - created;
	}

	@Override
	public boolean getReady() {
		return players.getReady();
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

	@Override
	public int getTurn() {
		return players.getTurn();
	}

	@Override
	public IPlayerAccesser getPlayer(SUniquePlayerIdentifier playerID) {
		return players.getPlayer(playerID);
	}

}
