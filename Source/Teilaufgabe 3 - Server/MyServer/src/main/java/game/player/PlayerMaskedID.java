package game.player;

import game.player.helpers.SUniquePlayerIdentifier;

public class PlayerMaskedID implements IPlayerAccesser {

	private final IPlayerAccesser maskedPlayer;

	public PlayerMaskedID(IPlayerAccesser maskedPlayer) {
		this.maskedPlayer = maskedPlayer;
	}

	@Override
	public SUniquePlayerIdentifier getPlayerID() {
		return SUniquePlayerIdentifier.getRandomID();
	}

	@Override
	public String getFirstName() {
		return maskedPlayer.getFirstName();
	}

	@Override
	public String getLastName() {
		return maskedPlayer.getLastName();
	}

	@Override
	public String getStudentID() {
		return maskedPlayer.getStudentID();
	}

	@Override
	public boolean getCollectedTreasure() {
		return maskedPlayer.getCollectedTreasure();
	}

}
