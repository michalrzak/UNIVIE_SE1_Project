package gamedata.player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.InternalServerException;
import exceptions.TooManyPlayersRegistered;
import gamedata.EGameConstants;
import gamedata.player.helpers.ESPlayerGameState;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class PlayersController {

	// private final PlayerStorage players = new PlayerStorage();
	private final Set<Player> registeredPlayers = new HashSet<>();
	private final Queue<Player> playerTurn = new LinkedList<>();

	private Optional<SUniquePlayerIdentifier> winner = Optional.empty();

	private static Logger logger = LoggerFactory.getLogger(PlayersController.class);

	public SUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {

		Player newPlayer = new Player(playerInf);
		if (registeredPlayers.size() == EGameConstants.MAX_PLAYER_COUNT.getValue()) {
			logger.warn("Tried registering a player even though the game is already full!");
			throw new TooManyPlayersRegistered("The game you tried to register for is already full");
		}

		registeredPlayers.add(newPlayer);

		if (registeredPlayers.size() == EGameConstants.MAX_PLAYER_COUNT.getValue()) {
			logger.debug("Reached max players. Picking player to go first!");
			pickPlayerOrder();
		}

		return newPlayer;
	}

	public boolean checkPlayerTurn(SUniquePlayerIdentifier playerID) {
		if (playerTurn.isEmpty()) {
			logger.warn("Tried checking player turn even though the game is not ready!");
			return false;
		}

		SUniquePlayerIdentifier current = playerTurn.element();
		if (!current.equals(playerID)) {
			return false;
		}
		return true;
	}

	public boolean checkPlayer(SUniquePlayerIdentifier playerID) {
		return registeredPlayers.contains(playerID);
	}

	public void nextTurn() {
		playerTurn.add(playerTurn.remove());
	}

	public Collection<IPlayerAccesser> getPlayers() {
		// convert the Player objects to IPlayerAccesser
		return registeredPlayers.stream().collect(Collectors.toList());
	}

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID) {
		if (winner.isPresent()) {
			if (playerID.equals(winner.get())) {
				return ESPlayerGameState.WON;
			} else {
				return ESPlayerGameState.LOST;
			}
		}

		if (playerID.equals(playerTurn.element())) {
			return ESPlayerGameState.SHOULD_ACT_NEXT;
		} else {
			return ESPlayerGameState.SHOULD_WAIT;
		}
	}

	public SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer) {
		if (playerTurn.isEmpty()) {
			logger.error("Tried accessing other player even though the game has not started yet!");
			throw new InternalServerException("Sorry, but the server had an internal error!");
		}

		return registeredPlayers.stream().filter(player -> player.equals(myPlayer)).findFirst().get();
	}

	private void pickPlayerOrder() {
		List<Player> players = registeredPlayers.stream().collect(Collectors.toList());
		// shuffle the registered playerIDs
		Collections.shuffle(players);
		playerTurn.addAll(players);

	}

}