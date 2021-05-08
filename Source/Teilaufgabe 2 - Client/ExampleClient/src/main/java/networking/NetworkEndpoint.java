package networking;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import exceptions.NetworkCommunicationException;
import reactor.core.publisher.Mono;

public class NetworkEndpoint {

	private static Logger logger = LoggerFactory.getLogger(NetworkEndpoint.class);

	private final UniqueGameIdentifier gameID;
	private final String gameURL;
	private final WebClient baseWebClient;

	private GameState cached;
	private boolean gameStateValid = false;

	private Date d = new Date();
	private long lastCommandTs = 0;

	public NetworkEndpoint(String gameURL, UniqueGameIdentifier gameID) {
		if (gameURL == null || gameID == null) {
			logger.error("Passed parameters into constructor were null!");
			throw new IllegalArgumentException("Cannot pass null arguments!");
		}

		// save passed arguments
		this.gameURL = gameURL;
		this.gameID = gameID;

		// prepare web communication
		baseWebClient = WebClient.builder().baseUrl(this.gameURL + "/games")
				// the network protocol uses XML
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}

	// used to enforce the 400ms between requests!
	private void waitForCommand() {
		if (d.getTime() - lastCommandTs < 400)
			try {
				logger.debug("sleeping for: " + (400 - d.getTime() + lastCommandTs));
				Thread.sleep(400 - (d.getTime() - lastCommandTs));
			} catch (InterruptedException e) {
				logger.error("Please don't run this programm in multiple threads!!!");
				throw new RuntimeException("A thread interupted the programm execution.");
			}

		lastCommandTs = d.getTime();
	}

	private <T> ResponseEnvelope<T> getResults(Mono<ResponseEnvelope> webAccess) throws NetworkCommunicationException {

		// try to get the data from network. if no network connection is present this
		// can fail
		ResponseEnvelope<T> result;
		try {
			result = webAccess.block();
		} catch (RuntimeException e) {
			logger.error("The network communication failed!");
			throw new NetworkCommunicationException(e);
		}

		// check if the server did not answer with an error. else throw
		if (result.getState() == ERequestState.Error) {
			logger.error("PlayerRegistration failed, exception: " + result.getExceptionMessage());
			throw new NetworkCommunicationException(result.getExceptionMessage());
		}

		return result;
	}

	public UniquePlayerIdentifier registerPlayer(PlayerRegistration playerReg) throws NetworkCommunicationException {
		if (playerReg == null) {
			logger.error("PlayerRegistration passed to registerPlayer is null");
			throw new IllegalArgumentException("the playerReg passed is null");
		}

		waitForCommand();

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				// specify the data which is set to the server
				.uri("/" + gameID.getUniqueGameID() + "/players").body(BodyInserters.fromValue(playerReg)).retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		// this can throw NetworkCommunicationException
		ResponseEnvelope<UniquePlayerIdentifier> result = getResults(webAccess);

		// return results
		return result.getData().get();
	}

	public GameState getGameState(UniquePlayerIdentifier playerID) throws NetworkCommunicationException {
		if (playerID == null) {
			logger.error("playerID was null");
			throw new IllegalArgumentException("The PlayerID passed is null");
		}

		waitForCommand();

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameID.getUniqueGameID() + "/states/" + playerID.getUniquePlayerID()).retrieve()
				.bodyToMono(ResponseEnvelope.class);

		// this can throw NetworkCommunicationException
		ResponseEnvelope<GameState> result = getResults(webAccess);

		// cache the results and make the cached results valid
		cached = result.getData().get();
		gameStateValid = true;

		// return results
		return cached;
	}

	public GameState getGameStateCached(UniquePlayerIdentifier playerID) throws NetworkCommunicationException {
		if (playerID == null) {
			logger.error("playerID was null");
			throw new IllegalArgumentException("The PlayerID passed is null");
		}

		// if the saved game state is valid return it, however if it is older then 400
		// then get a new one
		if (gameStateValid && d.getTime() - lastCommandTs < 400) {
			logger.debug("returning cached");
			return cached;
		}

		// if cached object is invalid get a new one
		return getGameState(playerID);
	}

	public void sendHalfMap(HalfMap hm) throws NetworkCommunicationException {
		if (hm == null) {
			logger.error("sendHalfMap received null as halfmap");
			throw new RuntimeException("Given HalfMap is null");
		}

		waitForCommand();

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				// specify the data which is set to the server
				.uri("/" + gameID.getUniqueGameID() + "/halfmaps").body(BodyInserters.fromValue(hm)).retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		// this can throw NetworkCommunicationException
		ResponseEnvelope<UniquePlayerIdentifier> result = getResults(webAccess);
	}

	public void sendMove(PlayerMove pm) throws NetworkCommunicationException {
		if (pm == null) {
			logger.error("sendMove received null as PlayerMove");
			throw new RuntimeException("Given PlayerMove is null");
		}

		waitForCommand();

		Mono<ResponseEnvelope> webAccess = (baseWebClient.method(HttpMethod.POST))
				// specify the data which is set to the server
				.uri("/" + gameID.getUniqueGameID() + "/moves").header("accept", "application/xml")
				.body(BodyInserters.fromValue(pm)).retrieve()
				// specify the object returned by the server
				.bodyToMono(ResponseEnvelope.class);

		// this can throw NetworkCommunicationException
		ResponseEnvelope<UniquePlayerIdentifier> result = getResults(webAccess);

		// if I send a move the saved gameState becomes invalid
		gameStateValid = false;

	}

}
