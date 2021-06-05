package gamedata.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class Player extends PlayerInformation {

	private final ServerUniquePlayerIdentifier playerID;

	private boolean collectedTreasure = false;

	private static Logger logger = LoggerFactory.getLogger(Player.class);

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

	public void collectTreasure() {
		if (collectedTreasure) {
			logger.error("collected treasure even though the treassure has already been collected."
					+ "This should not be possible. Continueing execution and hoping it wont cause any problems!");
		}
		collectedTreasure = true;
	}

	public boolean getCollectedTreasure() {
		return collectedTreasure;
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
