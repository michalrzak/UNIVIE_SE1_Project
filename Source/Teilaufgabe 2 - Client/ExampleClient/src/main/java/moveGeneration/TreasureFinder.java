package moveGeneration;

import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;

public class TreasureFinder extends NodeFinder {

	public TreasureFinder(FullMapAccesser fma) {
		super(fma, getMapHalf(fma), EGameEntity.MYTREASURE);
	}

	private static EMapHalf getMapHalf(FullMapAccesser fma) {
		EMapHalf ret;
		if (fma.getHeight() == 4) {
			int xsep = fma.getWidth() / 2;

			if (fma.getEntityPosition(EGameEntity.MYCASTLE).getx() < xsep)
				ret = EMapHalf.LONGMAPORIGIN;
			else
				ret = EMapHalf.LONGMAPOPPOSITE;
		} else {
			int ysep = fma.getHeight() / 2;

			if (fma.getEntityPosition(EGameEntity.MYCASTLE).gety() < ysep)
				ret = EMapHalf.SQUAREMAPORIGIN;
			else
				ret = EMapHalf.SQUAREMAPOPPOSITE;
		}
		return ret;
	}

}
