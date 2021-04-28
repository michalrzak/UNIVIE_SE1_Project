package moveGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.mapHelpers.Position;

public class PathFinder {

	private static Logger logger = LoggerFactory.getLogger(PathFinder.class);

	public static Queue<Position> pathTo(Position start, Position dest, FullMapAccesser fma) {

		logger.debug("trying to find path from:" + start.toString() + " to: " + dest.toString());

		// this can technically be grouped to one; maybe ofload to different class?
		Map<Position, Integer> cost = new HashMap<>();
		Map<Position, Queue<Position>> pathTo = new HashMap<>();

		Set<Position> visited = new HashSet<>();
		Set<Position> frontier = new HashSet<>();

		// initialize the cost with 0 at the start
		cost.put(start, 0);
		// initialize the path with an queue containing just the start at the start
		pathTo.put(start, new LinkedList<>());
		// pathTo.get(start).add(start);

		while (!visited.contains(dest)) {
			// get all unvisited neighbors
			Set<Position> neighbours = getNeighbours(start, fma);
			neighbours.removeAll(visited);

			// add current node as visited
			visited.add(start);

			// update costs of neighbors
			for (Position p : neighbours) {
				if (cost.get(p) == null || cost.get(p) >= cost.get(start) + fma.getTerrainAt(start).cost()
						+ fma.getTerrainAt(p).cost()) {

					cost.put(p, cost.get(start) + fma.getTerrainAt(start).cost() + fma.getTerrainAt(p).cost());

					// I need to create a copy of this so I can add the new node without side
					// effects
					Queue<Position> temp = new LinkedList<>(pathTo.get(start));
					temp.add(p);

					pathTo.put(p, temp);
				}
				frontier.add(p);
			}

			start = frontier.stream().min((Position lhs, Position rhs) -> {
				return cost.get(lhs) - cost.get(rhs);
			}).get();

			logger.debug("start node in dijkstra: " + start + " Cost: " + cost.get(start));

			frontier.remove(start);

		}

		logger.debug("Dijkstra cost of destination node: " + cost.get(dest));

		return pathTo.get(dest);
	}

	private static Set<Position> getNeighbours(Position pos, FullMapAccesser fma) {
		Set<Position> ret = new HashSet<>();

		// up
		if (pos.gety() > 0)
			ret.add(new Position(pos.getx(), pos.gety() - 1));

		// down
		if (pos.gety() < fma.getHeight() - 1)
			ret.add(new Position(pos.getx(), pos.gety() + 1));

		// right
		if (pos.getx() < fma.getWidth() - 1)
			ret.add(new Position(pos.getx() + 1, pos.gety()));

		// left
		if (pos.getx() > 0)
			ret.add(new Position(pos.getx() - 1, pos.gety()));

		return ret;

	}

}
