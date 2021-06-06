package gamedata.game;

import java.util.Collection;
import java.util.Optional;

import gamedata.map.FullMapData;
import gamedata.player.Player;

public interface GameAccesser {
	public Collection<Player> getPlayers();

	public Optional<FullMapData> getFullMap();

}
