package gamedata.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gamedata.map.helpers.EGameEntity;
import gamedata.map.helpers.EMapType;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class SFullMap implements ISFullMapAccesser {

	private final Map<Position, ETerrain> terrain = new HashMap<>();
	private final Map<OwnedGameEntity, Position> entities = new HashMap<>();

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

	public SFullMap(SHalfMap hmdataPlayer1, SHalfMap hmdataPlayer2, EMapType mapType,
			Position castlePositionPlayer1, Position castlePositionPlayer2) {

		assert (hmdataPlayer1 != null && hmdataPlayer2 != null && mapType != null);

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

	@Override
	public Map<Position, ETerrain> getTerrain() {
		return terrain;
	}

	@Override
	public Optional<Position> getTreasurePosition(SUniquePlayerIdentifier playerID) {
		OwnedGameEntity treasure = new OwnedGameEntity(playerID, EGameEntity.TREASURE);

		if (!entities.containsKey(treasure)) {
			return Optional.empty();
		}

		return Optional.of(entities.get(treasure));
	}

	@Override
	public Position getCastlePosition(SUniquePlayerIdentifier playerID) {
		OwnedGameEntity castle = new OwnedGameEntity(playerID, EGameEntity.CASTLE);

		return entities.get(castle);
	}

	@Override
	public Position getPlayerPosition(SUniquePlayerIdentifier playerID) {
		OwnedGameEntity player = new OwnedGameEntity(playerID, EGameEntity.PLAYER);

		return entities.get(player);
	}

}
