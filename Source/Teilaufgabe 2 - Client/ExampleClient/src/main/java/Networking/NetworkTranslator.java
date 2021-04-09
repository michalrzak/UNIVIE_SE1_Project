package Networking;

import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;

public class NetworkTranslator {
	private NetworkEndpoint ne;

	// maybe it is good to pass a created network endpoint instance here not sure
	// yet
	public NetworkTranslator(String gameURL, String gameID) {
		ne = new NetworkEndpoint(gameURL, new UniqueGameIdentifier(gameID));
	}

	public void registerPlayer(String firstName, String lastName, String id) {
		ne.registerPlayer(new PlayerRegistration(firstName, lastName, id));
	}
}
