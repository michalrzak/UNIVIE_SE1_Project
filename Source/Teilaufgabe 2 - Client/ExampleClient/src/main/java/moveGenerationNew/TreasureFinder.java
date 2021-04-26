package moveGenerationNew;

import mapHelpers.EGameEntity;
import mapHelpers.EMapHalf;
import moveGeneration.FullMapAccesser;

public class TreasureFinder extends NodeFinder {

	public TreasureFinder(FullMapAccesser fma) {
		super(fma, getMapHalf(fma));
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
