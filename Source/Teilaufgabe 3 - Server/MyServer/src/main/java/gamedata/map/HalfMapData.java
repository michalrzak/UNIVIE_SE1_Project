package gamedata.map;

import java.util.Map;

import gamedata.map.helpers.EGameEntity;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class HalfMapData {

	final private Map<Position, ETerrain> terrain;
	final private Position castlePosition;
	final private OwnedGameEntity castle;

	// private static Logger logger = LoggerFactory.getLogger(HalfMapData.class);

	public HalfMapData(Map<Position, ETerrain> terrain, Position castlePosition, ServerUniquePlayerIdentifier owner) {
		this.terrain = terrain;
		castle = new OwnedGameEntity(owner, EGameEntity.CASTLE);
		this.castlePosition = castlePosition;
	}

	public Map<Position, ETerrain> getTerrain() {
		return terrain;
	}

	public Position castlePosition() {
		return castlePosition;
	}

	public ServerUniquePlayerIdentifier getOwner() {
		return castle.getOwner();
	}

}
