package moveGenerations_tests;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import helpers.Helper;
import map.fullMap.FullMapAccesser;
import map.fullMap.FullMapData;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;
import moveGeneration.MoveGenerator;

public class MoveGeneration_Tests {

	private FullMapAccesser validMap;

	@BeforeEach
	public void getMapAccesser() {
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
		validMap = new FullMapAccesser(new FullMapData(testMap, entities));
	}

	@Test
	@Disabled
	public void MoveGeneration_From_OutOfBoundsOfMap() {
		// TODO: Finnish this!!
		MoveGenerator mg = new MoveGenerator(validMap);

		mg.getNextMove();

	}

}
