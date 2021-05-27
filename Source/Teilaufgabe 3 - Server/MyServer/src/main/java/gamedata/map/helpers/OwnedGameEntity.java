package gamedata.map.helpers;

import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class OwnedGameEntity {
	final private ServerUniquePlayerIdentifier owner;
	final private EGameEntity entity;

	public OwnedGameEntity(ServerUniquePlayerIdentifier owner, EGameEntity entity) {
		this.owner = owner;
		this.entity = entity;
	}

	public ServerUniquePlayerIdentifier getOwner() {
		return owner;
	}

	public EGameEntity getEntity() {
		return entity;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof OwnedGameEntity))
			return false;

		OwnedGameEntity ent = (OwnedGameEntity) obj;

		return owner.equals(ent.owner) && entity == ent.entity;
	}

	@Override
	public int hashCode() {
		return entity.hashCode() + owner.hashCode();
	}

}
