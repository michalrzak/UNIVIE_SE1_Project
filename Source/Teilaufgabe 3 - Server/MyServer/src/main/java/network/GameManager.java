package network;

import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import games.GameController;
import games.helpers.ServerUniqueGameIdentifier;
import player.helpers.PlayerInformation;
import player.helpers.ServerUniquePlayerIdentifier;

public class GameManager {

	final private GameController games = new GameController();
	final private NetworkTranslator translate = new NetworkTranslator();

	public UniqueGameIdentifier newGame() {
		return translate.internalGameIDToNetwork(games.createNewGame());
	}

	public UniquePlayerIdentifier registerPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerReg) {
		// should be validated by spring
		ServerUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		PlayerInformation playerInf = translate.networkPlayerRegistrationtoInternal(playerReg);

		ServerUniquePlayerIdentifier playerID = games.registerPlayer(serverGameID, playerInf);

		return translate.internalPlayerIDToNetwork(playerID);
	}

}
