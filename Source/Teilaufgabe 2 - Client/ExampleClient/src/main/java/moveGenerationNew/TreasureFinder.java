package moveGenerationNew;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;
import moveGeneration.FullMapAccesser;

public class TreasureFinder {

	private FullMapAccesser fma;
	private Queue<Position> visitOrder;
	private Queue<Position> pathToNextNode = null;
	private boolean goingToTreasure = false;

	public TreasureFinder(FullMapAccesser fma) {
		this.fma = fma;

		Set<Position> toVisit = getInterestingNodes(fma);
		this.visitOrder = visitInOrder(toVisit, fma.getEntityPosition(EGameEntity.MYPLAYER));
	}

	// used to help the constructor find nodes to visit
	private static Set<Position> getInterestingNodes(FullMapAccesser fma) {

		// this section is not very nice
		Position fromMyMap;
		Position toMyMap;

		if (fma.getHeight() == 4) {
			int xsep = fma.getWidth() / 2;

			if (fma.getEntityPosition(EGameEntity.MYCASTLE).getx() < xsep) {
				fromMyMap = new Position(0, 0);
				toMyMap = new Position(xsep - 1, fma.getHeight() - 1);
			} else {
				fromMyMap = new Position(xsep, 0);
				toMyMap = new Position(fma.getWidth() - 1, fma.getHeight() - 1);
			}
		} else {
			int ysep = fma.getHeight() / 2;

			if (fma.getEntityPosition(EGameEntity.MYCASTLE).gety() < ysep) {
				fromMyMap = new Position(0, 0);
				toMyMap = new Position(fma.getWidth() - 1, ysep - 1);
			} else {
				fromMyMap = new Position(0, ysep);
				toMyMap = new Position(fma.getWidth() - 1, fma.getHeight() - 1);
			}
		}

		Set<Position> ret = new HashSet<>();

		// first add all grass fields
		for (int y = fromMyMap.gety(); y <= toMyMap.gety(); ++y) {
			for (int x = fromMyMap.getx(); x <= toMyMap.getx(); ++x) {
				Position cur = new Position(x, y);
				if (fma.getTerrainAt(cur) == ETerrain.GRASS && !fma.getEntityPosition(EGameEntity.MYCASTLE).equals(cur))
					ret.add(cur);
			}
		}

		// then add some mountains and remove grass around them where this is more
		// efficient
		for (int y = fromMyMap.gety(); y <= toMyMap.gety(); ++y) {
			for (int x = fromMyMap.getx(); x <= toMyMap.getx(); ++x) {
				Position cur = new Position(x, y);
				if (fma.getTerrainAt(cur) == ETerrain.MOUNTAIN) {

					Set<Position> surroundingGrass = new HashSet<>();

					// loop over all adjacent positions
					for (int xDiff = -1; xDiff <= 1; ++xDiff) {
						for (int yDiff = -1; yDiff <= 1; ++yDiff) {
							if (xDiff == 0 && yDiff == 0)
								continue;
							if (x + xDiff >= 0 && x + xDiff < fma.getWidth() && y + yDiff >= 0
									&& y + yDiff < fma.getHeight())
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
		if (fma.getEntityPosition(EGameEntity.MYTREASURE) != null && !goingToTreasure) {
			pathToNextNode = PathFinder.pathTo(fma.getEntityPosition(EGameEntity.MYPLAYER),
					fma.getEntityPosition(EGameEntity.MYTREASURE), fma);
			goingToTreasure = true;
		}

		// check if I am not going anywhere
		if (pathToNextNode == null || pathToNextNode.size() == 0)
			// throws if visitOrder is empty!
			pathToNextNode = PathFinder.pathTo(fma.getEntityPosition(EGameEntity.MYPLAYER), visitOrder.remove(), fma);

		return pathToNextNode.remove();
	}

}
