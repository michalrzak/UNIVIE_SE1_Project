package moveGeneration;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.PositionOutOfBoundsException;
import map.fullMap.FullMapAccesser;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.Position;
import moveHelpers.EMove;

public class MoveGenerator {

	private final FullMapAccesser fma;
	private NodeFinder nodeFinder;

	// Moves to return to get to the next destination
	private Queue<EMove> toMove;

	private boolean changedToCastleFinding = false;

	private static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);

	public MoveGenerator(FullMapAccesser fma) {
		nodeFinder = new NodeFinder(fma, fma.getMyMapHalf(), EGameEntity.MYTREASURE);
		logger.debug("initializing node finder with map half = " + fma.getMyMapHalf() + " and seraching for = "
				+ EGameEntity.MYTREASURE);
		this.fma = fma;
	}

	private static Queue<EMove> movesToAdjascentNode(Position to, FullMapAccesser fma) {
		if (fma == null) {
			logger.error("Map accesser was passed as null!");
			throw new IllegalArgumentException("Map Accesser cannot be null!");
		}

		Position from = fma.getEntityPosition(EGameEntity.MYPLAYER);

		if (from.getx() >= fma.getWidth() || from.gety() >= fma.getHeight()) {
			logger.error("Position from out of bounds!");
			throw new PositionOutOfBoundsException("Position from out of bounds", from);
		}

		if (to.getx() >= fma.getWidth() || to.gety() >= fma.getHeight()) {
			logger.error("Position to out of bounds!");
			throw new PositionOutOfBoundsException("Position to out of bounds", to);
		}

		// ofload to another method?
		EMove dir;
		if (to.getx() - from.getx() == 1)
			dir = EMove.RIGHT;
		else if (to.getx() - from.getx() == -1)
			dir = EMove.LEFT;
		else if (to.gety() - from.gety() == 1)
			dir = EMove.DOWN;
		else if (to.gety() - from.gety() == -1)
			dir = EMove.UP;
		else {
			logger.error("Tried to get moves to a non adjascent node! Position from: " + from.toString()
					+ ", Position to: " + to.toString());
			throw new IllegalArgumentException("Positions must be adjascent!");
		}

		int repeat = fma.getTerrainAt(from).cost() + fma.getTerrainAt(to).cost();
		Queue<EMove> ret = new LinkedList<>();

		for (int i = 0; i < repeat; ++i)
			ret.add(dir);

		return ret;

	}

	public EMove getNextMove() {
		if (toMove == null || toMove.size() == 0) {
			if (fma.treasureCollected() && !changedToCastleFinding) {
				changedToCastleFinding = true;
				nodeFinder = new NodeFinder(fma, fma.getMyMapHalf().getOppositeHalf(), EGameEntity.ENEMYCASTLE);
				logger.debug("changed to castle finding");
			}

			toMove = movesToAdjascentNode(nodeFinder.getNextPosition(), fma);
		}

		return toMove.remove();
	}

}
