package network.translation;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidDataException;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.map.helpers.ESTerrain;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class NetworkTranslator {

	public SUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new SUniqueGameIdentifier(gameID.getUniqueGameID());
	}

	public UniqueGameIdentifier internalGameIDToNetwork(SUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.getIDAsString());
	}

	public SUniquePlayerIdentifier networkPlayerIDToInternal(UniquePlayerIdentifier playerID) {
		return new SUniquePlayerIdentifier(playerID.getUniquePlayerID());
	}

	public UniquePlayerIdentifier internalPlayerIDToNetwork(SUniquePlayerIdentifier playerID) {
		return new UniquePlayerIdentifier(playerID.getPlayerIDAsString());
	}

	public PlayerInformation networkPlayerRegistrationtoInternal(PlayerRegistration playerReg) {
		return new PlayerInformation(playerReg.getStudentFirstName(), playerReg.getStudentLastName(),
				playerReg.getStudentID());
	}

	public SHalfMap networkHalfMapToInernal(HalfMap halfmap) {
		NetworkHalfMapTranslator halfMapTrans = new NetworkHalfMapTranslator();
		return halfMapTrans.translateNetworkHalfMap(halfmap);
	}

	public MessagesBase.ETerrain internalTerrainToNetwork(ESTerrain terrain) {

		switch (terrain) {
		case GRASS:
			return MessagesBase.ETerrain.Grass;
		case MOUNTAIN:
			return MessagesBase.ETerrain.Mountain;
		case WATER:
			return MessagesBase.ETerrain.Water;
		}

		throw new InvalidDataException("the passed terrain contained an unexpected realization");

	}
}
