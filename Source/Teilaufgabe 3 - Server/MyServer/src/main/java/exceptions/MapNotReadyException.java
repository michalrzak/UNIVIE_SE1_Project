package exceptions;

public class MapNotReadyException extends GenericExampleException {

	public MapNotReadyException(String errorMessage) {
		super("MapNotReadyException", errorMessage);
	}

}
