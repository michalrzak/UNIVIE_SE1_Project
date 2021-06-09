package gamedata.map;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import gamedata.map.helpers.EMapType;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public interface ISFullMapAccesser {

	Map<Position, ETerrain> getTerrain();

	EMapType getMapType();

	Optional<Position> getTreasurePosition(SUniquePlayerIdentifier of);

	Position getCastlePosition(SUniquePlayerIdentifier of);

	Position getPlayerPosition(SUniquePlayerIdentifier of);

	Collection<OwnedGameEntity> getVisisbleEntitites(SUniquePlayerIdentifier of);
}