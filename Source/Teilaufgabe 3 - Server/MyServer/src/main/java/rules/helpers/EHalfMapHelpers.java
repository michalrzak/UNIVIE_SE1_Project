package rules.helpers;

import java.util.HashMap;
import java.util.Map;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import game.map.helpers.Position;

public enum EHalfMapHelpers {
	HALF_MAP_WIDTH(8), HALF_MAP_HEIGHT(4), MIN_GRASS_COUNT(15), MIN_MOUNTAIN_COUNT(3), MIN_WATER_COUNT(4);

	private final int value;

	private EHalfMapHelpers(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}

	public static Map<Position, ETerrain> extractMap(HalfMap halfmap) {
		Map<Position, ETerrain> ret = new HashMap<>();

		var halfmapNodes = halfmap.getNodes();

		halfmapNodes.stream().forEach(node -> ret.put(new Position(node.getX(), node.getY()), node.getTerrain()));

		return ret;
	}

}
