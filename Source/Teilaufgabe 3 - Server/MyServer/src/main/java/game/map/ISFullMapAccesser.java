package game.map;

import java.util.Collection;
import java.util.Optional;

import game.map.helpers.EMapType;
import game.map.helpers.OwnedGameEntity;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;

public interface ISFullMapAccesser extends IMapTerrainAccesser {

	EMapType getMapType();

	Optional<Position> getTreasurePosition(SUniquePlayerIdentifier of);

	Position getCastlePosition(SUniquePlayerIdentifier of);

	Position getPlayerPosition(SUniquePlayerIdentifier of);

	Collection<OwnedGameEntity> getVisisbleEntitites(SUniquePlayerIdentifier of);
}