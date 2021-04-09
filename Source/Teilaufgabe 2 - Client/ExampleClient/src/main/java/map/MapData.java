package map;

import java.util.HashMap;

import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class MapData {
	protected HashMap<Position, ETerrain> terrain;
	protected HashMap<EGameEntity, Position> gameEntityPosition;
	protected int width;
	protected int height;
	protected boolean treasureCollected = false;

	// TODO: Property change support and the requiered methods

	// For now this will hold both HalfMaps and Full maps. This is maybe not so
	// ideal. I am not sure how to solve it yet, one idea would be to make this
	// abstract and derive two classes from this. That would however force me to
	// duplicate the Factory as well.

	// change to protected when Factory class is available
	protected MapData(HashMap<Position, ETerrain> terrain, HashMap<EGameEntity, Position> gameEntityPosition, int width,
			int height) {

		if (terrain == null || gameEntityPosition == null)
			throw new IllegalArgumentException("Parameters cannot be null!");

		if (width < 0 || height < 0)
			throw new IllegalArgumentException("Width & Height cannot be negative");

		// maybe clone all attributes?
		this.terrain = terrain;
		this.gameEntityPosition = gameEntityPosition;
		this.width = width;
		this.height = height;
	}

}
