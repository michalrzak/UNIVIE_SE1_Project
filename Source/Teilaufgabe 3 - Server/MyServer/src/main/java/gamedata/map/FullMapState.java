package gamedata.map;

import java.util.Map;
import java.util.Optional;

import gamedata.map.helpers.EGameEntity;
import gamedata.map.helpers.EMapType;
import gamedata.map.helpers.ETerrain;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.map.helpers.Position;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class FullMapState {
	private final SUniquePlayerIdentifier owner;
	private final SUniquePlayerIdentifier other;
	private final ISFullMapAccesser fullMap;
	private final Optional<Position> otherPlayerRandomPosition;

	private static final int RANDOM_OTHER_PLAYER_POSITION_UNTIL_TURN = 10;

	public FullMapState(SUniquePlayerIdentifier owner, SUniquePlayerIdentifier other, ISFullMapAccesser fullMap,
			int turn) {
		this.owner = owner;
		this.other = other;
		this.fullMap = fullMap;
		if (turn > RANDOM_OTHER_PLAYER_POSITION_UNTIL_TURN) {
			otherPlayerRandomPosition = Optional.empty();
		} else {
			EMapType mapType = fullMap.getMapType();
			otherPlayerRandomPosition = Optional
					.of(Position.getRandomMapPosition(mapType.getWidth(), mapType.getHeight()));
		}
	}

	public Map<Position, ETerrain> getTerrain() {
		return fullMap.getTerrain();
	}

	public Position getOwnerPosition() {
		return fullMap.getPlayerPosition(owner);
	}

	public Position getOtherPosition() {
		if (otherPlayerRandomPosition.isPresent()) {
			return otherPlayerRandomPosition.get();
		}
		return fullMap.getPlayerPosition(other);
	}

	public Position getOwnerCastle() {
		return fullMap.getCastlePosition(owner);
	}

	public Optional<Position> getOwnerTreasure() {
		OwnedGameEntity ownerTreasure = new OwnedGameEntity(owner, EGameEntity.TREASURE);

		if (!fullMap.getVisisbleEntitites(owner).contains(ownerTreasure)) {
			return Optional.empty();
		}

		// already returns Optional as the treassure may be collected and thus not
		// present
		return fullMap.getTreasurePosition(owner);
	}

	public Optional<Position> getOtherCastle() {
		OwnedGameEntity otherCastle = new OwnedGameEntity(other, EGameEntity.CASTLE);

		if (!fullMap.getVisisbleEntitites(owner).contains(otherCastle)) {
			return Optional.empty();
		}

		return Optional.of(fullMap.getCastlePosition(other));
	}
}
