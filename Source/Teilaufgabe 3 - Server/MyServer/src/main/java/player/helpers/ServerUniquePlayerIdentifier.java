package player.helpers;

import java.util.UUID;

public class ServerUniquePlayerIdentifier {
	final private String playerID = UUID.randomUUID().toString();

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
