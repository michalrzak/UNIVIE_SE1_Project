package network.translation;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import game.SGameState;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
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

}
