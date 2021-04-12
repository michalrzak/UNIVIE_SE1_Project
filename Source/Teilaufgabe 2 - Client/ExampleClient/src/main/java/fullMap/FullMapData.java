package fullMap;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import halfMap.HalfMapData;
import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class FullMapData {
	private HashMap<Position, ETerrain> terrain;
	protected HashMap<EGameEntity, Position> gameEntityPosition;
	protected int width;
	protected int height;
	protected boolean treasureCollected = false;

	private static Logger logger = LoggerFactory.getLogger(HalfMapData.class);

	// TODO: Property change support and the requiered methods

	// I assume the map data (terrain and gameEntityPosition) passed here is valid
	// as it comes from the server. Hence I dont conduct any validation
	public FullMapData(HashMap<Position, ETerrain> terrain, HashMap<EGameEntity, Position> gameEntityPosition) {

		if (terrain == null || gameEntityPosition == null)
			throw new IllegalArgumentException("Parameters cannot be null!");

		// maybe clone all attributes?
		this.terrain = terrain;
		this.gameEntityPosition = gameEntityPosition;

		// extract width and height from terrain
		// stream is probably slower as I know the map is a rectangle and can use this
		// to limit the loops
		int width;
		for (width = 0; terrain.containsKey(new Position(0, width)); ++width)
			;
		this.width = width;

		int height;
		for (height = 0; terrain.containsKey(new Position(height, 0)); ++height)
			;
		this.height = height;
	}

}
