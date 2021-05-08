package execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	// it is OK to say throws here. Since I know i wont be using multiple threads
	// this won't ever be an issue. Theoreticaly there is imporovement potential
	// though
	public static void main(String[] args) throws InterruptedException {

		// parse the parameters, otherwise the automatic evaluation will not work on
		// http://swe.wst.univie.ac.at
		final String serverBaseUrl = args[1];
		final String gameId = args[2];

		// start network communication
		final NetworkTranslator net = new NetworkTranslator(serverBaseUrl, gameId);

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

		/*
		 * TIP: Check out the network protocol documentation. It shows you with a nice
		 * sequence diagram all the steps which are required to be executed by your
		 * client along with a general overview on the required behavior (e.g., when it
		 * is necessary to repeatedly ask the server for its state to determine if
		 * actions can be sent or not). When the client will need to wait for the other
		 * client and when you client should stop with sending any more messages to the
		 * server.
		 */

		/*
		 * TIP: A game consist out of two clients, how can I get two clients for testing
		 * purposes? Start your client two times. You can do this in Eclipse by hitting
		 * the green start button twice. Or you can start your jar file twice in two
		 * different terminals. When you hit the debug button twice you can even debug
		 * both clients "independently" from each other.
		 */
	}
}
