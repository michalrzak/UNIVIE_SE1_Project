package move.generation;

import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.fullMap.FullMapAccesser;
import map.helpers.EGameEntity;
import map.helpers.EMapHalf;
import map.helpers.Position;

public class NodeFinder {
	private final FullMapAccesser fma;
	private final Queue<Position> visitOrder;
	private final EGameEntity lookingFor;

	private Queue<Position> pathToNextNode = null;
	private boolean goingToTreasure = false;

	private static Logger logger = LoggerFactory.getLogger(NodeFinder.class);

	public NodeFinder(FullMapAccesser fma, EMapHalf searchingOnHalf, EGameEntity lookingFor) {

		if (fma == null || searchingOnHalf == null || lookingFor == null) {
			logger.error("Arguments passed is null!");
			throw new IllegalArgumentException("one of the passed arguments is null");
		}

		this.fma = fma;
		this.lookingFor = lookingFor;

		ImportantNodesExtractor ine = new ImportantNodesExtractor(fma, searchingOnHalf);
		Set<Position> toVisit = ine.getInterestingNodes();

		ShortestPathExtractor spe = new ShortestPathExtractor();
		this.visitOrder = spe.visitInOrder(toVisit, fma.getEntityPosition(EGameEntity.MYPLAYER));

		if (visitOrder.stream().distinct().count() != visitOrder.size())
			logger.warn("The algorithm wants to visit a node more then once!");

		logger.debug("Printing nodes to visit:");
		visitOrder.stream().forEach(ele -> logger.debug("Going to: " + ele.toString()));
	}

	public Position getNextPosition() {

		PathFinder pf = new PathFinder();

		// if Position of MyTreasure is known, go there
		if (fma.getEntityPosition(lookingFor) != null && !goingToTreasure) {
			pathToNextNode = pf.pathTo(fma.getEntityPosition(EGameEntity.MYPLAYER), fma.getEntityPosition(lookingFor),
					fma);
			goingToTreasure = true;
		}

		// check if I am not going anywhere
		if (pathToNextNode == null || pathToNextNode.isEmpty()) {
			// if this evaluates to true then i have just arrived on the treasure!
			if (goingToTreasure)
				logger.warn("Trying to find the treasure even though it has already been collected!");

			logger.debug("Next stop is: " + visitOrder.peek().toString());

			// throws if visitOrder is empty!
			pathToNextNode = pf.pathTo(fma.getEntityPosition(EGameEntity.MYPLAYER), visitOrder.remove(), fma);
		}

		return pathToNextNode.remove();
	}

}
