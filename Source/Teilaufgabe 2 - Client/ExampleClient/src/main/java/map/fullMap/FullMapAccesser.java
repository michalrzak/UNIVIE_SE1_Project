package map.fullMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.PositionOutOfBoundsException;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;

// TODO: think about this:
// THIS CLASS IS USED TO ACCESS DATA CONTAINED IN FULLMAPDATA
// I AM STILL NOT SURE THIS IS A GOOD SOLUTION MAYBE IT SHOULD BE MOVED TO fullMap PACKAGE???
public class FullMapAccesser {

	private FullMapData fmd;

	private static Logger logger = LoggerFactory.getLogger(FullMapAccesser.class);

	public FullMapAccesser(FullMapData fmd) {
		if (fmd == null) {
			logger.error("FullMapAccesser constructor received null FullMapData");
			throw new IllegalArgumentException("fmd was null");
		}

		this.fmd = fmd;
	}

	public ETerrain getTerrainAt(Position pos) {
		if (pos == null) {
			logger.error("FullMapAccesser received null Position");
			throw new IllegalArgumentException("pos was null");
		}

		if (pos.getx() >= fmd.getWidth() || pos.gety() >= fmd.getHeight()) {
			logger.error("FullMapAccesser received Position out of bounds " + pos.toString());
			throw new PositionOutOfBoundsException("Received position out of bounds of map.", pos);
		}

		return fmd.getTerrainCopy().get(pos);
	}

	public Position getEntityPosition(EGameEntity entityType) {
		if (entityType == null) {
			logger.error("FullMapAccesser received null EGameEntity");
			throw new IllegalArgumentException("entityType was null");
		}

		return fmd.getGameEntitiesCopy().get(entityType);
	}

	public int getWidth() {
		return fmd.getWidth();
	}

	public int getHeight() {
		return fmd.getHeight();
	}

	public boolean treasureCollected() {
		return fmd.getTreasureCollected();
	}

	public EMapHalf getMyMapHalf() {
		EMapHalf ret;
		if (getHeight() == 4) {
			int xsep = getWidth() / 2;

			if (getEntityPosition(EGameEntity.MYCASTLE).getx() < xsep)
				ret = EMapHalf.LONGMAPORIGIN;
			else
				ret = EMapHalf.LONGMAPOPPOSITE;
		} else {
			int ysep = getHeight() / 2;

			if (getEntityPosition(EGameEntity.MYCASTLE).gety() < ysep)
				ret = EMapHalf.SQUAREMAPORIGIN;
			else
				ret = EMapHalf.SQUAREMAPOPPOSITE;
		}
		return ret;
	}

}
