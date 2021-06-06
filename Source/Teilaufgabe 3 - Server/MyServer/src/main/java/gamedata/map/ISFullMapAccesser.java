package gamedata.map;

import java.util.Map;
import java.util.Optional;

import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public interface ISFullMapAccesser {

	Map<Position, ETerrain> getTerrain();

	Optional<Position> getTreasurePosition(SUniquePlayerIdentifier playerID);

	Position getCastlePosition(SUniquePlayerIdentifier playerID);

	Position getPlayerPosition(SUniquePlayerIdentifier playerID);

}