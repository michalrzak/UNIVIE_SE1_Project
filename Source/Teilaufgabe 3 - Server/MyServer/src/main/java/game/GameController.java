package game;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import game.helpers.EGameConstants;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.move.helpers.ESMove;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

@Component
public class GameController implements IGameControllerAccesser {

	private final Map<SUniqueGameIdentifier, Game> games = new HashMap<>();
	private final Queue<SUniqueGameIdentifier> gameIDCreation = new LinkedList<>();

	private final ThreadPoolTaskScheduler taskScheduler;

	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	@Autowired
	public GameController(ThreadPoolTaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public SUniqueGameIdentifier createNewGame() {
		SUniqueGameIdentifier newID = SUniqueGameIdentifier.getRandomID();
		while (checkGameIDUsed(newID)) {
			newID = SUniqueGameIdentifier.getRandomID();
		}

		createNewGameWithGameID(newID);

		return newID;
	}

	public SUniquePlayerIdentifier registerPlayer(SUniqueGameIdentifier gameID, PlayerInformation playerInf) {
		// gameIDUsedOrThrow(gameID);
		assert (checkGameIDUsed(gameID));

		return games.get(gameID).registerPlayer(playerInf);
	}

	public void addHalfMap(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID, SHalfMap hmdata) {
		// gameIDUsedOrThrow(gameID);
		assert (checkGameIDUsed(gameID));
		games.get(gameID).receiveHalfMap(playerID, hmdata);
	}

	public SGameState getGameState(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID) {
		// gameIDUsedOrThrow(gameID);
		assert (checkGameIDUsed(gameID));

		return games.get(gameID).getGameState(playerID);
	}

	public void setLooser(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID) {
		// gameIDUsedOrThrow(gameID);
		assert (checkGameIDUsed(gameID));

		games.get(gameID).setLooser(playerID);
	}

	public void receiveMove(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID, ESMove move) {
		// gameIDUsedOrThrow(gameID);
		assert (checkGameIDUsed(gameID));

		games.get(gameID).receiveMove(playerID, move);
	}

	private boolean checkGameIDUsed(SUniqueGameIdentifier gameID) {
		return games.containsKey(gameID);
	}

	/*
	 * private void gameIDUsedOrThrow(SUniqueGameIdentifier gameID) { if
	 * (!checkGameIDUsed(gameID)) { logger.warn(String.
	 * format("Tried accessing a game (id: %s) which does not exist",
	 * gameID.toString())); throw new GameNotFoundException(
	 * String.format("Tried accessing a game (id: %s) which does not exist",
	 * gameID.toString())); } }
	 */
	private void deleteGame(SUniqueGameIdentifier gameID) {
		games.remove(gameID);
		gameIDCreation.remove(gameID);
		logger.debug(String.format("Removed game with id: %s", gameID.toString()));
	}

	private void createNewGameWithGameID(SUniqueGameIdentifier gameID) {
		if (gameIDCreation.size() >= EGameConstants.MAX_NUM_OF_GAMES.get()) {
			deleteGame(gameIDCreation.element());
		}

		games.put(gameID, new Game());
		gameIDCreation.add(gameID);

		// schedule the death of the game
		taskScheduler.schedule(() -> deleteGame(gameID),
				Instant.now().plusMillis(EGameConstants.GAME_ALIVE_MILLISECONDS.get()));
	}

	// Mainly used for rules
	@Override
	public Collection<SUniqueGameIdentifier> getUsedGameID() {
		// create copies
		return new HashSet<>(games.keySet());
	}

	@Override
	public SUniquePlayerIdentifier playersTurnInGame(SUniqueGameIdentifier gameID) {
		assert (games.containsKey(gameID));
		return games.get(gameID).getPlayerTurn();
	}

	@Override
	public Collection<SUniquePlayerIdentifier> playersRegisteredInGame(SUniqueGameIdentifier gameID) {
		assert (games.containsKey(gameID));
		var registeredPlayersCollection = games.get(gameID).getRegisteredPlayers();
		return registeredPlayersCollection.stream().map(player -> player.getPlayerID()).collect(Collectors.toSet());
	}

	@Override
	public boolean isMapReady(SUniqueGameIdentifier gameID) {
		return games.get(gameID).isMapReady();
	}
}
