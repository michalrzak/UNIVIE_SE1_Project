package gamedata.game;

import java.util.Collection;
import java.util.Optional;

import gamedata.map.ISFullMapAccesser;
import gamedata.player.IPlayerAccesser;
import gamedata.player.helpers.ESPlayerGameState;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public interface IGameAccesser {
	public Collection<IPlayerAccesser> getPlayers();

	public Optional<ISFullMapAccesser> getFullMap();

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID);

	public SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer);

	public int getTurn();
}
