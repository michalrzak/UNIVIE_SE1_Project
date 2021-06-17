package rules;

import java.util.stream.Collectors;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.PlayerNotFoundException;
import game.IGameControllerAccesser;
import network.translation.NetworkTranslator;

public class RuleUniquePlayerIdentifierRegistered implements IRules {

	private final IGameControllerAccesser games;

	public RuleUniquePlayerIdentifierRegistered(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// assume gameID is already validated
		// TODO: is this assumption OK?

		NetworkTranslator translate = new NetworkTranslator();

		var registeredPlayersString = games.playersRegisteredInGame(translate.networkGameIDToInternal(gameID)).stream()
				.map(pID -> pID.asString()).collect(Collectors.toSet());

		String strPlayerID = playerID.getUniquePlayerID();

		if (!registeredPlayersString.contains(strPlayerID)) {
			throw new PlayerNotFoundException("The sent playerID was not found in the provided game");
		}

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

}
