package helpers;

import java.util.HashMap;

import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public class Helper {
	public static HashMap<Position, ETerrain> arrayToMap(char[][] nodes) {
		HashMap<Position, ETerrain> ret = new HashMap<>();

		for (int y = 0; y < nodes.length; ++y)
			for (int x = 0; x < nodes[y].length; ++x) {
				ETerrain n;
				switch (nodes[y][x]) {
				case 'm':
					n = ETerrain.MOUNTAIN;
					break;
				case 'w':
					n = ETerrain.WATER;
					break;
				default:
					n = ETerrain.GRASS;
				}
				ret.put(new Position(x, y), n);
			}

		return ret;
	}
}
