package gamedata.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class Player extends SUniquePlayerIdentifier implements IPlayerAccesser {

	private final PlayerInformation playerInf;

	private boolean collectedTreasure = false;

	private static Logger logger = LoggerFactory.getLogger(Player.class);

	public Player(String playerID, PlayerInformation playerInf) {
		super(playerID);
		this.playerInf = playerInf;
	}

	public Player(String playerID, String firstName, String lastName, String studentID) {
		this(playerID, new PlayerInformation(firstName, lastName, studentID));
	}

	public Player(SUniquePlayerIdentifier playerID, PlayerInformation playerInf) {
		this(playerID.getPlayerIDAsString(), playerInf);
	}

	public Player(PlayerInformation playerInf) {
		super();
		this.playerInf = playerInf;
	}

	public void collectTreasure() {
		if (collectedTreasure) {
			logger.error("collected treasure even though the treassure has already been collected."
					+ "This should not be possible. Continueing execution and hoping it wont cause any problems!");
		}
		collectedTreasure = true;
	}

	@Override
	public SUniquePlayerIdentifier getPlayerID() {
		return this;
	}

	@Override
	public String getFirstName() {
		return playerInf.getFirtName();
	}

	@Override
	public String getLastName() {
		return playerInf.getLastName();
	}

	@Override
	public String getStudentID() {
		return playerInf.getStudentID();
	}

	@Override
	public boolean getCollectedTreasure() {
		return collectedTreasure;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		SUniquePlayerIdentifier other = (SUniquePlayerIdentifier) obj;
		if (!other.equals(this))
			return false;
		return true;
	}

}
