package game;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import exceptions.GameNotFoundException;
import exceptions.PlayerInvalidTurn;
import game.helpers.EGameConstants;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.move.helpers.ESMove;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

@Component
public class GameController {

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
		gameIDUsedOrThrow(gameID);

		return games.get(gameID).registerPlayer(playerInf);
	}

	public void addHalfMap(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID, SHalfMap hmdata) {
		gameIDUsedOrThrow(gameID);

		// TODO: change this to propertylistner maybe?
		try {
			games.get(gameID).receiveHalfMap(playerID, hmdata);
		} catch (PlayerInvalidTurn e) {
			logger.warn(String.format(
					"The player with ID: %s tried to send a HalfMap to the game with ID: %s, but it was not his turn!",
					playerID.asString(), gameID.toString()));
			throw e;
		}
	}

	public SGameState getGameState(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID) {
		gameIDUsedOrThrow(gameID);

		return games.get(gameID).getGameState(playerID);
	}

	public void setLooser(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID) {
		gameIDUsedOrThrow(gameID);

		games.get(gameID).setLooser(playerID);
	}

	public void receiveMove(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID, ESMove move) {
		gameIDUsedOrThrow(gameID);

		games.get(gameID).receiveMove(playerID, move);
	}

	private boolean checkGameIDUsed(SUniqueGameIdentifier gameID) {
		return games.containsKey(gameID);
	}

	private void gameIDUsedOrThrow(SUniqueGameIdentifier gameID) {
		if (!checkGameIDUsed(gameID)) {
			logger.warn(String.format("Tried accessing a game (id: %s) which does not exist", gameID.toString()));
			throw new GameNotFoundException(
					String.format("Tried accessing a game (id: %s) which does not exist", gameID.toString()));
		}
	}

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
}
