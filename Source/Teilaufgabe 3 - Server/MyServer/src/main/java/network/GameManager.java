package network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import exceptions.GenericExampleException;
import gamedata.GameDataController;
import gamedata.SGameState;
import gamedata.game.helpers.SUniqueGameIdentifier;
import gamedata.map.SHalfMap;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.SUniquePlayerIdentifier;
import rules.IRules;
import rules.RuleHalfMapCastle;
import rules.RuleHalfMapDimensions;
import rules.RuleHalfMapNoIslands;

public class GameManager {

	private final static List<IRules> rules = List.of(new RuleHalfMapDimensions(), new RuleHalfMapNoIslands(),
			new RuleHalfMapNoIslands(), new RuleHalfMapCastle());

	private final GameDataController games = new GameDataController();
	private final NetworkTranslator translate = new NetworkTranslator();

	private static Logger logger = LoggerFactory.getLogger(GameManager.class);

	public UniqueGameIdentifier newGame() {
		return translate.internalGameIDToNetwork(games.createNewGame());
	}

	public UniquePlayerIdentifier registerPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerReg) {
		// should be validated by spring
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		PlayerInformation playerInf = translate.networkPlayerRegistrationtoInternal(playerReg);

		SUniquePlayerIdentifier playerID = games.registerPlayer(serverGameID, playerInf);

		return translate.internalPlayerIDToNetwork(playerID);
	}

	public void receiveHalfMap(UniqueGameIdentifier gameID, HalfMap receivedHalfMap) {
		for (IRules rule : rules) {
			try {
				rule.validateHalfMap(receivedHalfMap);
			} catch (GenericExampleException e) {
				logger.warn("A buisness rule threw an error " + e.getMessage());
				throw e;
			}
		}

		SHalfMap hmdata = translate.networkHalfMapToInernal(receivedHalfMap);
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);

		SUniquePlayerIdentifier playerID = translate.networkPlayerIDToInternal(receivedHalfMap);

		try {
			games.addHalfMap(serverGameID, playerID, hmdata);
		} catch (GenericExampleException e) {
			logger.warn("Failed to add a halfmap" + e.getMessage());
			throw e;
		}
	}

	public GameState getGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		SUniquePlayerIdentifier serverPlayerID = translate.networkPlayerIDToInternal(playerID);

		SGameState gameState = games.getGameState(serverGameID, serverPlayerID);

		GameStateExtractor gse = new GameStateExtractor();

		return gse.extractGameState(gameState);

	}

}
