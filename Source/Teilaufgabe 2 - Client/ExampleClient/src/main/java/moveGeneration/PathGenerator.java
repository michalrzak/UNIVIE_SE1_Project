package moveGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.EGameEntity;
import mapHelpers.Position;
import moveHelpers.EMove;

public class PathGenerator {

	private FullMapAccesser fma;

	private static Logger logger = LoggerFactory.getLogger(PathGenerator.class);

	public PathGenerator(FullMapAccesser fma) {
		if (fma == null) {
			logger.error("PathGenerator constructor received a null FullMapAccesser");
			throw new IllegalArgumentException("fma was null");
		}

		this.fma = fma;
	}

	private EMove getDirection(Position from, Position to) {
		int distance = Math.abs(from.getx() - to.getx()) + Math.abs(from.gety() - to.gety());
		if (distance != 1) {
			logger.error("Trying to get direction to not adjescend node! from: " + from.toString() + " to: "
					+ to.toString());
			throw new IllegalArgumentException("Arguments must be adjescent to oneanother");
		}

		if (from.getx() - to.getx() == 1)
			return EMove.LEFT;
		if (from.getx() - to.getx() == -1)
			return EMove.RIGHT;
		if (from.gety() - to.gety() == 1)
			return EMove.UP;
		if (from.gety() - to.gety() == -1)
			return EMove.DOWN;

		throw new RuntimeException("Distance check must have failed!");

	}

	private Queue<EMove> positionsToDirections(Queue<Position> positions) {
		Queue<EMove> ret = new LinkedList<>();

		for (var ele : positions)
			System.out.println(ele.toString());

		Position prev = null;
		for (Position pos : positions) {
			if (prev != null) {
				// this is kind of scuffed. Maybe change this later.
				for (int i = 0; i < fma.getTerrainAt(prev).cost() + fma.getTerrainAt(pos).cost(); ++i)
					ret.add(getDirection(prev, pos));
			}

			prev = pos;
		}

		return ret;
	}

	// TODO: Refactor this class
	public Queue<EMove> getPathTo(Position pos) {
		Position current = fma.getEntityPosition(EGameEntity.MYPLAYER);

		HashMap<Position, Integer> cost = new HashMap<>();
		HashMap<Position, Queue<Position>> pathTo = new HashMap<>();

		Set<Position> visited = new HashSet<>();
		Set<Position> frontier = new HashSet<>();

		cost.put(current, 0);
		pathTo.put(current, new LinkedList<>());
		pathTo.get(current).add(current);

		while (!visited.contains(pos)) {
			Set<Position> neighbours = new HashSet<>();

			visited.add(current);

			// up
			if (current.gety() > 0 && !visited.contains(new Position(current.getx(), current.gety() - 1)))
				neighbours.add(new Position(current.getx(), current.gety() - 1));

			// dowm
			if (current.gety() < fma.getHeight() - 1
					&& !visited.contains(new Position(current.getx(), current.gety() + 1)))
				neighbours.add(new Position(current.getx(), current.gety() + 1));

			// right
			if (current.getx() < fma.getWidth() - 1
					&& !visited.contains(new Position(current.getx() + 1, current.gety())))
				neighbours.add(new Position(current.getx() + 1, current.gety()));

			// left
			if (current.getx() > 0 && !visited.contains(new Position(current.getx() - 1, current.gety())))
				neighbours.add(new Position(current.getx() - 1, current.gety()));

			for (Position p : neighbours) {
				if (cost.get(p) == null || cost.get(p) >= cost.get(current) + fma.getTerrainAt(current).cost()
						+ fma.getTerrainAt(p).cost()) {

					cost.put(p, cost.get(current) + fma.getTerrainAt(current).cost() + fma.getTerrainAt(p).cost());

					Queue<Position> temp = new LinkedList<>(pathTo.get(current));
					temp.add(p);

					pathTo.put(p, temp);
				}
				frontier.add(p);
			}

			current = frontier.stream().min((Position lhs, Position rhs) -> {
				return cost.get(lhs) - cost.get(rhs);
			}).get();

			logger.debug("Current node in dijkstra: " + current + " Cost: " + cost.get(current));

			frontier.remove(current);

		}

		logger.debug("Dijkstra cost of destination node: " + cost.get(pos));

		for (var ele : pathTo.get(pos))
			System.out.println(ele);
		System.out.println("------------------");

		// return pathTo.get(pos);

		return positionsToDirections(pathTo.get(pos));
	}
}
