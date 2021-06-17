package game;

import java.util.Collection;
import java.util.Optional;

import game.helpers.SUniqueGameIdentifier;
import game.map.IMapAccesser;
import game.player.helpers.SUniquePlayerIdentifier;

public interface IGameControllerAccesser {
	public Collection<SUniqueGameIdentifier> getUsedGameID();

	public SUniquePlayerIdentifier playersTurnInGame(SUniqueGameIdentifier gameID);

	public Collection<SUniquePlayerIdentifier> playersRegisteredInGame(SUniqueGameIdentifier gameID);

	public boolean isMapReady(SUniqueGameIdentifier gameID);

	public Optional<IMapAccesser> getMapAccesser(SUniqueGameIdentifier gameID);
}
