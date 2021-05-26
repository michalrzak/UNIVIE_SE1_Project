package games;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import games.helpers.ServerUniqueGameIdentifier;
import games.helpers.ServerUniquePlayerIdentifier;

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

	public ServerUniquePlayerIdentifier registerPlayer(ServerUniqueGameIdentifier gameID, String firstName,
			String lastName, String studentID) {
		return null;
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
