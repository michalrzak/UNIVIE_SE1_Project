package getStateUnitTests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import exceptions.GenericExampleException;
import gamedata.game.GameController;
import gamedata.game.helpers.ServerUniqueGameIdentifier;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

class GetState_Tests {

	@Test
	void GetState_InvalidGameID_shouldThrowError() {
		GameController gc = new GameController();
		ServerUniqueGameIdentifier gameID = new ServerUniqueGameIdentifier("aaaaa");
		ServerUniquePlayerIdentifier playerID = new ServerUniquePlayerIdentifier("totaly-legit-playerID");

		Executable getStateInfo = () -> {
			gc.getGameInformation(gameID, playerID);
		};

		assertThrows(GenericExampleException.class, getStateInfo);
	}

	@Test
	void GetState_InvalidPlayerID_shouldThrowError() {
		GameController gc = new GameController();
		ServerUniqueGameIdentifier gameID = gc.createNewGame();
		ServerUniquePlayerIdentifier playerID = new ServerUniquePlayerIdentifier("totaly-legit-playerID");

		Executable getStateInfo = () -> {
			gc.getGameInformation(gameID, playerID);
		};

		assertThrows(GenericExampleException.class, getStateInfo);
	}

	/*
	 * @Test void GetState_InvalidPlayerID_shouldThrowError() { GameController gc =
	 * new GameController(); ServerUniqueGameIdentifier gameID = gc.createNewGame();
	 * ServerUniquePlayerIdentifier playerID = new
	 * ServerUniquePlayerIdentifier("totaly-legit-playerID");
	 * 
	 * Executable getStateInfo = () -> { gc.getGameInformation(gameID, playerID); };
	 * 
	 * assertThrows(GenericExampleException.class, getStateInfo); }
	 */

}
