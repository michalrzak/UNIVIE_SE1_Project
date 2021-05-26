package network;

import MessagesBase.UniqueGameIdentifier;
import games.helpers.ServerUniqueGameIdentifier;

public class NetworkTranslator {

	public ServerUniqueGameIdentifier networkGameIDToInternal(UniqueGameIdentifier gameID) {
		return new ServerUniqueGameIdentifier();
	}

	public UniqueGameIdentifier internalGameIDToNetwork(ServerUniqueGameIdentifier gameID) {
		return new UniqueGameIdentifier(gameID.getIDAsString());
	}

}
