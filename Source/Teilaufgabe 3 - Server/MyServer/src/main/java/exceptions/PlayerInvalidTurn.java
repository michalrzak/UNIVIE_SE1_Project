package exceptions;

public class PlayerInvalidTurn extends GenericExampleException {

	public PlayerInvalidTurn(String errorMessage) {
		super("PlayerInvalidTurn", errorMessage);
	}

}
