package game;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GameNotReadyException;
import exceptions.PlayerInvalidTurn;
import game.map.ISFullMapAccesser;
import game.map.MapController;
import game.map.SHalfMap;
import game.player.IPlayerAccesser;
import game.player.PlayersController;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class Game implements IGameAccesser {

	private final PlayersController players = new PlayersController();
	private final MapController map = new MapController();

	private boolean playersReady = false;
	private boolean mapReady = false;

	private static Logger logger = LoggerFactory.getLogger(Game.class);

	public Game() {
		// add a listener for if the map is ready. If the map is ready then the game is
		// ready
		map.registerListenForMapReady(eleIsNull -> {
			mapReady = true;
		});

		players.registerListenForPlayersReady(eleIsNull -> {
			playersReady = true;
		});
	}

	public SUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		return players.registerPlayer(playerInf);
	}

	public void receiveHalfMap(SUniquePlayerIdentifier playerID, SHalfMap hmData) {
		checkPlayersReady();

		if (!players.checkPlayerTurn(playerID)) {
			setLooser(playerID);
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
		checkPlayersReady();
		players.setAsLooser(playerID);
	}

	private void checkPlayersReady() {
		if (!getPlayersReady()) {
			logger.warn("Tried to access a game that is not ready. Not all players registerd!");
			throw new GameNotReadyException(
					"Tried to access a game that is not ready. (Both players aren't registered!)");
		}
	}

	private void checkMapReady() {
		if (!getMapReady()) {
			logger.warn("Tried to access a game that is not ready. The map is not combined!");
			throw new GameNotReadyException(
					"Tried to access a game that is not ready. (Both players haven't sent a halfmap!)");
		}
	}

	@Override
	public boolean getPlayersReady() {
		return playersReady;
	}

	@Override
	public boolean getMapReady() {
		return mapReady;
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
