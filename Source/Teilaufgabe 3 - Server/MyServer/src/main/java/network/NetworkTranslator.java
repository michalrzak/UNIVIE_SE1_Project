package network;

import java.util.HashMap;
import java.util.Map;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidDataException;
import gamedata.game.helpers.SUniqueGameIdentifier;
import gamedata.map.SHalfMap;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.SUniquePlayerIdentifier;
import rules.helpers.EHalfMapHelpers;

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
		var halfmapNodes = halfmap.getNodes();

		Map<Position, ETerrain> terrainMap = extractTerrainMap(halfmap);
		Position castlePosition = findCastle(halfmap);

		// TODO: MAGIC NUMBER!
		assert (terrainMap.size() == 32);

		return new SHalfMap(terrainMap, castlePosition, networkPlayerIDToInternal(halfmap));
	}

	public MessagesBase.ETerrain internalTerrainToNetwork(ETerrain terrain) {

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

	private Map<Position, ETerrain> extractTerrainMap(HalfMap halfmap) {
		Map<Position, MessagesBase.ETerrain> networkTerrain = EHalfMapHelpers.extractMap(halfmap);

		Map<Position, ETerrain> ret = new HashMap<>();
		for (var posTerrain : networkTerrain.entrySet()) {
			switch (posTerrain.getValue()) {
			case Grass:
				ret.put(posTerrain.getKey(), ETerrain.GRASS);
				break;
			case Mountain:
				ret.put(posTerrain.getKey(), ETerrain.MOUNTAIN);
				break;
			case Water:
				ret.put(posTerrain.getKey(), ETerrain.WATER);
				break;
			}
		}

		return ret;
	}

	private Position findCastle(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		var castleNode = halfmapNodes.stream().filter(node -> node.isFortPresent()).findFirst().get();

		return new Position(castleNode.getX(), castleNode.getY());
	}

}
