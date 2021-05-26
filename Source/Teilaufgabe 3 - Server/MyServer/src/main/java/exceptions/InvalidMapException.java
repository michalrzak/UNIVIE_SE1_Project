package exceptions;

public class InvalidMapException extends GenericExampleException {

	public InvalidMapException(String errorMessage) {
		super("InvalidMapException", errorMessage);
	}

}
