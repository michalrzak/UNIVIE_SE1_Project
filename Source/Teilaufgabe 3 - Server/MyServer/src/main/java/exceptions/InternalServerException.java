package exceptions;

public class InternalServerException extends GenericExampleException {

	public InternalServerException(String errorMessage) {
		super("InternalServerException", errorMessage);
	}

}
