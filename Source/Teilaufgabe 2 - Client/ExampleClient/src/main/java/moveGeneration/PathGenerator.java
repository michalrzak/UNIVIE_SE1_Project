package moveGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	// TODO: Refactor this class
	public List<EMove> getPathTo(Position pos) {
		Position current = fma.getEntityPosition(EGameEntity.MYPLAYER);

		HashMap<Position, Integer> cost = new HashMap<>();

		Set<Position> neighbours = new HashSet<>();
		Set<Position> visited = new HashSet<>();

		cost.put(current, 0);

		while (!visited.contains(pos)) {
			visited.add(current);

			// up
			if (current.gety() > 0 && !visited.contains(new Position(current.getx(), current.gety() - 1)))
				neighbours.add(new Position(current.getx(), current.gety() - 1));

			// dowm
			if (current.gety() < fma.getHeight() && !visited.contains(new Position(current.getx(), current.gety() + 1)))
				neighbours.add(new Position(current.getx(), current.gety() + 1));

			// right
			if (current.getx() < fma.getWidth() && !visited.contains(new Position(current.getx() + 1, current.gety())))
				neighbours.add(new Position(current.getx() + 1, current.gety()));

			// left
			if (current.getx() > 0 && !visited.contains(new Position(current.getx() - 1, current.gety())))
				neighbours.add(new Position(current.getx() - 1, current.gety()));

			for (Position p : neighbours) {
				if (cost.get(p) == null || cost.get(p) >= cost.get(current) + fma.getTerrainAt(current).cost()
						+ fma.getTerrainAt(p).cost())
					cost.put(p, cost.get(current) + fma.getTerrainAt(current).cost() + fma.getTerrainAt(p).cost());
			}

			current = neighbours.stream().max((Position lhs, Position rhs) -> {
				return cost.get(lhs) - cost.get(rhs);
			}).get();

			neighbours.remove(current);

		}

		logger.debug("Dijkstra cost of destination node: " + cost.get(pos));

		// TODO: figure out how to get a path to the destination node and not just the
		// cost to get there!
		return null;
	}
}
