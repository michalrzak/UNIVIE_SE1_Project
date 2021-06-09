package gamedata.game;

import java.util.Optional;

import gamedata.map.ISFullMapAccesser;
import gamedata.player.IPlayerAccesser;
import gamedata.player.helpers.ESPlayerGameState;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public interface IGameAccesser {

	public IPlayerAccesser getPlayer(SUniquePlayerIdentifier playerID);

	public Optional<ISFullMapAccesser> getFullMap();

	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID);

	public SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer);

	public int getTurn();
}
