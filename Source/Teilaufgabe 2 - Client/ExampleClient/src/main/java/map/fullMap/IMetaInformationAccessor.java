package map.fullMap;

import map.mapHelpers.EGameEntity;
import map.mapHelpers.EMapHalf;
import map.mapHelpers.Position;

public interface IMetaInformationAccessor {
	public int getWidth();

	public int getHeight();

	public boolean getTreasureCollected();

	public EMapHalf getMyMapHalf();

	public Position getEntityPosition(EGameEntity entityType);
}
