package moveGeneration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.mapHelpers.Position;

public class ShortestPathExtractor {

	private static Logger logger = LoggerFactory.getLogger(ShortestPathExtractor.class);

	// this uses heuristics to construct an OK order toVisit the nodes
	// more complicated algorithms could be used as well, I am just not sure whether
	// that is worth it.
	public Queue<Position> visitInOrder(Set<Position> toVisit, Position start) {

		Queue<Position> ret = new LinkedList<>();

		Set<Position> toVisitCopy = new HashSet<>(toVisit);

		// if toVisit also contaisn the start node, remove it
		toVisit.remove(start);

		while (toVisitCopy.size() != 0) {
			// needed to be able to pass it into the lambda
			Position cur = start;
			Position closest = toVisitCopy.stream().min((Position x1, Position x2) -> {
				return Double.compare(Position.distance(cur, x1), Position.distance(cur, x2));
			}).get();

			ret.add(closest);
			toVisitCopy.remove(closest);
			start = closest;
		}

		return ret;

	}

}
