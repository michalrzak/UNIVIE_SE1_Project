package gamedata.player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.TooManyPlayersRegistered;
import gamedata.EGameConstants;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.PlayerStorage;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class PlayerController {

	private final PlayerStorage players = new PlayerStorage();
	private final Queue<ServerUniquePlayerIdentifier> playerTurn = new LinkedList<>();

	private static Logger logger = LoggerFactory.getLogger(PlayerStorage.class);

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {

		ServerUniquePlayerIdentifier id = new ServerUniquePlayerIdentifier();
		try {
			players.put(id, playerInf);
		} catch (TooManyPlayersRegistered e) {
			logger.warn("Inserting a new player failed!");
			throw e;
		}

		if (players.size() == EGameConstants.MAX_PLAYER_COUNT.getValue()) {
			logger.debug("Reached max players. Picking player to go first!");
			pickPlayerOrder();
		}

		return id;
	}

	/*
	 * public void checkPlayerTurn(ServerUniquePlayerIdentifier playerID) { if
	 * (playerTurn.isEmpty()) { logger.
	 * warn("Tried to check turn even though game is not ready! (not all players registered)"
	 * ); throw new
	 * PlayerInvalidTurn("Not all players are registered in the game!"); }
	 * 
	 * ServerUniquePlayerIdentifier current = playerTurn.element();
	 * 
	 * if (!current.equals(playerID)) { logger.warn(null); throw new
	 * PlayerInvalidTurn("Not your turn!"); } }
	 */

	public boolean checkPlayerTurn(ServerUniquePlayerIdentifier playerID) {
		if (playerTurn.isEmpty()) {
			logger.warn("Tried checking player turn even though the game is not ready!");
			return false;
		}

		ServerUniquePlayerIdentifier current = playerTurn.element();
		if (!current.equals(playerID)) {
			return false;
		}
		return true;
	}

	public boolean checkPlayer(ServerUniquePlayerIdentifier playerID) {
		return players.contains(playerID);
	}

	public void nextTurn() {
		playerTurn.add(playerTurn.remove());
	}

	private void pickPlayerOrder() {
		List<ServerUniquePlayerIdentifier> registeredIDs = players.getRegisteredIDs();

		// shuffle the registered playerIDs
		Collections.shuffle(registeredIDs);

		playerTurn.addAll(registeredIDs);

	}

}