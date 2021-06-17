package game;

import java.util.Collection;
import java.util.Optional;

import game.map.IMapAccesser;
import game.player.IPlayerAccesser;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.SUniquePlayerIdentifier;

public interface IGameAccesser {

	public Optional<IMapAccesser> getFullMap();

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID);

	public Collection<IPlayerAccesser> getRegisteredPlayers();

	public SUniquePlayerIdentifier getPlayerTurn();

	public int getTurn();

	public boolean arePlayersReady();

	public boolean isMapReady();
}
