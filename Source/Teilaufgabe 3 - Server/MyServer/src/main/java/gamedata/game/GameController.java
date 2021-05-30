package gamedata.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import exceptions.GameNotFoundException;
import gamedata.game.helpers.ServerUniqueGameIdentifier;
import gamedata.map.HalfMapData;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class GameController {

	static final private int MAX_NUM_OF_GAMES = 999;

	final private Map<ServerUniqueGameIdentifier, Game> games = new HashMap<>();
	final private Queue<ServerUniqueGameIdentifier> gameIDCreation = new LinkedList<>();

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
			throw new GameNotFoundException("The passed gameID was not found");
		}

		return games.get(gameID).registerPlayer(playerInf);
	}

	public void addHalfMap(ServerUniqueGameIdentifier gameID, ServerUniquePlayerIdentifier playerID,
			HalfMapData hmdata) {
		if (!games.containsKey(gameID)) {
			throw new GameNotFoundException("The passed gameID was not found");
		}
		games.get(gameID).receiveHalfMap(playerID, hmdata);
	}

	private boolean checkGameIDUsed(ServerUniqueGameIdentifier gameID) {
		return games.containsKey(gameID);
	}

	private void createNewGameWithGameID(ServerUniqueGameIdentifier gameID) {
		if (gameIDCreation.size() >= MAX_NUM_OF_GAMES) {
			games.remove(gameIDCreation.remove());
		}

		games.put(gameID, new Game());
		gameIDCreation.add(gameID);
	}

}
