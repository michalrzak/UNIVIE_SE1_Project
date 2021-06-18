package rules;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.MapNotReadyException;
import game.IGameControllerAccesser;
import game.helpers.SUniqueGameIdentifier;
import network.translation.NetworkTranslator;

public class RuleMapReady implements IRules {

	private final IGameControllerAccesser games;

	public RuleMapReady(IGameControllerAccesser games) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// assert gameID is validated
		NetworkTranslator translate = new NetworkTranslator();
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);

		if (!games.isMapReady(serverGameID)) {
			throw new MapNotReadyException(
					"The game you tried to send a move to has no finnished map! (Both players did not send the halfmap)");
		}
	}

}
