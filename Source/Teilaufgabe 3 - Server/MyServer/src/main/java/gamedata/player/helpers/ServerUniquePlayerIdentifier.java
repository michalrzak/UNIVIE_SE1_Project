package gamedata.player.helpers;

import java.util.UUID;

public class ServerUniquePlayerIdentifier {
	private final String playerID;

	public ServerUniquePlayerIdentifier() {
		this.playerID = UUID.randomUUID().toString();
	}

	public ServerUniquePlayerIdentifier(String playerID) {
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
		if (!(obj instanceof ServerUniquePlayerIdentifier))
			return false;

		ServerUniquePlayerIdentifier arg = (ServerUniquePlayerIdentifier) obj;

		return playerID.equals(arg.playerID);
	}
}
