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

import game.helpers.EGameConstants;
import game.map.helpers.EGameEntity;
import game.map.helpers.EMapType;
import game.map.helpers.EMoveEvent;
import game.map.helpers.ESTerrain;
import game.map.helpers.OwnedGameEntity;
import game.map.helpers.Position;
import game.move.helpers.ESMove;
import game.player.helpers.SUniquePlayerIdentifier;

public class SFullMap implements IMapAccesser {

	private final Map<Position, ESTerrain> terrain = new HashMap<>();
	private final Map<OwnedGameEntity, Position> entities = new HashMap<>();
	private final Map<SUniquePlayerIdentifier, Collection<OwnedGameEntity>> playerRevealedEntities = new HashMap<>();
	private final EMapType mapType;

	private final static Random rand = new Random();

	private static Logger logger = LoggerFactory.getLogger(SFullMap.class);

	public static SFullMap generateRandomMap(SHalfMap hmdataPlayer1, SHalfMap hmdataPlayer2) {
		// Pick first map on random
		if (rand.nextBoolean()) {
			var temp = hmdataPlayer1;
			hmdataPlayer1 = hmdataPlayer2;
			hmdataPlayer2 = temp;
		}

		var hmMapPlayer1 = hmdataPlayer1.getTerrain();
		Position castlePlayer1 = hmdataPlayer1.getCastlePosition();

		var hmMapPlayer2 = hmdataPlayer2.getTerrain();
		Position castlePlayer2 = hmdataPlayer2.getCastlePosition();

		EMapType mapType = EMapType.getRandomMapType();

		// the treasure is placed quite wrongly here. This needs a major rework
		// maybe generate a number between (1->32)-9?
		Position treasurePositionPlayer1;
		do {
			treasurePositionPlayer1 = Position.getRandomMapPosition(mapType.getHalfWidth(), mapType.getHalfHeight());
		} while (hmMapPlayer1.get(treasurePositionPlayer1) != ESTerrain.GRASS || Position.distance(castlePlayer1,
				treasurePositionPlayer1) < EGameConstants.RADIUS_WITHOUT_TREASURE.getValue());
		// repeat until the treasure is on grass and further than 1 tile away from the
		// castle

		Position treasurePositionPlayer2;
		do {
			treasurePositionPlayer2 = Position.getRandomMapPosition(mapType.getHalfWidth(), mapType.getHalfHeight());
		} while (hmMapPlayer2.get(treasurePositionPlayer2) != ESTerrain.GRASS || Position.distance(castlePlayer2,
				treasurePositionPlayer2) < EGameConstants.RADIUS_WITHOUT_TREASURE.getValue());
		// add offset that shifts the map to the second half
		treasurePositionPlayer2 = mapType.getSecondHalfOffset().addPosition(treasurePositionPlayer2);

		return new SFullMap(hmdataPlayer1, hmdataPlayer2, mapType, treasurePositionPlayer1, treasurePositionPlayer2);
	}

	private SFullMap(SHalfMap hmdataPlayer1, SHalfMap hmdataPlayer2, EMapType mapType, Position treasurePositionPlayer1,
			Position treasurePositionPlayer2) {

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

		assert (terrain.get(treasurePositionPlayer1) == ESTerrain.GRASS);
		assert (terrain.get(treasurePositionPlayer2) == ESTerrain.GRASS);

		entities.put(new OwnedGameEntity(p1Owner, EGameEntity.TREASURE), treasurePositionPlayer1);
		entities.put(new OwnedGameEntity(p2Owner, EGameEntity.TREASURE), treasurePositionPlayer2);

		// save entities that are visible by default
		playerRevealedEntities.put(p1Owner, getDefaultVisbleEntities(p1Owner, p2Owner));
		playerRevealedEntities.put(p2Owner, getDefaultVisbleEntities(p2Owner, p1Owner));

		assert (terrain.size() == mapType.getHeight() * mapType.getWidth());
	}

	public EMoveEvent movePlayer(SUniquePlayerIdentifier playerID, ESMove move) {
		final OwnedGameEntity player = new OwnedGameEntity(playerID, EGameEntity.PLAYER);
		final OwnedGameEntity treassure = new OwnedGameEntity(playerID, EGameEntity.TREASURE);
		final OwnedGameEntity enemyCastle = getEnemyCastleEntity(playerID);
		assert (!enemyCastle.getOwner().equals(playerID));

		EMoveEvent ret = EMoveEvent.NOTHING;

		assert (entities.containsKey(player));

		final Position newPos = entities.get(player).addOffset(move.getXDiff(), move.getYDiff());

		if (entities.containsKey(treassure) && entities.get(treassure).equals(newPos)) {
			collectTreasure(playerID);
			ret = EMoveEvent.COLLECTED_TREASURE;
		}

		assert (entities.containsKey(enemyCastle));
		if (entities.get(enemyCastle).equals(newPos)) {
			assert (ret == EMoveEvent.NOTHING);
			steppedOnEnemyCastle(playerID);
			ret = EMoveEvent.STEPPED_ON_CASTLE;
		}

		entities.put(player, newPos);

		return ret;

	}

	private void collectTreasure(SUniquePlayerIdentifier playerID) {
		OwnedGameEntity treasure = new OwnedGameEntity(playerID, EGameEntity.TREASURE);
		if (!entities.containsKey(treasure)) {
			logger.error("collecting treassure, even though it has already been collected");
			return;
		}

		entities.remove(treasure);

		logger.debug("A player collected the treassure! Nice!");
	}

	private void steppedOnEnemyCastle(SUniquePlayerIdentifier playerID) {
		assert (playerRevealedEntities.containsKey(playerID));
		playerRevealedEntities.get(playerID).add(getEnemyCastleEntity(playerID));
		logger.debug("A player stepped on the enemy castle");
	}

	private OwnedGameEntity getEnemyCastleEntity(SUniquePlayerIdentifier me) {
		Optional<OwnedGameEntity> ret = entities.entrySet().stream()
				.filter(entry -> !entry.getKey().getOwner().equals(me))
				.filter(entry -> entry.getKey().getEntity() == EGameEntity.CASTLE).map(entry -> entry.getKey())
				.findFirst();
		// castle must be always present
		assert (ret.isPresent());
		return ret.get();
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

	@Override
	public ESTerrain getTerrainAt(Position pos) {
		return terrain.get(pos);
	}

	@Override
	public ESTerrain getTerrainAt(int x, int y) {
		return getTerrainAt(new Position(x, y));
	}

	@Override
	public Map<Position, ESTerrain> getTerrainMap() {
		return new HashMap<Position, ESTerrain>(terrain);
	}

}
