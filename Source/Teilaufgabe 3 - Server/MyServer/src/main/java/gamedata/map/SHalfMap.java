package gamedata.map;

import java.util.Map;

import gamedata.map.helpers.EGameEntity;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class SHalfMap {

	private final Map<Position, ETerrain> terrain;
	private final Position castlePosition;
	private final OwnedGameEntity castle;

	// private static Logger logger = LoggerFactory.getLogger(HalfMapData.class);

	public SHalfMap(Map<Position, ETerrain> terrain, Position castlePosition, SUniquePlayerIdentifier owner) {
		// TODO: magic number
		assert (terrain.size() == 32);

		this.terrain = terrain;
		castle = new OwnedGameEntity(owner, EGameEntity.CASTLE);
		this.castlePosition = castlePosition;
	}

	public Map<Position, ETerrain> getTerrain() {
		return terrain;
	}

	public Position getCastlePosition() {
		return castlePosition;
	}

	public SUniquePlayerIdentifier getOwner() {
		return castle.getOwner();
	}

}
