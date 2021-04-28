package moveGenerations_tests;

import java.util.HashMap;
import java.util.Map;

import helpers.Helper;
import map.fullMap.FullMapData;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;
import moveGeneration.FullMapAccesser;
import moveGeneration.MoveGenerator;

public class MoveGeneration_Tests {

	private final FullMapData validMap;

	public MoveGeneration_Tests() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);

		Map<EGameEntity, Position> entities = new HashMap<>();
		entities.put(EGameEntity.MYPLAYER, new Position(0, 0));
		entities.put(EGameEntity.MYCASTLE, new Position(0, 5));
		entities.put(EGameEntity.MYTREASURE, new Position(0, 10));
		entities.put(EGameEntity.ENEMYCASTLE, new Position(0, 15));
		validMap = new FullMapData(testMap, entities);
	}

	public void MoveGeneration_From_OutOfBoundsOfMap() {
		// TODO: Finnish this!!
		MoveGenerator mg = new MoveGenerator(new FullMapAccesser(validMap));

		mg.getNextMove();

	}

}
