package Networking;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import MessagesBase.ERequestState;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import reactor.core.publisher.Mono;

public class NetworkEndpoint {
	
	private UniquePlayerIdentifier playerID;
	private UniqueGameIdentifier gameID;
	private String gameURL;
	private WebClient baseWebClient;
	
	public NetworkEndpoint(String gameURL, UniqueGameIdentifier gameID) {
		this.gameURL = gameURL;
		this.gameID = gameID;
		
		baseWebClient = WebClient.builder().baseUrl(this.gameURL + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) //the network protocol uses XML
			    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
			    .build();
		
	}
	
	public void registerPlayer(PlayerRegistration playerReg) {
		if (playerReg == null) 
			throw new IllegalArgumentException("the playerReg passed is null");
		
		if (playerID != null)
			//maybe make this a custom class?
			throw new RuntimeException("playerID already set; Player already registered on the servere");
		
		
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST)
				.uri("/" + gameID.getUniqueGameID() + "/players")
				.body(BodyInserters.fromValue(playerReg)) // specify the data which is set to the server
				.retrieve()
				.bodyToMono(ResponseEnvelope.class); // specify the object returned by the server
		
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
		
		// always check for errors, and if some are reported at least print them to the console  (logging should be preferred)
		// so that you become aware of them during debugging! The provided server gives you very helpful error messages.
		if(resultReg.getState() == ERequestState.Error)
			//here i will prob. need a new class
			throw new RuntimeException(resultReg.getExceptionMessage());
		
		else
			playerID = resultReg.getData().get();
		
	}
	
	
	
	//sendHalfMap()
	
}
