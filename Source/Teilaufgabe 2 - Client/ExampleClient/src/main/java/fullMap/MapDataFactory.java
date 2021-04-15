package fullMap;

import java.util.HashMap;

import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

// DEPRECATED !!!!
// THIS WILL PROBABLY GET REMOVED

public class MapDataFactory {
	private HashMap<Position, ETerrain> terrain;
	private HashMap<EGameEntity, Position> gameEntityPosition;
	private int width;
	private int height;
	private boolean isHalfMap;

	public MapDataFactory(boolean isHalfMap) {
		this.isHalfMap = isHalfMap;

		terrain = new HashMap<>();
		gameEntityPosition = new HashMap<>();
		width = 0;
		height = 0;
	}

	public MapDataFactory() {
		this(true); // call other constructor with true
	}

	public void addTerrainAt(Position pos, ETerrain terrainType) {
		if (pos == null || terrainType == null)
			throw new IllegalArgumentException("Arguments cannot be null");

		if (pos.getx() > width)
			width = pos.getx();
		if (pos.gety() > height)
			height = pos.gety();

		terrain.put(pos, terrainType);
	}

	public void addGameEntity(EGameEntity entityType, Position pos) {
		if (entityType == null || pos == null)
			throw new IllegalArgumentException("Arguments cannot be null");

		gameEntityPosition.put(entityType, pos);
	}

	public boolean validate() {
		return true; // TODO: needs to be implemented
	}
}
