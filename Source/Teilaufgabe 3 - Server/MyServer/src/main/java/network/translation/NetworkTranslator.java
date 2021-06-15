package network.translation;

import MessagesBase.EMove;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import exceptions.InternalServerException;
import game.SGameState;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.move.helpers.ESMove;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;

public class NetworkTranslator {

	GameStateExtractor gameStateTranslate = new GameStateExtractor();
	NetworkHalfMapTranslator halfMapTrans = new NetworkHalfMapTranslator();

	public SUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new SUniqueGameIdentifier(gameID.getUniqueGameID());
	}

	public UniqueGameIdentifier internalGameIDToNetwork(SUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.getIDAsString());
	}

	public SUniquePlayerIdentifier networkPlayerIDToInternal(UniquePlayerIdentifier playerID) {
		return new SUniquePlayerIdentifier(playerID.getUniquePlayerID());
	}

	public UniquePlayerIdentifier internalPlayerIDToNetwork(SUniquePlayerIdentifier playerID) {
		return new UniquePlayerIdentifier(playerID.getPlayerIDAsString());
	}

	public PlayerInformation networkPlayerRegistrationtoInternal(PlayerRegistration playerReg) {
		return new PlayerInformation(playerReg.getStudentFirstName(), playerReg.getStudentLastName(),
				playerReg.getStudentID());
	}

	public SHalfMap networkHalfMapToInernal(HalfMap halfmap) {
		return halfMapTrans.translateNetworkHalfMap(halfmap);
	}

	public GameState internalGameStateToNetwork(SGameState gameState) {
		return gameStateTranslate.extractGameState(gameState);
	}

	public ESMove networkMoveToInternal(EMove move) {
		switch (move) {
		case Down:
			return ESMove.DOWN;
		case Left:
			return ESMove.LEFT;
		case Right:
			return ESMove.RIGHT;
		case Up:
			return ESMove.UP;
		}
		throw new InternalServerException("Failed to convert network move to internal.");
	}

}
