package game;

import java.util.Collection;
import java.util.Optional;

import game.map.IMapAccesser;
import game.map.helpers.FullMapState;
import game.player.IPlayerAccesser;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.PlayerMaskedID;
import game.player.helpers.SUniquePlayerIdentifier;

public class SGameState {

	private final IPlayerAccesser owner;
	private final IGameAccesser game;

	private final Optional<IPlayerAccesser> other;

	public SGameState(SUniquePlayerIdentifier owner, IGameAccesser game) {
		this.game = game;

		Collection<IPlayerAccesser> registeredPlayers = game.getRegisteredPlayers();
		assert (registeredPlayers.contains(owner));

		this.owner = registeredPlayers.stream().filter(player -> player.equals(owner)).findFirst().get();
		this.other = registeredPlayers.stream().filter(player -> !player.equals(owner)).findFirst();
	}

	public IPlayerAccesser getOwnerPlayer() {
		return owner;
	}

	public Optional<IPlayerAccesser> getOtherPlayer() {

		if (other.isEmpty()) {
			return Optional.empty();
		}

		IPlayerAccesser otherPlayerOriginal = other.get();

		// create a new player with a random ID, but with the correct playerInformation
		PlayerMaskedID otherPlayerReturn = new PlayerMaskedID(otherPlayerOriginal);

		return Optional.of(otherPlayerReturn);
	}

	public ESPlayerGameState getOwnerPlayerGameState() {
		return game.getPlayerState(owner.getPlayerID());
	}

	public Optional<ESPlayerGameState> getOtherPlayerGameState() {
		if (other.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(game.getPlayerState(other.get().getPlayerID()));
	}

	public Optional<FullMapState> getFullMap() {
		Optional<IMapAccesser> fullMap = game.getFullMap();

		if (fullMap.isEmpty() || other.isEmpty()) {
			return Optional.empty();
		}

		return Optional
				.of(new FullMapState(owner.getPlayerID(), other.get().getPlayerID(), fullMap.get(), game.getTurn()));
	}

	public int getTurn() {
		return game.getTurn();
	}

}
