package gamedata.player;

import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class Player extends PlayerInformation {

	private final ServerUniquePlayerIdentifier playerID;

	public Player(String firstName, String lastName, String studentID, ServerUniquePlayerIdentifier playerID) {
		super(firstName, lastName, studentID);
		this.playerID = playerID;
	}

	public Player(PlayerInformation playerInf, ServerUniquePlayerIdentifier playerID) {
		super(playerInf.getFirtName(), playerInf.getLastName(), playerInf.getStudentID());
		this.playerID = playerID;
	}

	public ServerUniquePlayerIdentifier getPlayerID() {
		return playerID;
	}

	@Override
	public int hashCode() {
		return playerID.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerID == null) {
			if (other.playerID != null)
				return false;
		} else if (!playerID.equals(other.playerID))
			return false;
		return true;
	}

}
