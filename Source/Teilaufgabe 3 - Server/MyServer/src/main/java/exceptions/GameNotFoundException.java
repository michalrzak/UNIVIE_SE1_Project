package exceptions;

public class GameNotFoundException extends GenericExampleException {

	public GameNotFoundException(String errorMessage) {
		super("GameNotFoundException", errorMessage);
	}

}
