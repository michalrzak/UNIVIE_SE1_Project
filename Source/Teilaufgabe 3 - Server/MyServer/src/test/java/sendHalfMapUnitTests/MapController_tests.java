package sendHalfMapUnitTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import exceptions.GenericExampleException;
import gamedata.map.MapController;
import gamedata.map.SHalfMap;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.SUniquePlayerIdentifier;

class MapController_tests {

	Map<Position, ETerrain> createMockedMap() {
		Map<Position, ETerrain> map = Mockito.mock(Map.class);
		Mockito.when(map.get(Mockito.any())).thenReturn(ETerrain.GRASS);
		return map;
	}

	private SHalfMap getMockedHalfMap() {
		SHalfMap hmd = Mockito.mock(SHalfMap.class);

		var map = createMockedMap();
		Mockito.when(hmd.getTerrain()).thenReturn(map);

		// Mockito.when(hmd.getOwner()).thenReturn(new
		// ServerUniquePlayerIdentifier("totally-legit-playerID"));
		Mockito.when(hmd.getCastlePosition()).thenReturn(new Position(0, 0));
		Mockito.when(hmd.getOwner()).thenReturn(SUniquePlayerIdentifier.getRandomID());

		return hmd;
	}

	@Test
	void MapController_ReceiveHalfMapData_ShouldNotThrow() {
		final MapController mc = new MapController();

		final SHalfMap hmd1 = getMockedHalfMap();

		Executable receiveHalfMap = () -> {
			mc.receiveHalfMap(hmd1);
		};

		assertDoesNotThrow(receiveHalfMap);
	}

	@Test
	void MapController_AccesFullMap_ShouldNotThrow() {
		final MapController mc = new MapController();

		final SHalfMap hmd1 = getMockedHalfMap();
		final SHalfMap hmd2 = getMockedHalfMap();

		Executable receiveHalfMap = () -> {
			mc.receiveHalfMap(hmd1);
			mc.receiveHalfMap(hmd2);
			mc.getFullMap();
		};

		assertDoesNotThrow(receiveHalfMap);
	}

	@Test
	void MapController_Receive3HalfMapData_ShouldThrow() {
		final MapController mc = new MapController();

		final SHalfMap hmd1 = getMockedHalfMap();
		final SHalfMap hmd2 = getMockedHalfMap();
		final SHalfMap hmd3 = getMockedHalfMap();

		Executable receiveHalfMap = () -> {
			mc.receiveHalfMap(hmd1);
			mc.receiveHalfMap(hmd2);
			mc.receiveHalfMap(hmd3);
		};

		assertThrows(GenericExampleException.class, receiveHalfMap);
	}

}
