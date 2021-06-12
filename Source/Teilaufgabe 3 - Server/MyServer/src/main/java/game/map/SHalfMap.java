package game.map;

import java.util.Map;

import game.map.helpers.EGameEntity;
import game.map.helpers.EMapType;
import game.map.helpers.ESTerrain;
import game.map.helpers.OwnedGameEntity;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;

public class SHalfMap {

	private final Map<Position, ESTerrain> terrain;
	private final Position castlePosition;
	private final OwnedGameEntity castle;

	// private static Logger logger = LoggerFactory.getLogger(HalfMapData.class);

	public SHalfMap(Map<Position, ESTerrain> terrain, Position castlePosition, SUniquePlayerIdentifier owner) {
		assert (terrain.size() == EMapType.LONGMAP.getHalfHeight() * EMapType.LONGMAP.getHalfWidth());

		this.terrain = terrain;
		castle = new OwnedGameEntity(owner, EGameEntity.CASTLE);
		this.castlePosition = castlePosition;
	}

	public Map<Position, ESTerrain> getTerrain() {
		return terrain;
	}

	public Position getCastlePosition() {
		return castlePosition;
	}

	public SUniquePlayerIdentifier getOwner() {
		return castle.getOwner();
	}

}
