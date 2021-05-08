package moveGenerations_tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import map.fullMap.FullMapAccesser;
import map.helpers.EGameEntity;
import map.helpers.EMapHalf;
import map.helpers.ETerrain;
import map.helpers.Position;
import move.generation.NodeFinder;

public class NodeFinder_Tests {

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
