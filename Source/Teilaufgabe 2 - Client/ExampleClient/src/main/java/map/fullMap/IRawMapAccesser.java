package map.fullMap;

import java.util.Map;

import map.mapHelpers.EGameEntity;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public interface IRawMapAccesser {

	public Map<Position, ETerrain> getTerrainCopy();

	public Map<EGameEntity, Position> getGameEntitiesCopy();
}
