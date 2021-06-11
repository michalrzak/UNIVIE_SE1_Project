package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GameNotFoundException;
import exceptions.PlayerInvalidTurn;
import game.helpers.EGameConstants;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class GameController {

	private final Map<SUniqueGameIdentifier, Game> games = new HashMap<>();
	private final Queue<SUniqueGameIdentifier> gameIDCreation = new LinkedList<>();

	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	public SUniqueGameIdentifier createNewGame() {
		SUniqueGameIdentifier newID = SUniqueGameIdentifier.getRandomID();
		while (checkGameIDUsed(newID)) {
			newID = SUniqueGameIdentifier.getRandomID();
		}

		createNewGameWithGameID(newID);

		return newID;
	}

	public SUniquePlayerIdentifier registerPlayer(SUniqueGameIdentifier gameID, PlayerInformation playerInf) {
		if (!(games.containsKey(gameID))) {
			logger.warn("Tried registering a player to a gameID that does not exist. GameID was: "
					+ gameID.getIDAsString());
			throw new GameNotFoundException("The passed gameID was not found");
		}

		return games.get(gameID).registerPlayer(playerInf);
	}

	public void addHalfMap(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID, SHalfMap hmdata) {
		if (!games.containsKey(gameID)) {
			logger.warn("Player with ID: " + playerID.getPlayerIDAsString()
					+ " tried adding a halfmap to a gameID which does not exist (was: " + gameID.getIDAsString() + ")");
			throw new GameNotFoundException("The passed gameID was not found");
		}
		try {
			games.get(gameID).receiveHalfMap(playerID, hmdata);
		} catch (PlayerInvalidTurn e) {
			logger.warn(String.format(
					"The player with ID: %s tried to send a HalfMap to the game with ID: %s, but it was not his turn!",
					playerID.getPlayerIDAsString(), gameID.getIDAsString()));
			throw e;
		}
	}

	public SGameState getGameState(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID) {

		if (!games.containsKey(gameID)) {
			logger.warn("Player with ID: " + playerID.getPlayerIDAsString()
					+ " tried requesting the gamestate of a game that does not exist (was: " + gameID.getIDAsString()
					+ ")");
			throw new GameNotFoundException("The passed gameID was not found");
		}

		if (!games.get(gameID).checkPlayer(playerID)) {
			logger.warn("Player with ID: " + playerID.getPlayerIDAsString()
					+ " tried accessing the gamestate of a game where he is not registered (was: "
					+ gameID.getIDAsString() + ")");
			throw new GameNotFoundException("The passed playerID was not found");
		}

		/*
		 * if (!games.get(gameID).getReady()) {
		 * logger.warn("The accessed game was not ready"); throw new
		 * GameNotReadyException("The accessed game is not ready"); }
		 */

		return new SGameState(playerID, games.get(gameID));
	}

	public void setLooser(SUniqueGameIdentifier gameID, SUniquePlayerIdentifier playerID) {
		if (!games.containsKey(gameID)) {
			logger.warn("Player with ID: " + playerID.getPlayerIDAsString()
					+ " tried requesting the gamestate of a game that does not exist (was: " + gameID.getIDAsString()
					+ ")");
			throw new GameNotFoundException("The passed gameID was not found");
		}

		if (!games.get(gameID).getReady()) {
			return;
		}

		games.get(gameID).setLooser(playerID);
	}

	private boolean checkGameIDUsed(SUniqueGameIdentifier gameID) {
		return games.containsKey(gameID);
	}

	private void deleteGame(SUniqueGameIdentifier gameID) {
		games.remove(gameID);
		gameIDCreation.remove(gameID);
		logger.debug(String.format("Removed game with id: %s", gameID.getIDAsString()));
	}

	private void createNewGameWithGameID(SUniqueGameIdentifier gameID) {
		if (gameIDCreation.size() >= EGameConstants.MAX_NUM_OF_GAMES.getValue()) {
			deleteGame(gameIDCreation.element());
		}

		games.put(gameID, new Game());
		gameIDCreation.add(gameID);

		games.get(gameID).registerListenForDeath(eleIsNull -> {
			logger.debug(String.format("Called the PropertyChangeSupport for gameID: %s", gameID.getIDAsString()));
			deleteGame(gameID);
		});
	}
}
