package gamedata.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import exceptions.PlayerInvalidTurn;
import exceptions.TooManyPlayersRegistered;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class PlayerController {

	final static int MAX_PLAYERS_PER_GAME = 2;

	final private Map<ServerUniquePlayerIdentifier, PlayerInformation> registeredPlayers = new HashMap<>();

	private Optional<ServerUniquePlayerIdentifier> currentTurn = Optional.empty();

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		if (registeredPlayers.size() >= 2) {
			throw new TooManyPlayersRegistered("There are already 2 players registered in the game.");
		}

		ServerUniquePlayerIdentifier id = new ServerUniquePlayerIdentifier();
		registeredPlayers.put(id, playerInf);

		if (registeredPlayers.size() == MAX_PLAYERS_PER_GAME) {
			pickFirstPlayer();
		}

		return id;
	}

	public void checkPlayerTurn(ServerUniquePlayerIdentifier playerID) {
		if (currentTurn.isEmpty()) {
			throw new PlayerInvalidTurn("Not all players are registered in the game!");
		}

		ServerUniquePlayerIdentifier current = currentTurn.get();

		if (!current.equals(playerID)) {
			throw new PlayerInvalidTurn("Not your turn!");
		}
	}

	private void pickFirstPlayer() {
		// as the player IDs are random and the hash map is sorted by them, I just pick
		// the first element from this HashSet and have a random player

		// already returns optional
		currentTurn = registeredPlayers.keySet().stream().findFirst();
	}

}