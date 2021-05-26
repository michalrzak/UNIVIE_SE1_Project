package network;

import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import games.GameController;
import games.helpers.ServerUniqueGameIdentifier;

public class GameManager {

	final private GameController games = new GameController();
	final private NetworkTranslator translate = new NetworkTranslator();

	public UniqueGameIdentifier newGame() {
		return translate.internalGameIDToNetwork(games.createNewGame());
	}

	public UniquePlayerIdentifier registerPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerReg) {
		ServerUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
	}

}
