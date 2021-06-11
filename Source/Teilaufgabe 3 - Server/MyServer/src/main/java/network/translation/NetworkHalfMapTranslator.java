package network.translation;

import java.util.HashMap;
import java.util.Map;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import exceptions.InvalidDataException;
import game.map.SHalfMap;
import game.map.helpers.EMapType;
import game.map.helpers.ESTerrain;
import game.map.helpers.Position;

public class NetworkHalfMapTranslator {

	public SHalfMap translateNetworkHalfMap(HalfMap netHalfMap) {

		Map<Position, ESTerrain> terrainMap = extractTerrainMap(netHalfMap);
		// assert the terrain map has the right number of nodes. Any of the two map
		// types could have been used here
		assert (terrainMap.size() == EMapType.LONGMAP.getHalfHeight() * EMapType.LONGMAP.getHalfWidth());

		Position castlePosition = findCastle(netHalfMap);

		NetworkTranslator translate = new NetworkTranslator();
		return new SHalfMap(terrainMap, castlePosition, translate.networkPlayerIDToInternal(netHalfMap));

	}

	// has to be public as it is usefull for rule validation
	public Map<Position, ETerrain> extractNetorkHalfMapTerrainMap(HalfMap netHalfMap) {
		Map<Position, ETerrain> ret = new HashMap<>();

		var halfmapNodes = netHalfMap.getNodes();

		halfmapNodes.stream().forEach(node -> ret.put(new Position(node.getX(), node.getY()), node.getTerrain()));

		return ret;
	}

	private ESTerrain translateTerrain(ETerrain terrain) {

		switch (terrain) {
		case Grass:
			return ESTerrain.GRASS;
		case Mountain:
			return ESTerrain.MOUNTAIN;
		case Water:
			return ESTerrain.WATER;
		}

		throw new InvalidDataException("the passed terrain contained an unexpected realization");

	}

	private Map<Position, ESTerrain> extractTerrainMap(HalfMap netHalfMap) {
		Map<Position, ETerrain> networkTerrain = extractNetorkHalfMapTerrainMap(netHalfMap);

		Map<Position, ESTerrain> ret = new HashMap<>();
		for (var posTerrain : networkTerrain.entrySet()) {
			ESTerrain terrain = translateTerrain(posTerrain.getValue());
			ret.put(posTerrain.getKey(), terrain);
		}

		return ret;
	}

	private Position findCastle(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		var castleNode = halfmapNodes.stream().filter(node -> node.isFortPresent()).findFirst().get();

		return new Position(castleNode.getX(), castleNode.getY());
	}
}
