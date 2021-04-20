package moveGeneration.pathfinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.Position;

public class DijkstraData {

	private HashMap<Position, Integer> cost;
	private HashMap<Position, Queue<Position>> pathTo;
	private Set<Position> visited;
	private Set<Position> frontier;

	private int mapWidth;
	private int mapHeight;

	private static Logger logger = LoggerFactory.getLogger(DijkstraData.class);

	public DijkstraData(Position start, int mapWidth, int mapHeight) {
		if (start == null) {
			logger.error("start Position in DijkstraData was null");
			throw new IllegalArgumentException("start cannot be null");
		}

		if (mapWidth < 0 || mapHeight < 0) {
			logger.error("At least one of the map dimensions passed was invalid (bellow 0); mapWidth: " + mapWidth
					+ ", mapHeight: " + mapHeight);
			throw new IllegalArgumentException("map dimensions must be greater then 0");
		}

		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;

		cost = new HashMap<>();
		pathTo = new HashMap<>();
		// initialize the cost with 0 at the start
		cost.put(start, 0);
		// initialize the path with an queue containing just the start at the start
		pathTo.put(start, new LinkedList<>());
		pathTo.get(start).add(start);

		visited = new HashSet<>();
		frontier = new HashSet<>();
	}

	public Position visitMinimal() {
		Position min = frontier.stream().min((Position lhs, Position rhs) -> {
			return cost.get(lhs) - cost.get(rhs);
		}).get();

		frontier.remove(min);
		visited.add(min);

		return min;
	}

	public Set<Position> getNeigbours(Position pos) {
		Set<Position> neighbours = new HashSet<>();

		// up
		if (pos.gety() > 0 && !visited.contains(new Position(pos.getx(), pos.gety() - 1)))
			neighbours.add(new Position(pos.getx(), pos.gety() - 1));

		// dowm
		if (pos.gety() < mapHeight - 1 && !visited.contains(new Position(pos.getx(), pos.gety() + 1)))
			neighbours.add(new Position(pos.getx(), pos.gety() + 1));

		// right
		if (pos.getx() < mapWidth - 1 && !visited.contains(new Position(pos.getx() + 1, pos.gety())))
			neighbours.add(new Position(pos.getx() + 1, pos.gety()));

		// left
		if (pos.getx() > 0 && !visited.contains(new Position(pos.getx() - 1, pos.gety())))
			neighbours.add(new Position(pos.getx() - 1, pos.gety()));

		return neighbours;
	}

}
