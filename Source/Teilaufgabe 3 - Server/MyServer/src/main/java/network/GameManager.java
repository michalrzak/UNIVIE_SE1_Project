package network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.GenericExampleException;
import gamedata.game.GameController;
import gamedata.game.helpers.ServerUniqueGameIdentifier;
import gamedata.map.HalfMapData;
import gamedata.player.helpers.PlayerInformation;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;
import rules.IRules;
import rules.RuleHalfMapCastle;
import rules.RuleHalfMapDimensions;
import rules.RuleHalfMapNoIslands;

public class GameManager {

	private final static List<IRules> rules = List.of(new RuleHalfMapDimensions(), new RuleHalfMapNoIslands(),
			new RuleHalfMapNoIslands(), new RuleHalfMapCastle());

	private final GameController games = new GameController();
	private final NetworkTranslator translate = new NetworkTranslator();

	private static Logger logger = LoggerFactory.getLogger(GameManager.class);

	public UniqueGameIdentifier newGame() {
		return translate.internalGameIDToNetwork(games.createNewGame());
	}

	public UniquePlayerIdentifier registerPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerReg) {
		// should be validated by spring
		ServerUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		PlayerInformation playerInf = translate.networkPlayerRegistrationtoInternal(playerReg);

		ServerUniquePlayerIdentifier playerID = games.registerPlayer(serverGameID, playerInf);

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

		HalfMapData hmdata = translate.networkHalfMapToInernal(receivedHalfMap);
		ServerUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);

		ServerUniquePlayerIdentifier playerID = translate.networkPlayerIDToInternal(receivedHalfMap);

		try {
			games.addHalfMap(serverGameID, playerID, hmdata);
		} catch (GenericExampleException e) {
			logger.error("Failed to add a halfmap" + e.getMessage());
			throw e;
		}
	}

}
