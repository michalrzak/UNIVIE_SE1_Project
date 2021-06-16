package game.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import game.map.helpers.EGameEntity;
import game.map.helpers.EMapType;
import game.map.helpers.ESTerrain;
import game.map.helpers.OwnedGameEntity;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;

public class SHalfMap implements IMapAccesser {

	private final Map<Position, ESTerrain> terrain;
	private final Position castlePosition;
	private final OwnedGameEntity castle;

	// private static Logger logger = LoggerFactory.getLogger(SHalfMap.class);

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

	@Override
	public EMapType getMapType() {
		return EMapType.HALFMAP;
	}

	@Override
	public Optional<Position> getTreasurePosition(SUniquePlayerIdentifier of) {
		return Optional.empty();
	}

	@Override
	public Position getCastlePosition(SUniquePlayerIdentifier of) {
		return castlePosition;
	}

	@Override
	public Position getPlayerPosition(SUniquePlayerIdentifier of) {
		return castlePosition;
	}

	@Override
	public Collection<OwnedGameEntity> getVisisbleEntitites(SUniquePlayerIdentifier of) {
		if (!of.equals(castle.getOwner())) {
			return new ArrayList<>();
		}
		return List.of(new OwnedGameEntity(of, EGameEntity.CASTLE), new OwnedGameEntity(of, EGameEntity.PLAYER));
	}

	@Override
	public ESTerrain getTerrainAt(int x, int y) {
		return getTerrainAt(new Position(x, y));
	}

	@Override
	public ESTerrain getTerrainAt(Position pos) {
		return terrain.get(pos);
	}

	@Override
	public Map<Position, ESTerrain> getTerrainMap() {
		return new HashMap<>(terrain);
	}

}
