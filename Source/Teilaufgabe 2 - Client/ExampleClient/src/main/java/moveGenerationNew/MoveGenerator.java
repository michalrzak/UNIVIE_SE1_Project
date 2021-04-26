package moveGenerationNew;

import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.EGameEntity;
import mapHelpers.Position;
import moveGeneration.FullMapAccesser; //carefull here -> from another package !
import moveHelpers.EMove;

public class MoveGenerator {

	private FullMapAccesser fma;
	private TreasureFinder nodeFinder;

	// group together into one class?
	private Queue<EMove> toMove;

	private static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);

	public MoveGenerator(FullMapAccesser fma) {
		nodeFinder = new TreasureFinder(fma);
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
			throw new IllegalArgumentException("Position from out of bounds");
		}

		if (to.getx() >= fma.getWidth() || to.gety() >= fma.getHeight()) {
			logger.error("Position to out of bounds!");
			throw new IllegalArgumentException("Position to out of bounds");
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
		if (toMove == null || toMove.size() == 0)
			toMove = movesToAdjascentNode(nodeFinder.getNextPosition(), fma);

		return toMove.remove();
	}

}
