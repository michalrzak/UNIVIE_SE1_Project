package game.map;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import game.map.helpers.EMapType;
import game.map.helpers.ESTerrain;
import game.map.helpers.OwnedGameEntity;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;

public interface IMapAccesser {

	EMapType getMapType();

	Optional<Position> getTreasurePosition(SUniquePlayerIdentifier of);

	Position getCastlePosition(SUniquePlayerIdentifier of);

	Position getPlayerPosition(SUniquePlayerIdentifier of);

	Collection<OwnedGameEntity> getVisisbleEntitites(SUniquePlayerIdentifier of);

	public ESTerrain getTerrainAt(int x, int y);

	public ESTerrain getTerrainAt(Position pos);

	public Map<Position, ESTerrain> getTerrainMap();
}