package server.games;

import java.util.HashSet;
import java.util.Set;

import server.exceptions.TooManyPlayersRegistered;
import server.games.helpers.ServerUniquePlayerIdentifier;

public class PlayerController {

	final private Set<ServerUniquePlayerIdentifier> registeredPlayers = new HashSet<>();

	public void registerPlayer(ServerUniquePlayerIdentifier playerID) {
		if (registeredPlayers.size() >= 2) {
			throw new TooManyPlayersRegistered("There are already 2 players registered in the game.");
		}
		registeredPlayers.add(playerID);
	}

}