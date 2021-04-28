package map.halfMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.InvalidHalfMapGeneratedException;
import exceptions.PositionOutOfBoundsException;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public class HalfMapData {
	private Map<Position, ETerrain> terrain;
	private Position myCastlePosition;

	private static Logger logger = LoggerFactory.getLogger(HalfMapData.class);

	private static boolean validateTerrainDimensions(Map<Position, ETerrain> testing) {
		Set<Position> positions = testing.keySet();

		if (positions.size() != 32) {
			logger.error("HalfMap size is not 32, size is: " + positions.size());
			return false;
		}

		if (!positions.stream().allMatch(pos -> pos.getx() < 8 && pos.gety() < 4)) {
			logger.error("HalfMap terrain hashmap position invalid");
			return false;
		}
		return true;
	}

	private static boolean validateTerrainFieldCount(Map<Position, ETerrain> testing) {
		Collection<ETerrain> t = testing.values();

		long countGrass = t.stream().filter(ter -> ter == ETerrain.GRASS).count();
		long countWater = t.stream().filter(ter -> ter == ETerrain.WATER).count();
		long countMountain = t.stream().filter(ter -> ter == ETerrain.MOUNTAIN).count();

		if (countGrass < 15 || countWater < 4 || countMountain < 3) {
			logger.error("HalfMap terrain does not contain enough of a certain terrain type countGrass: " + countGrass
					+ ", countWater: " + countWater + ", countMountain:" + countMountain);
			return false;
		}

		return true;
	}

	private static boolean validateTerrainEdges(Map<Position, ETerrain> testing) {
		// check if half of the edge is made up of water

		var mapset = testing.entrySet();

		long leftEdgeCount = mapset.stream().filter(ele -> ele.getKey().getx() == 0 && ele.getValue() == ETerrain.WATER)
				.count();
		long rightEdgeCount = mapset.stream()
				.filter(ele -> ele.getKey().getx() == 7 && ele.getValue() == ETerrain.WATER).count();
		long topEdgeCount = mapset.stream().filter(ele -> ele.getKey().gety() == 0 && ele.getValue() == ETerrain.WATER)
				.count();
		long botEdgeCount = mapset.stream().filter(ele -> ele.getKey().gety() == 3 && ele.getValue() == ETerrain.WATER)
				.count();

		if (leftEdgeCount >= 2 || rightEdgeCount >= 2 || topEdgeCount >= 4 || botEdgeCount >= 4) {
			logger.error("HalfMap terrain generated too much on one of the edges left: " + leftEdgeCount + ", right: "
					+ rightEdgeCount + ", top: " + topEdgeCount + ", bottom: " + botEdgeCount);
			return false;
		}
		return true;
	}

	private static void floodfill(Position node, Map<Position, ETerrain> nodes) {
		// THE HASHMAP PASSES AS ARGUMENT HERE WILL BE MODIFIED. PROVIDE A COPY
		// THE START NODE CANNOT BE WATER
		if (!nodes.containsKey(node) || nodes.get(node) == ETerrain.WATER)
			return;

		nodes.remove(node);

		// north
		if (node.gety() - 1 >= 0)
			floodfill(new Position(node.getx(), node.gety() - 1), nodes);

		floodfill(new Position(node.getx(), node.gety() + 1), nodes);
		// east
		if (node.getx() - 1 >= 0)
			floodfill(new Position(node.getx() - 1, node.gety()), nodes);
		// west
		floodfill(new Position(node.getx() + 1, node.gety()), nodes);
	}

	private static boolean validateTerrainIslands(Map<Position, ETerrain> testing) {
		// provide a copy of the hashmap to floodfill
		var copy = new HashMap<Position, ETerrain>(testing);

		// start floodfill from 0, 0
		floodfill(new Position(0, 0), copy);

		// map contains islands if it is not empty and at least one node inside is not
		// water
		if (copy.size() != 0 && !copy.values().stream().allMatch(ele -> ele == ETerrain.WATER)) {
			logger.error("HalfMap contains an Island!");
			return false;
		}

		return true;
	}

	private static boolean validateTerrain(Map<Position, ETerrain> testing) {
		return validateTerrainDimensions(testing) && validateTerrainFieldCount(testing) && validateTerrainEdges(testing)
				&& validateTerrainIslands(testing);
	}

	public HalfMapData(Map<Position, ETerrain> terrain, Position myCastlePosition)
			throws InvalidHalfMapGeneratedException {
		if (terrain == null || myCastlePosition == null) {
			logger.error("HalfMapData constructor received a null parameter");
			throw new IllegalArgumentException("Arguments cannot be null!");
		}

		if (myCastlePosition.getx() >= 8 || myCastlePosition.gety() >= 4) {
			logger.error(
					"HalfMapData constructor received a myFortPosition object with position outside of halfmap; Received: "
							+ myCastlePosition);
			throw new PositionOutOfBoundsException("myFortPosition x must be in range [0; 8) and y in range [0; 4)",
					myCastlePosition);
		}

		if (!validateTerrain(terrain)) {
			logger.error("HalfMapData constructor received an invalid Terrain map!");
			throw new InvalidHalfMapGeneratedException();
		}

		if (terrain.get(myCastlePosition) != ETerrain.GRASS) {
			logger.error("Castle is not placed on a grass field!");
			throw new RuntimeException("Castle is not placed on a grass field");
		}

		// maybe make this copy the object
		this.terrain = terrain;
		this.myCastlePosition = myCastlePosition;
	}

	// TODO: is this a good idea?
	public Stream<Entry<Position, ETerrain>> getStream() {
		return terrain.entrySet().stream();
	}

	public Position castlePosition() {
		return myCastlePosition;
	}

}
