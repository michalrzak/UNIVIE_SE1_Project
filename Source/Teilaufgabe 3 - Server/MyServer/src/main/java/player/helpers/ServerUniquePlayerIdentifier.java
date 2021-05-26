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
}
