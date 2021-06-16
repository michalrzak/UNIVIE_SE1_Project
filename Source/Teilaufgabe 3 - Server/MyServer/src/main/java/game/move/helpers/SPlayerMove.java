package game.move.helpers;

import game.player.helpers.SUniquePlayerIdentifier;

public class SPlayerMove extends SUniquePlayerIdentifier {

	private final ESMove move;

	public SPlayerMove(String playerID, ESMove move) {
		super(playerID);
		this.move = move;
	}

	public SPlayerMove(SUniquePlayerIdentifier playerID, ESMove move) {
		this(playerID.asString(), move);
	}

	public ESMove getMove() {
		return move;
	}

}
