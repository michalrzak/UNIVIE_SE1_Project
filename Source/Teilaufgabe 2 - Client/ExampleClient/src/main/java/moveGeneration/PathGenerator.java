package moveGeneration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public List<EMove> getPathTo(Position pos) {
		// TODO: DIJKSTRA!
		return null;
	}
}
