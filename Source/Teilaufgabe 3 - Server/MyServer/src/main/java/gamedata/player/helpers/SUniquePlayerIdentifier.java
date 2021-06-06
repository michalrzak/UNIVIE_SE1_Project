package gamedata.player.helpers;

import java.util.UUID;

public class SUniquePlayerIdentifier {
	private final String playerID;

	public SUniquePlayerIdentifier() {
		this.playerID = UUID.randomUUID().toString();
	}

	public SUniquePlayerIdentifier(String playerID) {
		this.playerID = playerID;
	}

	public String getPlayerIDAsString() {
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
		if (!(obj instanceof SUniquePlayerIdentifier))
			return false;

		SUniquePlayerIdentifier arg = (SUniquePlayerIdentifier) obj;

		return playerID.equals(arg.playerID);
	}
}
