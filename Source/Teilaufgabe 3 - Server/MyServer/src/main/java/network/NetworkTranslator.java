package network;

import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import games.helpers.ServerUniqueGameIdentifier;
import player.helpers.PlayerInformation;
import player.helpers.ServerUniquePlayerIdentifier;

public class NetworkTranslator {

	public ServerUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new ServerUniqueGameIdentifier();
	}

	public UniqueGameIdentifier internalGameIDToNetwork(ServerUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.getIDAsString());
	}

	public UniquePlayerIdentifier internalPlayerIDToNetwork(ServerUniquePlayerIdentifier playerID) {
		return new UniquePlayerIdentifier(playerID.getPlayerIDAsString());
	}

	public PlayerInformation networkPlayerRegistrationtoInternal(PlayerRegistration playerReg) {
		return new PlayerInformation(playerReg.getStudentFirstName(), playerReg.getStudentLastName(),
				playerReg.getStudentID());
	}

}
