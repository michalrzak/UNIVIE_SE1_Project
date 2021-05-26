package exceptions;

public class InvalidDataException extends GenericExampleException {

	public InvalidDataException(String errorMessage) {
		super("InvalidDataException", errorMessage);
	}

}
