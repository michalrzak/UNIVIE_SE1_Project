package exceptions;

public class InvalidMoveException extends GenericExampleException {

	public InvalidMoveException(String errorMessage) {
		super("InvalidMoveException", errorMessage);
	}

}
