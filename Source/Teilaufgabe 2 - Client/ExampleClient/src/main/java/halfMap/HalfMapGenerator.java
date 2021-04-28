package halfMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.ETerrain;
import mapHelpers.Position;

public class HalfMapGenerator {

	private static Logger logger = LoggerFactory.getLogger(HalfMapGenerator.class);

	private static Position placeCastle(Map<Position, ETerrain> terrain) {
		Random rnd = new Random();

		Position castle = new Position(rnd.nextInt(8), rnd.nextInt(4));
		while (terrain.get(castle) != ETerrain.GRASS)
			castle = new Position(rnd.nextInt(8), rnd.nextInt(4));

		return castle;
	}

	private static Map<Position, ETerrain> generateTerrain() {

		// i can maybe delegate this to HalfMapData
		int grassCount = 15;
		int mountainCount = 4;
		int waterCount = 3;

		Stack<ETerrain> nodes = new Stack<>();

		// insert all grassFields
		for (int i = 0; i < grassCount; ++i)
			nodes.push(ETerrain.GRASS);

		// insert all mountainFields
		for (int i = 0; i < mountainCount; ++i)
			nodes.push(ETerrain.MOUNTAIN);

		// insert all mountainFields
		for (int i = 0; i < waterCount; ++i)
			nodes.push(ETerrain.WATER);

		// insert rest of fields
		Random rnd = new Random();
		for (int i = grassCount + mountainCount + waterCount; i < 32; ++i) {
			int res = rnd.nextInt(100);
			if (res < 75) // higher chance to generate mountain
				nodes.push(ETerrain.MOUNTAIN);
			else
				nodes.push(ETerrain.WATER);
		}

		Collections.shuffle(nodes);

		Map<Position, ETerrain> terrain = new HashMap<>();
		for (int y = 0; y < 4; ++y)
			for (int x = 0; x < 8; ++x)
				terrain.put(new Position(x, y), nodes.pop());

		return terrain;
	}

	public static HalfMapData generateMap() {

		// temporary Map creation
		// TODO: make this a valid map creation

		Map<Position, ETerrain> terrain = generateTerrain();
		Position castle = placeCastle(terrain);

		HalfMapData hmd;
		try {
			hmd = new HalfMapData(terrain, castle);
		} catch (IllegalArgumentException e) {
			hmd = generateMap();
		}
		return hmd;
	}

}
