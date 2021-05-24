package server.network;

import MessagesBase.UniqueGameIdentifier;
import server.games.GameController;
import server.games.helpers.ServerUniqueGameIdentifier;

public class GameManager {

	final private GameController games = new GameController();

	public UniqueGameIdentifier newGame() {
		return convertGameIDToNetwork(games.createNewGame());
	}

	private UniqueGameIdentifier convertGameIDToNetwork(ServerUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.getIDAsString());
	}

}