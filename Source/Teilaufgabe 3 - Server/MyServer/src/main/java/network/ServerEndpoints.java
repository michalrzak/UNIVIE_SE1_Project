package network;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import exceptions.GenericExampleException;
import game.GameController;
import game.SGameState;
import game.helpers.SUniqueGameIdentifier;
import game.map.SHalfMap;
import game.map.helpers.ESMove;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;
import network.translation.NetworkTranslator;
import rules.IRules;
import rules.RuleHalfMapCastle;
import rules.RuleHalfMapDimensions;
import rules.RuleHalfMapEdges;
import rules.RuleHalfMapNoIslands;
import rules.RuleHalfMapTerrainCount;

@Controller
@RequestMapping(value = "/games")
public class ServerEndpoints {

	// private final GameManager games = new GameManager();

	private final static List<IRules> rules = List.of(new RuleHalfMapDimensions(), new RuleHalfMapTerrainCount(),
			new RuleHalfMapEdges(), new RuleHalfMapNoIslands(), new RuleHalfMapCastle());

	private static Logger logger = LoggerFactory.getLogger(ServerEndpoints.class);

	private final GameController games;
	private final NetworkTranslator translate = new NetworkTranslator();

	@Autowired
	public ServerEndpoints(GameController games) {
		this.games = games;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame() {
		return translate.internalGameIDToNetwork(games.createNewGame());
	}

	// example for a POST endpoint based on games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		// translate data for server
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		PlayerInformation playerInf = translate.networkPlayerRegistrationtoInternal(playerRegistration);

		// generate result
		SUniquePlayerIdentifier playerID = games.registerPlayer(serverGameID, playerInf);

		// translate result for network
		UniquePlayerIdentifier netPlayerID = translate.internalPlayerIDToNetwork(playerID);

		// return result
		return new ResponseEnvelope<>(netPlayerID);
	}

	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope receiveHalfMap(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody HalfMap halfMap) {

		// translate data
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		SUniquePlayerIdentifier serverPlayerID = translate.networkPlayerIDToInternal(halfMap);

		// validate complex data
		for (IRules rule : rules) {
			try {
				rule.validateHalfMap(halfMap);
			} catch (GenericExampleException e) {
				games.setLooser(serverGameID, serverPlayerID);
				logger.warn("A buisness rule threw an error " + e.getMessage());
				throw e;
			}
		}

		// translate complex data
		SHalfMap hmdata = translate.networkHalfMapToInernal(halfMap);

		// save half map
		try {
			games.addHalfMap(serverGameID, serverPlayerID, hmdata);
		} catch (GenericExampleException e) {
			logger.warn("Failed to add a halfmap" + e.getMessage());
			throw e;
		}

		// if it got here no error was thrown and return
		return new ResponseEnvelope();
	}

	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> receiveHalfMap(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @PathVariable UniquePlayerIdentifier playerID) {

		// translate data
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		SUniquePlayerIdentifier serverPlayerID = translate.networkPlayerIDToInternal(playerID);

		// get required data from server
		SGameState gameState = games.getGameState(serverGameID, serverPlayerID);

		// translate complex data
		GameState netGameState = translate.internalGameStateToNetwork(gameState);

		return new ResponseEnvelope<>(netGameState);
	}

	@RequestMapping(value = "/{gameID}/moves", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope receiveMove(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated PlayerMove playerMove) {

		// translate data
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		SUniquePlayerIdentifier serverPlayerID = translate.networkPlayerIDToInternal(playerMove);
		ESMove serverMove = translate.networkMoveToInternal(playerMove.getMove());

		// move the player
		games.receiveMove(serverGameID, serverPlayerID, serverMove);

		// if it got here no error was thrown and return
		return new ResponseEnvelope();
	}

	/*
	 * Note, this is only the most basic way of handling exceptions in spring (but
	 * sufficient for our task) it would for example struggle if you use multiple
	 * controllers. Add the exception types to the @ExceptionHandler which your
	 * exception handling should support, the superclass catches subclasses aspect
	 * of try/catch applies also here. Hence, we recommend to simply extend your own
	 * Exceptions from the GenericExampleException. For larger projects one would
	 * most likely want to use the HandlerExceptionResolver, see here
	 * https://www.baeldung.com/exception-handling-for-rest-with-spring
	 * 
	 * Ask yourself: Why is handling the exceptions in a different method than the
	 * endpoint methods a good solution?
	 */
	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());

		// reply with 200 OK as defined in the network documentation
		// Side note: We only do this here for simplicity reasons. For future projects,
		// you should check out HTTP status codes and
		// what they can be used for. Note, the WebClient used on the client can react
		// to them using the .onStatus(...) method.
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
