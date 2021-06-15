package game.map;

import java.util.Map;

import game.map.helpers.ESTerrain;
import game.map.helpers.Position;

public interface IMapTerrainAccesser {
	public ESTerrain getTerrainAt(int x, int y);

	public ESTerrain getTerrainAt(Position pos);

	public Map<Position, ESTerrain> getTerrainMap();
}
