package map.fullMap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.mapHelpers.EGameEntity;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public class FullMapData {
	private final Map<Position, ETerrain> terrain;
	private final int width;
	private final int height;

	private Map<EGameEntity, Position> gameEntityPosition;
	private boolean treasureCollected = false;

	private static Logger logger = LoggerFactory.getLogger(FullMapData.class);

	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	// I assume the map data (terrain and gameEntityPosition) passed here is valid
	// as it comes from the server. Hence I dont conduct any validation
	public FullMapData(Map<Position, ETerrain> terrain, Map<EGameEntity, Position> gameEntityPosition) {

		if (terrain == null || gameEntityPosition == null) {
			logger.error("Parameters cannot be null!");
			throw new IllegalArgumentException("Parameters cannot be null!");
		}

		this.terrain = terrain;
		this.gameEntityPosition = gameEntityPosition;

		this.width = extractWidth(terrain);
		logger.debug("FullMap width is: " + this.width);

		this.height = extractHeight(terrain);
		logger.debug("FullMap height is: " + this.height);

	}

	private static int extractWidth(Map<Position, ETerrain> terrain) {
		int i = 0;
		while (terrain.containsKey(new Position(i, 0)))
			++i;
		return i;
	}

	private static int extractHeight(Map<Position, ETerrain> terrain) {
		int i = 0;
		while (terrain.containsKey(new Position(0, i)))
			++i;
		return i;
	}

	// maybe it is a good idea to pass entities one by one?
	public void updateEntities(Map<EGameEntity, Position> gameEntities) {
		if (gameEntities == null) {
			logger.error("Tried passing gameEntities == null");
			throw new IllegalArgumentException("Arguments cannot be null");
		}

		if (gameEntities.get(EGameEntity.MYPLAYER) == null) {
			logger.error(
					"gameEntities does not contain my position. This information has to be contained at all times!");
			throw new IllegalArgumentException("gameEntities must contain EGameEntity.MYPLAYER");
		}

		Map<EGameEntity, Position> old = gameEntityPosition;

		gameEntityPosition = gameEntities;

		if (old.containsKey(EGameEntity.MYTREASURE) && !gameEntityPosition.containsKey(EGameEntity.MYTREASURE))
			collectTreasure();

		changes.firePropertyChange("gameEntities", old, gameEntityPosition);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean getTreasureCollected() {
		return treasureCollected;
	}

	public void collectTreasure() {
		if (treasureCollected)
			logger.warn("Treassure was already collected yet calling collectTreasure again!");

		Boolean treasureOld = treasureCollected;

		logger.debug("Treasure collected!");
		treasureCollected = true;

		changes.firePropertyChange("treasureCollected", treasureOld, (Boolean) treasureCollected);
	}

	// this is dangerous as the returned terrain can be modified which modifies the
	// saved MapData as well
	public Map<Position, ETerrain> getTerrainCopy() {
		// maybe return a copy? or just a stream?
		return new HashMap<>(terrain);
	}

	public Map<EGameEntity, Position> getGameEntitiesCopy() {
		return gameEntityPosition;
	}

	public void addListener(PropertyChangeListener view) {
		changes.addPropertyChangeListener(view);
	}

}
