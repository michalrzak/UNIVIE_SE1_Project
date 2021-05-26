package player;

import java.util.HashMap;
import java.util.Map;

import exceptions.TooManyPlayersRegistered;
import player.helpers.PlayerInformation;
import player.helpers.ServerUniquePlayerIdentifier;

public class PlayerController {

	final private Map<ServerUniquePlayerIdentifier, PlayerInformation> registeredPlayers = new HashMap<>();

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		if (registeredPlayers.size() >= 2) {
			throw new TooManyPlayersRegistered("There are already 2 players registered in the game.");
		}

		ServerUniquePlayerIdentifier id = new ServerUniquePlayerIdentifier();
		registeredPlayers.put(id, playerInf);
		return id;
	}

}