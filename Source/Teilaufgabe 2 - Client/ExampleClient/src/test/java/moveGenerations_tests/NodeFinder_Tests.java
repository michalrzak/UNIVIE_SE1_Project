package moveGenerations_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import helpers.Helper;
import map.fullMap.FullMapAccesser;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;
import moveGeneration.NodeFinder;

public class NodeFinder_Tests {
	private Map<Position, ETerrain> testMap;

	public NodeFinder_Tests() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);
		/*
		 * Map<EGameEntity, Position> entities = new HashMap<>();
		 * entities.put(EGameEntity.MYPLAYER, new Position(0, 0));
		 * entities.put(EGameEntity.MYCASTLE, new Position(0, 5));
		 * entities.put(EGameEntity.MYTREASURE, new Position(0, 10));
		 * entities.put(EGameEntity.ENEMYCASTLE, new Position(0, 15)); validMap = new
		 * FullMapData(testMap, entities);
		 */
	}

	@Test
	public void NodeFinder_CastleNextToMe_shouldGoThere() {
		FullMapAccesser mocked = Mockito.mock(FullMapAccesser.class);

		Mockito.when(mocked.getHeight()).thenReturn(4);
		Mockito.when(mocked.getEntityPosition(eq(EGameEntity.MYCASTLE))).thenReturn(new Position(15, 0));
		Mockito.when(mocked.getEntityPosition(eq(EGameEntity.ENEMYCASTLE))).thenReturn(new Position(0, 1));
		Mockito.when(mocked.getEntityPosition(EGameEntity.MYPLAYER)).thenReturn(new Position(0, 0));
		Mockito.when(mocked.getTerrainAt(any())).thenReturn(ETerrain.GRASS);

		NodeFinder cf = new NodeFinder(mocked, EMapHalf.LONGMAPOPPOSITE, EGameEntity.ENEMYCASTLE);

		Assertions.assertEquals(cf.getNextPosition(), new Position(0, 1));
	}

}
