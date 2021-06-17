package rules;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.PlayerInvalidTurn;
import game.IGameControllerAccesser;
import game.player.helpers.SUniquePlayerIdentifier;
import network.translation.NetworkTranslator;

public class RulePlayerTurn implements IRules {

	private final IGameControllerAccesser games;

	public RulePlayerTurn(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		validatePlayerTurn(gameID, halfmap);
	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		validatePlayerTurn(gameID, playerMove);

	}

	private void validatePlayerTurn(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// assume gameID is already validated
		NetworkTranslator translate = new NetworkTranslator();
		SUniquePlayerIdentifier hasTurn = games.playersTurnInGame(translate.networkGameIDToInternal(gameID));

		if (!hasTurn.asString().equals(playerID.getUniquePlayerID())) {
			throw new PlayerInvalidTurn("It was not the players turn!");
		}
	}

}
