package game;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GameNotFoundException;
import exceptions.GameNotReadyException;
import exceptions.PlayerInvalidTurn;
import game.map.IMapAccesser;
import game.map.MapController;
import game.map.SHalfMap;
import game.move.MoveController;
import game.move.helpers.ESMove;
import game.player.IPlayerAccesser;
import game.player.PlayersController;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class Game implements IGameAccesser {

	private final PlayersController players = new PlayersController();
	private final MapController map = new MapController();
	private final MoveController moves = new MoveController();

	private boolean playersReady = false;
	private boolean mapReady = false;

	private static Logger logger = LoggerFactory.getLogger(Game.class);

	public Game() {
		// register the controllers to each other
		moves.listenToAllRequiredProperties(map.rergisterForFullMap());
		map.listenToAllRequiredProperties(moves.registerPlayerMove());
		players.listenToAllRequiredProperties(map.rergisterForTreassureCollected(), map.rergisterForSteppedOnCastle());

		// register this class to some required controllers
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
		playersReadyOrThrow();
		playersTurnOrThrow(playerID);

		map.receiveHalfMap(hmData);
		players.nextTurn();
	}

	public void setLooser(SUniquePlayerIdentifier playerID) {
		// TODO: think about this
		if (playersReady) {
			players.setAsLooser(playerID);
		}
	}

	public SGameState getGameState(SUniquePlayerIdentifier playerID) {
		playerRegisteredOrThrow(playerID);
		return new SGameState(playerID, this);
	}

	public void receiveMove(SUniquePlayerIdentifier playerID, ESMove move) {
		playerRegisteredOrThrow(playerID);
		mapReadyOrThrow();
		playersTurnOrThrow(playerID);

		moves.move(playerID, move);

		players.nextTurn();
	}

	private void playersReadyOrThrow() {
		if (!arePlayersReady()) {
			logger.warn("Tried to access a game that is not ready. Not all players registerd!");
			throw new GameNotReadyException(
					"Tried to access a game that is not ready. (Both players aren't registered!)");
		}
	}

	private void mapReadyOrThrow() {
		if (!isMapReady()) {
			logger.warn("Tried to access a game that is not ready. The map is not combined!");
			throw new GameNotReadyException(
					"Tried to access a game that is not ready. (Both players haven't sent a halfmap!)");
		}
	}

	private boolean isPlayerRegistered(SUniquePlayerIdentifier playerID) {
		return players.isPlayerRegistered(playerID);
	}

	private void playerRegisteredOrThrow(SUniquePlayerIdentifier playerID) {
		if (!isPlayerRegistered(playerID)) {
			logger.warn(String.format("Player with ID: %s tried accessing a game where he is not registered",
					playerID.asString()));
			throw new GameNotFoundException(
					String.format("Player with ID: %s tried accessing a game where he is not registered",
							playerID.asString()));
		}
	}

	private void playersTurnOrThrow(SUniquePlayerIdentifier playerID) {
		if (!players.checkPlayerTurn(playerID)) {
			setLooser(playerID);
			logger.warn("A player with playerID: " + playerID.asString()
					+ "; tried performing an action, but it was not his turn!");
			throw new PlayerInvalidTurn(
					"It is not the players with playerID: " + playerID.asString() + "; turn!");
		}
	}

	@Override
	public boolean arePlayersReady() {
		return playersReady;
	}

	@Override
	public boolean isMapReady() {
		return mapReady;
	}

	@Override
	public Optional<IMapAccesser> getFullMap() {
		return map.getFullMap();
	}

	@Override
	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID) {
		return players.getPlayerState(playerID);
	}

	@Override
	public int getTurn() {
		return players.getTurn();
	}

	@Override
	public Collection<IPlayerAccesser> getRegisteredPlayers() {
		return players.getRegisteredPlayers();
	}

}
