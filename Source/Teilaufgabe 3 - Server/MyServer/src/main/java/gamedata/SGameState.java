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
	private final SUniquePlayerIdentifier other;
	private final IGameAccesser game;

	public SGameState(SUniquePlayerIdentifier owner, IGameAccesser game) {
		this.owner = owner;
		this.other = game.getOtherPlayer(owner);
		this.game = game;
	}

	public IPlayerAccesser getOwnerPlayer() {
		return game.getPlayer(owner);
	}

	public IPlayerAccesser getOtherPlayer() {
		IPlayerAccesser otherPlayerOriginal = game.getPlayer(other);

		// create a new player with a random ID, but with the correct playerInformation
		Player otherPlayerReturn = Player.getRandomPlayer(otherPlayerOriginal.getFirstName(),
				otherPlayerOriginal.getLastName(), otherPlayerOriginal.getStudentID());

		return otherPlayerReturn;
	}

	public ESPlayerGameState getOwnerPlayerGameState() {
		return game.getPlayerState(owner);
	}

	public ESPlayerGameState getOtherPlayerGameState() {
		return game.getPlayerState(other);
	}

	public Optional<FullMapState> getFullMap() {
		Optional<ISFullMapAccesser> fullMap = game.getFullMap();

		if (fullMap.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new FullMapState(owner, other, fullMap.get(), game.getTurn()));
	}

	public int getTurn() {
		return game.getTurn();
	}

}
