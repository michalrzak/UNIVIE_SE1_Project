package networking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import reactor.core.publisher.Mono;

public class NetworkEndpoint {

	private static Logger logger = LoggerFactory.getLogger(NetworkEndpoint.class);

	private UniqueGameIdentifier gameID;
	private String gameURL;
	private WebClient baseWebClient;

	public NetworkEndpoint(String gameURL, UniqueGameIdentifier gameID) {
		this.gameURL = gameURL;
		this.gameID = gameID;

		baseWebClient = WebClient.builder().baseUrl(this.gameURL + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
																							// XML
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

	}

	public UniquePlayerIdentifier registerPlayer(PlayerRegistration playerReg) {
		if (playerReg == null) {
			logger.error("PlayerRegistration passed to registerPlayer is null");
			throw new IllegalArgumentException("the playerReg passed is null");
		}

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				// specify the data which is set to the server
				.uri("/" + gameID.getUniqueGameID() + "/players").body(BodyInserters.fromValue(playerReg)).retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		ResponseEnvelope<UniquePlayerIdentifier> result = webAccess.block();

		// always check for errors, and if some are reported at least print them to the
		// console (logging should be preferred)
		// so that you become aware of them during debugging! The provided server gives
		// you very helpful error messages.
		if (result.getState() == ERequestState.Error) {
			// here i will prob. need a new class
			logger.error("PlayerRegistration failed, exception: " + result.getExceptionMessage());
			throw new RuntimeException(result.getExceptionMessage());
		}

		return result.getData().get();

	}

	public GameState getGameState(UniquePlayerIdentifier playerID) {
		if (playerID == null)
			throw new RuntimeException("The PlayerID passed is null");

		// maybe make the next section a private method? it is repeated mostly the same
		// in both methods
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameID.getUniqueGameID() + "/states/" + playerID.getUniquePlayerID()).retrieve()
				.bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<GameState> result = webAccess.block();

		if (result.getState() == ERequestState.Error)
			// here i will prob. need a new class
			throw new RuntimeException(result.getExceptionMessage());

		return result.getData().get();
	}

	public void sendHalfMap(HalfMap hm) {
		if (hm == null) {
			logger.error("sendHalfMap received null as halfmap");
			throw new RuntimeException("Given HalfMap is null");
		}

		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				// specify the data which is set to the server
				.uri("/" + gameID.getUniqueGameID() + "/halfmaps").body(BodyInserters.fromValue(hm)).retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		ResponseEnvelope<UniquePlayerIdentifier> result = webAccess.block();

		// always check for errors, and if some are reported at least print them to the
		// console (logging should be preferred)
		// so that you become aware of them during debugging! The provided server gives
		// you very helpful error messages.
		if (result.getState() == ERequestState.Error)
			// here i will prob. need a new class
			throw new RuntimeException(result.getExceptionMessage());

		System.out.println("sending halfmap");
		System.out.println(result.getState().toString());

	}

}
