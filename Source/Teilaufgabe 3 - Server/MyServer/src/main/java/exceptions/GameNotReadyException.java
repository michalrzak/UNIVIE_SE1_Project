package exceptions;

public class GameNotReadyException extends GenericExampleException {

	public GameNotReadyException(String errorMessage) {
		super("GameNotReadyException", errorMessage);
	}

}
