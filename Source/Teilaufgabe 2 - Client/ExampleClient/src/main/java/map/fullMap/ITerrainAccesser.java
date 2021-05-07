package map.fullMap;

import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

public interface ITerrainAccesser {
	public ETerrain getTerrainAt(Position pos);
}
