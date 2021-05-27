package gamedata.map;

import java.util.HashMap;
import java.util.Map;

import gamedata.map.helpers.EGameEntity;
import gamedata.map.helpers.EMapType;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.map.helpers.Position;

public class FullMapData {

	final private Map<Position, ETerrain> terrain = new HashMap<>();
	final private Map<OwnedGameEntity, Position> entities = new HashMap<>();

	public FullMapData(HalfMapData hmdataPlayer1, HalfMapData hmdataPlayer2) {
		// TODO: choose to combine halfMaps
		// TODO: pick on random I guess?

		var player1HMTerrainMap = hmdataPlayer1.getTerrain();
		var player2HMTerrainMap = hmdataPlayer2.getTerrain();

		EMapType mapType = EMapType.getRandomMapType();

		for (int y = 0; y <= mapType.getHalfHeight(); ++y) {
			for (int x = 0; x <= mapType.getHalfWidth(); ++x) {
				Position current = new Position(x, y);
				Position currentOffset = current.addPosition(mapType.getSecondHalfOffset());

				terrain.put(current, player1HMTerrainMap.get(current));
				terrain.put(currentOffset, player2HMTerrainMap.get(current));
			}
		}

		entities.put(new OwnedGameEntity(hmdataPlayer1.getOwner(), EGameEntity.CASTLE), hmdataPlayer1.castlePosition());
		entities.put(new OwnedGameEntity(hmdataPlayer1.getOwner(), EGameEntity.PLAYER), hmdataPlayer1.castlePosition());

		entities.put(new OwnedGameEntity(hmdataPlayer2.getOwner(), EGameEntity.CASTLE), hmdataPlayer2.castlePosition());
		entities.put(new OwnedGameEntity(hmdataPlayer2.getOwner(), EGameEntity.PLAYER), hmdataPlayer2.castlePosition());

		entities.put(new OwnedGameEntity(hmdataPlayer1.getOwner(), EGameEntity.TREASURE),
				Position.getRandomMapPosition(mapType.getHalfWidth(), mapType.getHalfHeight()));
		entities.put(new OwnedGameEntity(hmdataPlayer2.getOwner(), EGameEntity.TREASURE), mapType.getSecondHalfOffset()
				.addPosition(Position.getRandomMapPosition(mapType.getHalfWidth(), mapType.getHalfHeight())));
	}

}
