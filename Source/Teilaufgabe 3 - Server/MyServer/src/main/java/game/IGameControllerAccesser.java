package game;

import java.util.Collection;

import game.helpers.SUniqueGameIdentifier;
import game.player.helpers.SUniquePlayerIdentifier;

public interface IGameControllerAccesser {
	public Collection<SUniqueGameIdentifier> getUsedGameID();

	public SUniquePlayerIdentifier playersTurnInGame(SUniqueGameIdentifier gameID);

	public Collection<SUniquePlayerIdentifier> playersRegisteredInGame(SUniqueGameIdentifier gameID);

	public boolean isMapReady(SUniqueGameIdentifier gameID);
}
