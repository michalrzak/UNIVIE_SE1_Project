package fullmap_tests;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import fullMap.FullMapData;
import helpers.Helper;
import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class FullMap_Tests {

	@Test
	public void FullMap_DimensionsTest_shouldBe8and8() {
		char[][] nodes = { { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);

		FullMapData fm = new FullMapData(testMap, new HashMap<>());

		Assertions.assertEquals(fm.getWidth(), 8);
		Assertions.assertEquals(fm.getHeight(), 8);
	}

	@Test
	public void FullMap_DimensionsTest_shouldBe16and4() {
		char[][] nodes = { { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'g', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g', 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);

		FullMapData fm = new FullMapData(testMap, new HashMap<>());

		Assertions.assertEquals(fm.getWidth(), 16);
		Assertions.assertEquals(fm.getHeight(), 4);
	}

	@Test
	public void FullMap_DimensionsTest_shouldBe4and16() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'm', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'm', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);

		FullMapData fm = new FullMapData(testMap, new HashMap<>());

		Assertions.assertEquals(fm.getWidth(), 4);
		Assertions.assertEquals(fm.getHeight(), 16);
	}

	@Test
	public void FullMap_updateEntitiesWithoutMyPlayer_shouldThrowError() {
		char[][] nodes = { { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'm', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'g', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'g', 'g' },
				{ 'g', 'g', 'm', 'm' }, { 'm', 'w', 'w', 'g' }, { 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g' },
				{ 'g', 'g', 'm', 'm' } };
		HashMap<Position, ETerrain> testMap = Helper.arrayToMap(nodes);

		FullMapData fm = new FullMapData(testMap, new HashMap<>());

		Executable mapUpdate = () -> {
			HashMap<EGameEntity, Position> ent = new HashMap<>();
			ent.put(EGameEntity.MYTREASURE, new Position(0, 0));
			fm.updateEntities(ent);
		};

		Assertions.assertThrows(IllegalArgumentException.class, mapUpdate);
	}
}
