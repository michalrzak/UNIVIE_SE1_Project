package server.games;

import server.games.helpers.ServerUniqueGameIdentifier;

public class GameController {

	final private int MAX_NUM_OF_GAMES = 999;

	public ServerUniqueGameIdentifier createNewGame() {
		ServerUniqueGameIdentifier newID = new ServerUniqueGameIdentifier();
		while (checkGameIDUsed(newID)) {
			newID = new ServerUniqueGameIdentifier();
		}

		createNewGameWithGameID(newID);

		return newID;
	}

	private boolean checkGameIDUsed(ServerUniqueGameIdentifier gameID) {
		// TODO: make this meaningfull
		return false;
	}

	private void createNewGameWithGameID(ServerUniqueGameIdentifier gameID) {
		return;
	}

}
