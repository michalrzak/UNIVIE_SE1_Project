package halfMap;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.ETerrain;
import mapHelpers.Position;

public class HalfMapGenerator {

	private static Logger logger = LoggerFactory.getLogger(HalfMapGenerator.class);

	private Position placeTreasure(HashMap<Position, ETerrain> terrain) {
		// temporary treasure placement
		return new Position(0, 0);
	}

	private HashMap<Position, ETerrain> generateTerrain() {
		// temporary terrain generation
		HashMap<Position, ETerrain> terrain = new HashMap<>();
		int i = 0;
		// insert 15 grass fields
		for (; i < 15; ++i)
			terrain.put(new Position(i - (i / 8) * 8, i / 8), ETerrain.GRASS);
		// insert 4 water fields
		for (; i < 19; ++i)
			terrain.put(new Position(i - (i / 8) * 8, i / 8), ETerrain.WATER);
		// insert the rest as mountain
		for (; i < 32; ++i)
			terrain.put(new Position(i - (i / 8) * 8, i / 8), ETerrain.MOUNTAIN);

		return terrain;
	}

	public HalfMapData generateMap() {

		// temporary Map creation
		// TODO: make this a valid map creation

		HashMap<Position, ETerrain> terrain = generateTerrain();
		Position treasure = placeTreasure(terrain);

		return new HalfMapData(terrain, treasure);
	}

}
