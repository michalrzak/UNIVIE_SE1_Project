package exceptions;

public class InvalidHalfMapGeneratedException extends Exception {

	public InvalidHalfMapGeneratedException() {
		super("HalfMapGenerator failed, please try the generation again!");
	}

}
