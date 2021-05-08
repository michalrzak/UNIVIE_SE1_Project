package execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.NetworkCommunicationException;
import gameData.GameData;
import gameData.helpers.EGameState;
import map.fullMap.FullMapAccesser;
import map.fullMap.FullMapData;
import map.halfMap.HalfMapGenerator;
import move.generation.MoveGenerator;
import networking.NetworkTranslator;
import ui.CLI;

public class Controller {

	private static Logger logger = LoggerFactory.getLogger(Controller.class);

	public static void main(String[] args) {

		// parse the parameters, otherwise the automatic evaluation will not work on
		// http://swe.wst.univie.ac.at
		final String serverBaseUrl = args[1];
		final String gameId = args[2];

		// start network communication
		final NetworkTranslator net = new NetworkTranslator(serverBaseUrl, gameId);

		try {
			// register player
			net.registerPlayer("Michal Robert", "Zak", "11922222");

			// wait for other client to register
			// if a client fails to register this will result in an infinite loop!!!
			while (!net.myTurn())
				/* do nothing */;

			logger.debug("Both players registered, starting to generate HalfMap");

			// generate & send a HalfMap
			net.sendHalfMap(HalfMapGenerator.generateMap());
			logger.debug("HalfMap sent to server");

			// Wait for other client to send HalfMap as well
			// if a client fails to register this will result in an infinite loop!!!
			while (!net.myTurn())
				/* do nothing */;

			logger.debug("Both players sent HalfMaps, retrieving FullMap from server");

			// retrieve FullMap
			final FullMapData map = net.getFullMap();
			final GameData gameData = new GameData(true);

			final CLI ui = new CLI(map, gameData);

			final MoveGenerator mg = new MoveGenerator(new FullMapAccesser(map));

			while (gameData.getGameState() == EGameState.UNDETERMINED) {
				while (!net.myTurn() && net.getGameState() == EGameState.UNDETERMINED)
					/* do nothing */;

				gameData.updateGameState(net.getGameState());

				if (gameData.getGameState() != EGameState.UNDETERMINED)
					break;

				net.sendMove(mg.getNextMove());
				map.updateEntities(net.getEntities());
				if (net.collectedTreasure())
					map.collectTreasure();

				gameData.nextTurn();
			}
		} catch (NetworkCommunicationException e) {
			System.out.println("Sorry, something went wrong with the network communication!"
					+ " Please check your internet connection and try again. The error message is: " + e.getMessage());
		}
	}
}
