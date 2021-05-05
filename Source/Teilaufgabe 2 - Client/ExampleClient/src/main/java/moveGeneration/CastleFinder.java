package moveGeneration;

import map.fullMap.FullMapAccesser;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;

public class CastleFinder extends ANodeFinder {

	public CastleFinder(FullMapAccesser fma) {
		super(fma, getMapHalf(fma), EGameEntity.ENEMYCASTLE);
	}

	private static EMapHalf getMapHalf(FullMapAccesser fma) {
		EMapHalf ret;
		if (fma.getHeight() == 4) {
			int xsep = fma.getWidth() / 2;

			if (fma.getEntityPosition(EGameEntity.MYCASTLE).getx() < xsep)
				ret = EMapHalf.LONGMAPOPPOSITE;
			else
				ret = EMapHalf.LONGMAPORIGIN;
		} else {
			int ysep = fma.getHeight() / 2;

			if (fma.getEntityPosition(EGameEntity.MYCASTLE).gety() < ysep)
				ret = EMapHalf.SQUAREMAPOPPOSITE;
			else
				ret = EMapHalf.SQUAREMAPORIGIN;
		}
		return ret;
	}

}
