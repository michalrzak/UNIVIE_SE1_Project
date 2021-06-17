package rules;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.TooManyHalfMapsReceived;
import game.IGameControllerAccesser;
import network.translation.NetworkTranslator;

public class RuleOnlyOneHalfMapPerPlayer implements IRules {

	private final IGameControllerAccesser games;

	public RuleOnlyOneHalfMapPerPlayer(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		// assume gameID is already validated
		// TODO: is this OK?

		NetworkTranslator translate = new NetworkTranslator();

		boolean mapReady = games.isMapReady(translate.networkGameIDToInternal(gameID));

		// if the map is ready the game has combined the two half maps and thus if the
		// game was turn based both players have sent one half map each
		if (mapReady) {
			throw new TooManyHalfMapsReceived("The game you triedd to send a halfmap to already had both half maps");
		}

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
