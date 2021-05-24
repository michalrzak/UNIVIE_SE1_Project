package server.exceptions;

public class TooManyPlayersRegistered extends GenericExampleException {

	public TooManyPlayersRegistered(String errorMessage) {
		super("TooManyPlayersRegistered", errorMessage);
	}

}
