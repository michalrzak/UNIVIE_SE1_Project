package gamedata.player.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.TooManyPlayersRegistered;
import gamedata.EGameConstants;
import gamedata.player.Player;

public class PlayerStorage {

	final private Map<ServerUniquePlayerIdentifier, PlayerInformation> registeredPlayers = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(PlayerStorage.class);

	public void put(ServerUniquePlayerIdentifier playerID, PlayerInformation playerInf) {
		if (registeredPlayers.size() >= EGameConstants.MAX_PLAYER_COUNT.getValue()) {
			logger.warn("Tried to append a player even though the game has already max players registered");
			throw new TooManyPlayersRegistered("Tried to save a player but max players were already registered!");
		}

		if (registeredPlayers.containsKey(playerID)) {
			logger.warn("Tried to append the same playerID twice!");
			throw new TooManyPlayersRegistered("Tried to save a player with an ID that is already saved! ID was: "
					+ playerID.getPlayerIDAsString());
		}

		registeredPlayers.put(playerID, playerInf);
	}

	public void put(Player p) {
		put(p.getPlayerID(), p);
	}

	public Player get(ServerUniquePlayerIdentifier playerID) {
		return new Player(registeredPlayers.get(playerID), playerID);
	}

	public int size() {
		return registeredPlayers.size();
	}

	public List<ServerUniquePlayerIdentifier> getRegisteredIDs() {
		return registeredPlayers.keySet().stream().collect(Collectors.toList());
	}

}
