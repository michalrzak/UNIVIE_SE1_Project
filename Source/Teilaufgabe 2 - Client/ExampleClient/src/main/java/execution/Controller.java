package execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fullMap.FullMapData;
import halfMap.HalfMapGenerator;
import networking.NetworkEndpoint;
import networking.NetworkTranslator;

public class Controller {

	private static Logger logger = LoggerFactory.getLogger(NetworkEndpoint.class);

	// it is OK to say throws here. Since I know i wont be using multiple threads
	// this won't ever be an issue. Theoreticaly there is imporovement potential
	// though
	public static void main(String[] args) throws InterruptedException {

		/*
		 * IMPORTANT: Parsing/Handling of starting parameters. args[0] = Game Mode, you
		 * can use this to know that you code is running on the evaluation server (if
		 * this is the case args[0] = TR). If this is the case only a command line
		 * interface must be displayed. Also, no JavaFX and Swing UI component and
		 * classes must be used/executed by your Client in any way IF args[0]=TR.
		 * args[1] = Server url, will hold the server url which your client should use.
		 * Note, only use the server url supplied here as the url used by you during the
		 * development and by the evaluation server (for grading) is NOT the same!
		 * args[1] enables your client to always get the correct one. args[2] = Holds
		 * the game ID which your client should use. For testing purposes you can create
		 * a new one by accessing http://swe.wst.univie.ac.at:18235/games with your web
		 * browser. IMPORANT: If there is a value stored in args[2] you MUST use it! DO
		 * NOT create new games in your code in such a case!
		 * 
		 * DON'T FORGET TO EVALUATE YOUR FINAL IMPLEMENTATION WITH OUR TEST SERVER. THIS
		 * IS ALSO THE BASE FOR GRADING. THE TEST SERVER CAN BE FOUND AT:
		 * http://swe.wst.univie.ac.at/
		 * 
		 * HINT: The assignment section in Moodle also explains all the important
		 * aspects about the start parameters/arguments. Use the Run Configurations (as
		 * shown during the first lecture) in Eclipse to simulate the starting of an
		 * application with start parameters or implement your own argument parsing code
		 * to become more flexible (e.g., to mix hard coded and supplied parameters
		 * whenever the one or the other is available).
		 */

		// parse the parameters, otherwise the automatic evaluation will not work on
		// http://swe.wst.univie.ac.at
		String serverBaseUrl = args[1];
		String gameId = args[2];

		// start network communication
		NetworkTranslator net = new NetworkTranslator(serverBaseUrl, gameId);

		// register player
		net.registerPlayer("Michal Robert", "Zak", "11922222");

		// wait for other client to register
		// if a client fails to register this will result in an infinite loop!!!
		while (!net.myTurn())
			Thread.sleep(400);

		logger.debug("Both players registered, starting to generate HalfMap");

		// generate & send a HalfMap
		net.sendHalfMap(HalfMapGenerator.generateMap());
		logger.debug("HalfMap sent to server");

		// Wait for other client to send HalfMap as well
		// if a client fails to register this will result in an infinite loop!!!
		while (!net.myTurn())
			Thread.sleep(400);

		logger.debug("Both players sent HalfMaps, retrieving FullMap from server");

		// retrieve FullMap
		FullMapData map = net.getFullMap();

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
