package gamedata;

import java.util.Optional;

import gamedata.game.IGameAccesser;
import gamedata.map.FullMapState;
import gamedata.map.ISFullMapAccesser;
import gamedata.player.IPlayerAccesser;
import gamedata.player.Player;
import gamedata.player.helpers.ESPlayerGameState;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class SGameState {

	private final SUniquePlayerIdentifier owner;
	private final IGameAccesser game;

	private Optional<SUniquePlayerIdentifier> other = Optional.empty();

	public SGameState(SUniquePlayerIdentifier owner, IGameAccesser game) {
		this.owner = owner;
		this.game = game;

		if (game.getReady()) {
			other = Optional.of(game.getOtherPlayer(owner));
		}
	}

	public IPlayerAccesser getOwnerPlayer() {
		return game.getPlayer(owner);
	}

	public Optional<IPlayerAccesser> getOtherPlayer() {

		if (other.isEmpty()) {
			return Optional.empty();
		}

		IPlayerAccesser otherPlayerOriginal = game.getPlayer(other.get());

		// create a new player with a random ID, but with the correct playerInformation
		Player otherPlayerReturn = Player.getRandomPlayer(otherPlayerOriginal.getFirstName(),
				otherPlayerOriginal.getLastName(), otherPlayerOriginal.getStudentID());

		return Optional.of(otherPlayerReturn);
	}

	public ESPlayerGameState getOwnerPlayerGameState() {
		return game.getPlayerState(owner);
	}

	public Optional<ESPlayerGameState> getOtherPlayerGameState() {
		if (other.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(game.getPlayerState(other.get()));
	}

	public Optional<FullMapState> getFullMap() {
		Optional<ISFullMapAccesser> fullMap = game.getFullMap();

		if (fullMap.isEmpty() || other.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new FullMapState(owner, other.get(), fullMap.get(), game.getTurn()));
	}

	public int getTurn() {
		return game.getTurn();
	}

}
