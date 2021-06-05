package network;

import java.util.HashMap;
import java.util.Map;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidDataException;
import gamedata.game.helpers.ServerUniqueGameIdentifier;
import gamedata.map.HalfMapData;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;
import rules.helpers.EHalfMapHelpers;

public class NetworkTranslator {

	public ServerUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new ServerUniqueGameIdentifier(gameID.getUniqueGameID());
	}

	public UniqueGameIdentifier internalGameIDToNetwork(ServerUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.getIDAsString());
	}

	public ServerUniquePlayerIdentifier networkPlayerIDToInternal(UniquePlayerIdentifier playerID) {
		return new ServerUniquePlayerIdentifier(playerID.getUniquePlayerID());
	}

	public UniquePlayerIdentifier internalPlayerIDToNetwork(ServerUniquePlayerIdentifier playerID) {
		return new UniquePlayerIdentifier(playerID.getPlayerIDAsString());
	}

	public PlayerInformation networkPlayerRegistrationtoInternal(PlayerRegistration playerReg) {
		return new PlayerInformation(playerReg.getStudentFirstName(), playerReg.getStudentLastName(),
				playerReg.getStudentID());
	}

	public HalfMapData networkHalfMapToInernal(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		Map<Position, ETerrain> terrainMap = extractTerrainMap(halfmap);
		Position castlePosition = findCastle(halfmap);

		return new HalfMapData(terrainMap, castlePosition, networkPlayerIDToInternal(halfmap));
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

		for (Position pos : ret.keySet()) {
			switch (networkTerrain.get(pos)) {
			case Grass:
				ret.put(pos, ETerrain.GRASS);
				break;
			case Mountain:
				ret.put(pos, ETerrain.MOUNTAIN);
				break;
			case Water:
				ret.put(pos, ETerrain.WATER);
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
