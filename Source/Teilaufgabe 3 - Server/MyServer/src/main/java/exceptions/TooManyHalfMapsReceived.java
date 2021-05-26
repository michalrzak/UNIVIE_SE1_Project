package exceptions;

public class TooManyHalfMapsReceived extends GenericExampleException {

	public TooManyHalfMapsReceived(String errorMessage) {
		super("TooManyHalfMapsReceived", errorMessage);
	}

}
