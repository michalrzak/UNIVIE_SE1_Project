package moveGeneration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public abstract class NodeFinder {
	private FullMapAccesser fma;
	private Queue<Position> visitOrder;
	private Queue<Position> pathToNextNode = null;
	private boolean goingToTreasure = false;
	private EGameEntity lookingFor;

	private static Logger logger = LoggerFactory.getLogger(TreasureFinder.class);

	public NodeFinder(FullMapAccesser fma, EMapHalf myHalf, EGameEntity lookingFor) {

		// TODO: CHECKS!

		this.fma = fma;
		this.lookingFor = lookingFor;

		Set<Position> toVisit = getInterestingNodes(fma, myHalf);
		this.visitOrder = visitInOrder(toVisit, fma.getEntityPosition(EGameEntity.MYPLAYER));

		if (visitOrder.stream().distinct().count() != visitOrder.size())
			logger.warn("The algorithm wants to visit a node more then once!");
	}

	// used to help the constructor find nodes to visit
	private static Set<Position> getInterestingNodes(FullMapAccesser fma, EMapHalf myHalf) {

		Set<Position> ret = new HashSet<>();

		// first add all grass fields
		for (int y = myHalf.getyLowerBound(); y <= myHalf.getyUpperBound(); ++y) {
			for (int x = myHalf.getxLowerBound(); x <= myHalf.getxUpperBound(); ++x) {
				Position cur = new Position(x, y);
				if (fma.getTerrainAt(cur) == ETerrain.GRASS && !fma.getEntityPosition(EGameEntity.MYCASTLE).equals(cur))
					ret.add(cur);
			}
		}

		// then add some mountains and remove grass around them where this is more
		// efficient
		for (int y = myHalf.getyLowerBound(); y <= myHalf.getyUpperBound(); ++y) {
			for (int x = myHalf.getxLowerBound(); x <= myHalf.getxUpperBound(); ++x) {
				Position cur = new Position(x, y);
				if (fma.getTerrainAt(cur) == ETerrain.MOUNTAIN) {

					Set<Position> surroundingGrass = new HashSet<>();

					// loop over all adjacent positions
					for (int xDiff = -1; xDiff <= 1; ++xDiff) {
						for (int yDiff = -1; yDiff <= 1; ++yDiff) {
							if (xDiff == 0 && yDiff == 0)
								continue;
							if (x + xDiff >= 0 && x + xDiff < fma.getWidth() && y + yDiff >= 0
									&& y + yDiff < fma.getHeight()
									&& fma.getTerrainAt(new Position(x + xDiff, y + yDiff)) == ETerrain.GRASS)
								surroundingGrass.add(new Position(x + xDiff, y + yDiff));
						}
					}

					// if more than 2 grass tiles surround a mountain it is efficient to go up the
					// mountain
					if (surroundingGrass.size() > 2) {
						ret.removeAll(surroundingGrass);
						ret.add(cur);
					}
				}
			}
		}
		return ret;

	}

	private static Queue<Position> visitInOrder(Set<Position> toVisit, Position start) {
		// this uses heuristics to construct an OK order toVisit the nodes
		// more complicated algorithms could be used as well, I am just not sure whether
		// that is worth it.

		Queue<Position> ret = new LinkedList<>();

		Set<Position> toVisitCopy = new HashSet<>(toVisit);

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

	public Position getNextPosition() {

		// if Position of MyTreasure is known, go there
		if (fma.getEntityPosition(lookingFor) != null && !goingToTreasure) {
			pathToNextNode = PathFinder.pathTo(fma.getEntityPosition(EGameEntity.MYPLAYER),
					fma.getEntityPosition(lookingFor), fma);
			goingToTreasure = true;
			System.out.println("ola");
		}

		// check if I am not going anywhere
		if (pathToNextNode == null || pathToNextNode.size() == 0) {
			// if this evaluates to true then i have just arrived on the treasure!
			if (goingToTreasure)
				logger.warn("Trying to find the treasure even though it has already been collected!");

			logger.debug("Next stop is: " + visitOrder.peek().toString());

			// throws if visitOrder is empty!
			pathToNextNode = PathFinder.pathTo(fma.getEntityPosition(EGameEntity.MYPLAYER), visitOrder.remove(), fma);
		}

		return pathToNextNode.remove();
	}

}
