package rules;

import java.util.HashSet;
import java.util.Set;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;
import game.map.helpers.Position;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapDimensions implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		if (halfmapNodes.size() != EHalfMapHelpers.HEIGHT.get() * EHalfMapHelpers.WIDTH.get()) {
			throw new InvalidMapException("The received halfmap did not have the right number of nodes!");
		}

		Set<Position> positions = new HashSet<>();

		for (HalfMapNode node : halfmapNodes) {
			positions.add(new Position(node.getX(), node.getY()));
		}

		for (int y = 0; y < EHalfMapHelpers.HEIGHT.get(); ++y) {
			for (int x = 0; x < EHalfMapHelpers.WIDTH.get(); ++x) {
				if (!positions.contains(new Position(x, y))) {
					throw new InvalidMapException("The map did not contain the correct node coords.");
				}
			}
		}

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// TODO Auto-generated method stub

	}

}
