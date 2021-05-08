package moveGenerations_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import map.fullMap.FullMapAccesser;
import map.helpers.EGameEntity;
import map.helpers.EMapHalf;
import map.helpers.ETerrain;
import map.helpers.Position;
import move.generation.MoveGenerator;
import move.helpers.EMove;

public class MoveGenerator_Tests {
	private Map<Position, ETerrain> testMap;

	@Test
	public void MoveGeneration_TreasureNextToMe_shouldGoThere() {
		FullMapAccesser mocked = Mockito.mock(FullMapAccesser.class);

		Mockito.when(mocked.getHeight()).thenReturn(4);
		Mockito.when(mocked.getEntityPosition(EGameEntity.MYPLAYER)).thenReturn(new Position(5, 5));
		Mockito.when(mocked.getEntityPosition(eq(EGameEntity.MYTREASURE))).thenReturn(new Position(5, 6));
		Mockito.when(mocked.getEntityPosition(eq(EGameEntity.MYCASTLE))).thenReturn(new Position(5, 5));
		Mockito.when(mocked.getTerrainAt(any())).thenReturn(ETerrain.GRASS);
		Mockito.when(mocked.getMyMapHalf()).thenReturn(EMapHalf.LONGMAPOPPOSITE);
		Mockito.when(mocked.treasureCollected()).thenReturn(false);
		Mockito.when(mocked.getHeight()).thenReturn(8);
		Mockito.when(mocked.getWidth()).thenReturn(8);

		MoveGenerator mg = new MoveGenerator(mocked);

		Assertions.assertEquals(mg.getNextMove(), EMove.DOWN);
	}

}
