package fullMap;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class FullMapData {
	private Map<Position, ETerrain> terrain;
	private Map<EGameEntity, Position> gameEntityPosition;
	private int width;
	private int height;
	private boolean treasureCollected = false;

	private int counter = 0;

	private static Logger logger = LoggerFactory.getLogger(FullMapData.class);

	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	// I assume the map data (terrain and gameEntityPosition) passed here is valid
	// as it comes from the server. Hence I dont conduct any validation
	public FullMapData(Map<Position, ETerrain> terrain, Map<EGameEntity, Position> gameEntityPosition) {

		if (terrain == null || gameEntityPosition == null) {
			logger.error("Parameters cannot be null!");
			throw new IllegalArgumentException("Parameters cannot be null!");
		}

		// maybe clone all attributes?
		this.terrain = terrain;
		this.gameEntityPosition = gameEntityPosition;

		// extract width and height from terrain
		// stream is probably slower as I know the map is a rectangle and can use this
		// to limit the loops
		int i = 0;
		while (terrain.containsKey(new Position(i, 0)))
			++i;
		this.width = i;
		logger.debug("FullMap width is: " + i);

		i = 0;
		while (terrain.containsKey(new Position(0, i)))
			++i;
		this.height = i;
		logger.debug("FullMap height is: " + i);

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
	public Map<Position, ETerrain> getTerrain() {
		// maybe return a copy? or just a stream?
		return terrain;
	}

	public Map<EGameEntity, Position> getGameEntities() {
		return gameEntityPosition;
	}

	public void addListener(PropertyChangeListener view) {
		changes.addPropertyChangeListener(view);
	}

}
