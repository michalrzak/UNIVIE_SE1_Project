package game;

import java.util.Optional;

import game.map.ISFullMapAccesser;
import game.player.IPlayerAccesser;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.SUniquePlayerIdentifier;

public interface IGameAccesser {

	public IPlayerAccesser getPlayer(SUniquePlayerIdentifier playerID);

	public Optional<ISFullMapAccesser> getFullMap();

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID);

	public SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer);

	public int getTurn();

	public boolean getPlayersReady();

	public boolean getMapReady();
}
