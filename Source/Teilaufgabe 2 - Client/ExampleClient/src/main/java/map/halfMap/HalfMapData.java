package map.halfMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.InvalidHalfMapGeneratedException;
import exceptions.PositionOutOfBoundsException;
import map.mapHelpers.EMapDimensions;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public class HalfMapData {

	private final Map<Position, ETerrain> terrain;
	private final Position myCastlePosition;

	private static Logger logger = LoggerFactory.getLogger(HalfMapData.class);

	public HalfMapData(Map<Position, ETerrain> terrain, Position myCastlePosition)
			throws InvalidHalfMapGeneratedException {
		if (terrain == null || myCastlePosition == null) {
			logger.error("HalfMapData constructor received a null parameter");
			throw new IllegalArgumentException("Arguments cannot be null!");
		}

		if (myCastlePosition.getx() >= EMapDimensions.HALFMAP.width()
				|| myCastlePosition.gety() >= EMapDimensions.HALFMAP.height()) {
			logger.error(
					"HalfMapData constructor received a myFortPosition object with position outside of halfmap; Received: "
							+ myCastlePosition);
			throw new PositionOutOfBoundsException("myFortPosition x must be in range [0; 8) and y in range [0; 4)",
					myCastlePosition);
		}

		HalfMapValidator hmv = new HalfMapValidator(terrain);
		if (!hmv.validateTerrain()) {
			logger.error("HalfMapData constructor received an invalid Terrain map!");
			throw new InvalidHalfMapGeneratedException();
		}
		if (!hmv.validateCastlePosition(myCastlePosition)) {
			logger.error("Castle is not placed on a grass field!");
			throw new RuntimeException("Castle is not placed on a grass field");
		}

		this.terrain = terrain;
		this.myCastlePosition = myCastlePosition;
	}

	// TODO: is this a good idea?
	public Stream<Entry<Position, ETerrain>> getStream() {
		return terrain.entrySet().stream();
	}

	public Position castlePosition() {
		return myCastlePosition;
	}

}
