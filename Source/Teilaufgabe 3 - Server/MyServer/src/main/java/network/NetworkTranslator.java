package network;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import gamedata.game.helpers.ServerUniqueGameIdentifier;
import gamedata.map.HalfMapData;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class NetworkTranslator {

	public ServerUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new ServerUniqueGameIdentifier(gameID.getUniqueGameID());
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

	public HalfMapData networkHalfMapToInernal(HalfMap halfm) {
		// TODO: something
		// TODO: implement internal HalfMap

		return null;
	}

}