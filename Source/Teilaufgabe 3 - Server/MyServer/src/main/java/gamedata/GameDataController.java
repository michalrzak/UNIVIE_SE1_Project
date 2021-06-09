package gamedata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GameNotFoundException;
import exceptions.PlayerInvalidTurn;
import gamedata.game.Game;
import gamedata.game.helpers.SUniqueGameIdentifier;
import gamedata.map.SHalfMap;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class GameDataController {

	private final Map<SUniqueGameIdentifier, Game> games = new HashMap<>();
	private final Queue<SUniqueGameIdentifier> gameIDCreation = new LinkedList<>();

	private static Logger logger = LoggerFactory.getLogger(GameDataController.class);

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

		return new SGameState(playerID, games.get(gameID));
	}

	private boolean checkGameIDUsed(SUniqueGameIdentifier gameID) {
		return games.containsKey(gameID);
	}

	private void createNewGameWithGameID(SUniqueGameIdentifier gameID) {
		if (gameIDCreation.size() >= EGameConstants.MAX_NUM_OF_GAMES.getValue()) {
			games.remove(gameIDCreation.remove());
		}

		games.put(gameID, new Game());
		gameIDCreation.add(gameID);
	}
}
