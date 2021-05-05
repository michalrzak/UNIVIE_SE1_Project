package moveGeneration;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.fullMap.FullMapAccesser;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public class ImportantNodesExtractor {

	private final FullMapAccesser fma;
	private final EMapHalf myHalf;

	private static Logger logger = LoggerFactory.getLogger(ImportantNodesExtractor.class);

	public ImportantNodesExtractor(FullMapAccesser fma, EMapHalf myHalf) {
		if (fma == null || myHalf == null) {
			logger.error("One of the passed arguments was null!");
			throw new IllegalArgumentException("arguments cannot be null!");
		}

		this.fma = fma;
		this.myHalf = myHalf;
	}

	private Set<Position> extractGrassNodes() {
		Set<Position> ret = new HashSet<>();

		for (int y = myHalf.getyLowerBound(); y <= myHalf.getyUpperBound(); ++y) {
			for (int x = myHalf.getxLowerBound(); x <= myHalf.getxUpperBound(); ++x) {
				Position cur = new Position(x, y);
				if (fma.getTerrainAt(cur) == ETerrain.GRASS && !fma.getEntityPosition(EGameEntity.MYCASTLE).equals(cur))
					ret.add(cur);
			}
		}

		return ret;
	}

	private Set<Position> addMountainsWhereEfficient(Set<Position> allGrassNodes) {

		Set<Position> ret = new HashSet<>(allGrassNodes);

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
									&& fma.getTerrainAt(new Position(x + xDiff, y + yDiff)) == ETerrain.GRASS
									&& ret.contains(new Position(x + xDiff, y + yDiff)))
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

	// used to help the constructor find nodes to visit
	public Set<Position> getInterestingNodes() {

		Set<Position> allGrassNodes = extractGrassNodes();
		Set<Position> finalNodes = addMountainsWhereEfficient(allGrassNodes);

		return finalNodes;
	}
}
