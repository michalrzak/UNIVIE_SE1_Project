package rules;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;

public class RuleHalfMapCastle implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		oneCastle(halfmap);
		castleAtGrass(halfmap);
	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

	private void oneCastle(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		long castleCount = halfmapNodes.stream().filter(node -> node.isFortPresent()).count();

		if (castleCount != 1) {
			throw new InvalidMapException("There was no or more then one castle present!");
		}
	}

	private void castleAtGrass(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		boolean castleAtGrass = halfmapNodes.stream().filter(node -> node.isFortPresent())
				.allMatch(node -> node.getTerrain() == ETerrain.Grass);

		if (!castleAtGrass) {
			throw new InvalidMapException("The castle was not placed on a grass field!");
		}
	}

}
