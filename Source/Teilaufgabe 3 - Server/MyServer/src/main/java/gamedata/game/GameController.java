package gamedata.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GameNotFoundException;
import exceptions.PlayerInvalidTurn;
import gamedata.EGameConstants;
import gamedata.game.helpers.ServerUniqueGameIdentifier;
import gamedata.map.HalfMapData;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class GameController {

	private final Map<ServerUniqueGameIdentifier, Game> games = new HashMap<>();
	private final Queue<ServerUniqueGameIdentifier> gameIDCreation = new LinkedList<>();

	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	public ServerUniqueGameIdentifier createNewGame() {
		ServerUniqueGameIdentifier newID = new ServerUniqueGameIdentifier();
		while (checkGameIDUsed(newID)) {
			newID = new ServerUniqueGameIdentifier();
		}

		createNewGameWithGameID(newID);

		return newID;
	}

	public ServerUniquePlayerIdentifier registerPlayer(ServerUniqueGameIdentifier gameID, PlayerInformation playerInf) {
		if (!(games.containsKey(gameID))) {
			logger.warn("Tried registering a player to a gameID that does not exist. GameID was: "
					+ gameID.getIDAsString());
			throw new GameNotFoundException("The passed gameID was not found");
		}

		return games.get(gameID).registerPlayer(playerInf);
	}

	public void addHalfMap(ServerUniqueGameIdentifier gameID, ServerUniquePlayerIdentifier playerID,
			HalfMapData hmdata) {
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

	public GameAccesser getGameInformation(ServerUniqueGameIdentifier gameID, ServerUniquePlayerIdentifier playerID) {
		return null;
	}

	private boolean checkGameIDUsed(ServerUniqueGameIdentifier gameID) {
		return games.containsKey(gameID);
	}

	private void createNewGameWithGameID(ServerUniqueGameIdentifier gameID) {
		if (gameIDCreation.size() >= EGameConstants.MAX_NUM_OF_GAMES.getValue()) {
			games.remove(gameIDCreation.remove());
		}

		games.put(gameID, new Game());
		gameIDCreation.add(gameID);
	}
}
