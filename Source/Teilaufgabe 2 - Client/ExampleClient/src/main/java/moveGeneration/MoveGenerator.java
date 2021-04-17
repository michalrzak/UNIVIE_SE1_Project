package moveGeneration;

import java.util.Queue;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.ETerrain;
import mapHelpers.Position;
import moveHelpers.EMove;

public class MoveGenerator {

	private PathGenerator pg;
	private FullMapAccesser fma;

	private Queue<EMove> todo;

	private static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);

	public MoveGenerator(FullMapAccesser fma) {
		this.fma = fma;
		pg = new PathGenerator(fma);
	}

	public EMove getMove() {
		// TODO: make this functional
		// temporary

		if (todo == null || todo.size() == 0) {

			Random rnd = new Random();

			Position rndPos;
			do {
				rndPos = new Position(rnd.nextInt(fma.getWidth()), rnd.nextInt(fma.getHeight()));
			} while (fma.getTerrainAt(rndPos) == ETerrain.WATER);

			todo = pg.getPathTo(rndPos);

			for (var ele : todo)
				System.out.println(ele);
			System.out.println("---------------");

		}

		logger.debug("Sending move: " + todo.peek().toString() + "TODO size: " + todo.size());

		return todo.remove();
	}

}
