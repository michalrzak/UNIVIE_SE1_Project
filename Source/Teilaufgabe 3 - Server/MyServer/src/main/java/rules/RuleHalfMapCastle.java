package rules;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import exceptions.InvalidMapException;

public class RuleHalfMapCastle implements IRules {

	@Override
	public void validateHalfMap(HalfMap halfmap) {
		oneCastle(halfmap);
		castleAtGrass(halfmap);
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
