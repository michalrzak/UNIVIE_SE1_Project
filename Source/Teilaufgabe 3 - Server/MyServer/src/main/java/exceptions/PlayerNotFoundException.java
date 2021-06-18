package exceptions;

public class PlayerNotFoundException extends GenericExampleException {

	public PlayerNotFoundException(String errorMessage) {
		super("PlayerNotFoundException", errorMessage);
	}

}
