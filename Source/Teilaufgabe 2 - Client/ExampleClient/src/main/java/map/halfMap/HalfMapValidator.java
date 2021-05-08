package map.halfMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.helpers.EMapDimensions;
import map.helpers.ETerrain;
import map.helpers.Position;

public class HalfMapValidator {

	private Map<Position, ETerrain> testing;

	private static Logger logger = LoggerFactory.getLogger(HalfMapValidator.class);

	public HalfMapValidator(Map<Position, ETerrain> testing) {
		if (testing == null) {
			logger.error("The passed map was null!");
			throw new IllegalArgumentException("Map cannot be null!");
		}
		this.testing = testing;
	}

	private boolean validateTerrainDimensions() {
		Set<Position> positions = testing.keySet();

		if (positions.size() != EMapDimensions.HALFMAP.width() * EMapDimensions.HALFMAP.height()) {
			logger.error("HalfMap size is not 32, size is: " + positions.size());
			return false;
		}

		if (!positions.stream().allMatch(
				pos -> pos.getx() < EMapDimensions.HALFMAP.width() && pos.gety() < EMapDimensions.HALFMAP.height())) {
			logger.error("HalfMap terrain hashmap position invalid");
			return false;
		}
		return true;
	}

	private boolean validateTerrainFieldCount() {
		Collection<ETerrain> t = testing.values();

		long countGrass = t.stream().filter(ter -> ter == ETerrain.GRASS).count();
		long countWater = t.stream().filter(ter -> ter == ETerrain.WATER).count();
		long countMountain = t.stream().filter(ter -> ter == ETerrain.MOUNTAIN).count();

		if (countGrass < ETerrain.GRASS.minAmount() || countWater < ETerrain.WATER.minAmount()
				|| countMountain < ETerrain.MOUNTAIN.minAmount()) {
			logger.error("HalfMap terrain does not contain enough of a certain terrain type countGrass: " + countGrass
					+ ", countWater: " + countWater + ", countMountain:" + countMountain);
			return false;
		}

		return true;
	}

	private boolean validateTerrainEdges() {
		// check if half of the edge is made up of water

		var mapset = testing.entrySet();

		long leftEdgeCount = mapset.stream().filter(ele -> ele.getKey().getx() == 0 && ele.getValue() == ETerrain.WATER)
				.count();
		long rightEdgeCount = mapset.stream().filter(
				ele -> ele.getKey().getx() == EMapDimensions.HALFMAP.width() - 1 && ele.getValue() == ETerrain.WATER)
				.count();
		long topEdgeCount = mapset.stream().filter(ele -> ele.getKey().gety() == 0 && ele.getValue() == ETerrain.WATER)
				.count();
		long botEdgeCount = mapset.stream().filter(
				ele -> ele.getKey().gety() == EMapDimensions.HALFMAP.height() - 1 && ele.getValue() == ETerrain.WATER)
				.count();

		final int SHORTEDGE_MAX = EMapDimensions.HALFMAP.height() / 2;
		final int LONGEDGE_MAX = EMapDimensions.HALFMAP.width() / 2;

		if (leftEdgeCount >= SHORTEDGE_MAX || rightEdgeCount >= SHORTEDGE_MAX || topEdgeCount >= LONGEDGE_MAX
				|| botEdgeCount >= LONGEDGE_MAX) {
			logger.error("HalfMap terrain generated too much on one of the edges left: " + leftEdgeCount + ", right: "
					+ rightEdgeCount + ", top: " + topEdgeCount + ", bottom: " + botEdgeCount);
			return false;
		}
		return true;
	}

	private boolean validateTerrainIslands() {
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

	private void floodfill(Position node, Map<Position, ETerrain> nodes) {
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

	public boolean validateTerrain() {
		return validateTerrainDimensions() && validateTerrainFieldCount() && validateTerrainEdges()
				&& validateTerrainIslands();
	}

	public boolean validateCastlePosition(Position myCastlePosition) {
		return testing.get(myCastlePosition) == ETerrain.GRASS;
	}

}
