package moveGenerations_tests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import helpers.Helper;
import map.fullMap.FullMapAccesser;
import map.fullMap.FullMapData;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;
import moveGeneration.PathFinder;

public class PathFinding_Tests {

	private FullMapAccesser fma;

	@BeforeEach
	public void generateFullMapAccesser() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'm' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);

		fma = new FullMapAccesser(new FullMapData(testMap, new HashMap<>()));
	}

	@Test
	public void PathFinder_StartNodeEqualsEndNode_shouldReturnEmptyQ() {

		// Queue<Position> res = PathFinder.pathTo(new Position(0, 0), new Position(0,
		// 0), new FullMapAccesser(validMap));
		Queue<Position> res = PathFinder.pathTo(new Position(0, 0), new Position(0, 0), fma);

		Assertions.assertEquals(res.size(), 0);
	}

	@Test
	public void PathFinder_StartIs00EndIs01_shouldReturn01() {
		// Queue<Position> res = PathFinder.pathTo(new Position(0, 0), new Position(0,
		// 1), new FullMapAccesser(validMap));
		Queue<Position> res = PathFinder.pathTo(new Position(0, 0), new Position(0, 1), fma);

		Assertions.assertEquals(res.size(), 1);

		Queue<Position> compare = new LinkedList<Position>();
		compare.add(new Position(0, 1));

		Assertions.assertEquals(res, compare);
	}

	@Test
	public void PathFinder_StartIs00EndIs015_shouldReturnStraightLine() {

		// Queue<Position> res = PathFinder.pathTo(new Position(0, 0), new Position(0,
		// 15), new FullMapAccesser(validMap));
		Queue<Position> res = PathFinder.pathTo(new Position(0, 0), new Position(0, 15), fma);

		Assertions.assertEquals(res.size(), 15);

		Queue<Position> compare = new LinkedList<Position>();
		for (int i = 1; i < 16; ++i)
			compare.add(new Position(0, i));

		Assertions.assertEquals(res, compare);
	}

	@Test
	public void PathFinder_StartIs11EndIs02_shouldNotGoOverMountain() {

		// Queue<Position> res = PathFinder.pathTo(new Position(1, 1), new Position(0,
		// 2), new FullMapAccesser(validMap));
		Queue<Position> res = PathFinder.pathTo(new Position(1, 1), new Position(0, 2), fma);

		Assertions.assertEquals(res.size(), 2);

		Queue<Position> compare = new LinkedList<Position>();
		compare.add(new Position(0, 1));
		compare.add(new Position(0, 2));

		Assertions.assertEquals(res, compare);
	}

}
