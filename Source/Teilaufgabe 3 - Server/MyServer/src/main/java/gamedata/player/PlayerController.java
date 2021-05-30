package gamedata.player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import exceptions.PlayerInvalidTurn;
import exceptions.TooManyPlayersRegistered;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class PlayerController {

	final static int MAX_PLAYERS_PER_GAME = 2;

	final private Map<ServerUniquePlayerIdentifier, PlayerInformation> registeredPlayers = new HashMap<>();

	private final Queue<ServerUniquePlayerIdentifier> playerTurn = new LinkedList<>();

	public ServerUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		if (registeredPlayers.size() >= 2) {
			throw new TooManyPlayersRegistered("There are already 2 players registered in the game.");
		}

		ServerUniquePlayerIdentifier id = new ServerUniquePlayerIdentifier();
		registeredPlayers.put(id, playerInf);

		if (registeredPlayers.size() == MAX_PLAYERS_PER_GAME) {
			pickPlayerOrder();
		}

		return id;
	}

	public void checkPlayerTurn(ServerUniquePlayerIdentifier playerID) {
		if (playerTurn.isEmpty()) {
			throw new PlayerInvalidTurn("Not all players are registered in the game!");
		}

		ServerUniquePlayerIdentifier current = playerTurn.element();

		if (!current.equals(playerID)) {
			// playerTurn.stream().forEach(ele ->
			// System.out.println(ele.getPlayerIDAsString()));
			throw new PlayerInvalidTurn("Not your turn!");
		}
	}

	public void nextTurn() {
		playerTurn.add(playerTurn.remove());
	}

	private void pickPlayerOrder() {
		// as the player IDs are random and the hash map is sorted by them, I just pick
		// the first element from this HashSet and have a random player

		registeredPlayers.keySet().stream().forEach(ele -> playerTurn.add(ele));
	}

}