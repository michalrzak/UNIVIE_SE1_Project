package rules;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.TooManyPlayersRegistered;
import game.IGameControllerAccesser;
import game.helpers.EGameConstants;
import network.translation.NetworkTranslator;

public class RuleGameNotFull implements IRules {

	private final IGameControllerAccesser games;

	public RuleGameNotFull(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// assume gameID is already validated
		// TODO: is this OK?

		NetworkTranslator translate = new NetworkTranslator();

		var registeredPlayers = games.playersRegisteredInGame(translate.networkGameIDToInternal(gameID));

		if (registeredPlayers.size() >= EGameConstants.MAX_PLAYER_COUNT.get()) {
			throw new TooManyPlayersRegistered("The game you tried to register to is already full!");
		}
	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

}
