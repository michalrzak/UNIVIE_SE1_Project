package moveGenerations_tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import helpers.Helper;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;
import moveGeneration.ShortestPathExtractor;

public class ShortestPathExtractor_Tests {
	private Map<Position, ETerrain> testMap;

	public ShortestPathExtractor_Tests() {
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
	public void ShortestPathExtractor_SendValidPositions_ShouldReturnAllSentNodes() {

		Set<Position> toVisit = new HashSet<>();

		toVisit.add(new Position(0, 1));
		toVisit.add(new Position(3, 1));
		toVisit.add(new Position(2, 4));
		toVisit.add(new Position(1, 1));
		toVisit.add(new Position(10, 2));

		ShortestPathExtractor spe = new ShortestPathExtractor();

		Queue<Position> inOrder = spe.visitInOrder(toVisit, new Position(0, 0));

		// remove any potential duplicates
		Set<Position> inOrderNoDup = inOrder.stream().collect(Collectors.toSet());

		Assertions.assertEquals(inOrderNoDup.size(), toVisit.size());
		Assertions.assertEquals(inOrderNoDup.stream().allMatch(ele -> toVisit.contains(ele)), true);
	}

}
