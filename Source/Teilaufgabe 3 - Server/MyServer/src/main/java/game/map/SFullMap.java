package game.map;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.map.helpers.EGameEntity;
import game.map.helpers.EMapType;
import game.map.helpers.ESTerrain;
import game.map.helpers.OwnedGameEntity;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;

public class SFullMap implements ISFullMapAccesser {

	private final Map<Position, ESTerrain> terrain = new HashMap<>();
	private final Map<OwnedGameEntity, Position> entities = new HashMap<>();
	private final Map<SUniquePlayerIdentifier, Collection<OwnedGameEntity>> playerRevealedEntities = new HashMap<>();
	private final EMapType mapType;

	private static Logger logger = LoggerFactory.getLogger(SFullMap.class);

	public static SFullMap generateRandomMap(SHalfMap hmdataPlayer1, SHalfMap hmdataPlayer2) {
		// Pick first map on random
		Random rand = new Random();
		if (rand.nextBoolean()) {
			var temp = hmdataPlayer1;
			hmdataPlayer1 = hmdataPlayer2;
			hmdataPlayer2 = temp;
		}

		EMapType mapType = EMapType.getRandomMapType();

		Position castlePositionPlayer1 = Position.getRandomMapPosition(mapType.getHalfWidth(), mapType.getHalfHeight());
		Position castlePositionPlayer2 = mapType.getSecondHalfOffset()
				.addPosition(Position.getRandomMapPosition(mapType.getHalfWidth(), mapType.getHalfHeight()));

		return new SFullMap(hmdataPlayer1, hmdataPlayer2, mapType, castlePositionPlayer1, castlePositionPlayer2);
	}

	public SFullMap(SHalfMap hmdataPlayer1, SHalfMap hmdataPlayer2, EMapType mapType, Position castlePositionPlayer1,
			Position castlePositionPlayer2) {

		assert (hmdataPlayer1 != null && hmdataPlayer2 != null && mapType != null);

		this.mapType = mapType;

		var player1HMTerrainMap = hmdataPlayer1.getTerrain();
		var player2HMTerrainMap = hmdataPlayer2.getTerrain();

		assert (player1HMTerrainMap.size() == mapType.getHalfHeight() * mapType.getHalfWidth());
		assert (player2HMTerrainMap.size() == mapType.getHalfHeight() * mapType.getHalfWidth());

		// extract terrain from halfmaps
		for (int y = 0; y < mapType.getHalfHeight(); ++y) {
			for (int x = 0; x < mapType.getHalfWidth(); ++x) {
				Position current = new Position(x, y);
				Position currentOffset = current.addPosition(mapType.getSecondHalfOffset());

				terrain.put(current, player1HMTerrainMap.get(current));
				terrain.put(currentOffset, player2HMTerrainMap.get(current));
			}
		}

		// extract entities from HalfMaps
		Position p1CastlePosition = hmdataPlayer1.getCastlePosition();
		SUniquePlayerIdentifier p1Owner = hmdataPlayer1.getOwner();
		entities.put(new OwnedGameEntity(p1Owner, EGameEntity.CASTLE), p1CastlePosition);
		entities.put(new OwnedGameEntity(p1Owner, EGameEntity.PLAYER), p1CastlePosition);

		Position p2CastlePosition = hmdataPlayer2.getCastlePosition().addPosition(mapType.getSecondHalfOffset());
		SUniquePlayerIdentifier p2Owner = hmdataPlayer2.getOwner();
		entities.put(new OwnedGameEntity(p2Owner, EGameEntity.CASTLE), p2CastlePosition);
		entities.put(new OwnedGameEntity(p2Owner, EGameEntity.PLAYER), p2CastlePosition);

		entities.put(new OwnedGameEntity(p1Owner, EGameEntity.TREASURE), castlePositionPlayer1);
		entities.put(new OwnedGameEntity(p2Owner, EGameEntity.TREASURE), castlePositionPlayer2);

		// save entities that are visible by default
		playerRevealedEntities.put(p1Owner, getDefaultVisbleEntities(p1Owner, p2Owner));
		playerRevealedEntities.put(p2Owner, getDefaultVisbleEntities(p2Owner, p1Owner));

		assert (terrain.size() == mapType.getHeight() * mapType.getWidth());
	}

	public void collectTreasure(SUniquePlayerIdentifier playerID) {
		OwnedGameEntity treasure = new OwnedGameEntity(playerID, EGameEntity.TREASURE);
		if (!entities.containsKey(treasure)) {
			logger.error("collecting treassure, even though it has already been collected");
			return;
		}

		entities.remove(treasure);
	}

	private static Collection<OwnedGameEntity> getDefaultVisbleEntities(SUniquePlayerIdentifier inputingFor,
			SUniquePlayerIdentifier other) {

		Collection<OwnedGameEntity> visible = new HashSet<>();
		visible.add(new OwnedGameEntity(inputingFor, EGameEntity.CASTLE));
		visible.add(new OwnedGameEntity(inputingFor, EGameEntity.PLAYER));
		visible.add(new OwnedGameEntity(other, EGameEntity.PLAYER));

		return visible;
	}

	@Override
	public Map<Position, ESTerrain> getTerrain() {
		return new HashMap<Position, ESTerrain>(terrain);
	}

	@Override
	public Optional<Position> getTreasurePosition(SUniquePlayerIdentifier of) {
		OwnedGameEntity treasure = new OwnedGameEntity(of, EGameEntity.TREASURE);

		if (!entities.containsKey(treasure)) {
			return Optional.empty();
		}

		return Optional.of(entities.get(treasure));
	}

	@Override
	public Position getCastlePosition(SUniquePlayerIdentifier of) {
		OwnedGameEntity castle = new OwnedGameEntity(of, EGameEntity.CASTLE);

		// should be contained as player checks happen in other classes and a castle
		// should be always present
		assert (entities.containsKey(castle));

		return entities.get(castle);
	}

	@Override
	public Position getPlayerPosition(SUniquePlayerIdentifier of) {
		OwnedGameEntity player = new OwnedGameEntity(of, EGameEntity.PLAYER);

		// should be contained as player checks happen in other classes and a player
		// should be always present
		assert (entities.containsKey(player));

		return entities.get(player);
	}

	@Override
	public Collection<OwnedGameEntity> getVisisbleEntitites(SUniquePlayerIdentifier of) {

		// should contain key as this should be checked in game and player already!
		assert (playerRevealedEntities.containsKey(of));

		return Collections.unmodifiableCollection(playerRevealedEntities.get(of));
	}

	@Override
	public EMapType getMapType() {
		return mapType;
	}

}
