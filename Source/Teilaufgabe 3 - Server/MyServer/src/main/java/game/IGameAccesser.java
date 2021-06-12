package game;

import java.util.Collection;
import java.util.Optional;

import game.map.ISFullMapAccesser;
import game.player.IPlayerAccesser;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.SUniquePlayerIdentifier;

public interface IGameAccesser {

	public Optional<ISFullMapAccesser> getFullMap();

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID);

	public Collection<IPlayerAccesser> getRegisteredPlayers();

	public int getTurn();

	public boolean getPlayersReady();

	public boolean getMapReady();
}
