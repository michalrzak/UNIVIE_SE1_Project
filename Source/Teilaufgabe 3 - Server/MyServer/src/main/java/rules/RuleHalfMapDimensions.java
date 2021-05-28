package rules;

import java.util.HashSet;
import java.util.Set;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import exceptions.InvalidMapException;
import gamedata.map.helpers.Position;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapDimensions implements IRules {

	@Override
	public void validateHalfMap(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		if (halfmapNodes.size() != EHalfMapHelpers.HALF_MAP_HEIGHT.get() * EHalfMapHelpers.HALF_MAP_WIDTH.get()) {
			throw new InvalidMapException("The received halfmap did not have the right number of nodes!");
		}

		Set<Position> positions = new HashSet<>();

		for (HalfMapNode node : halfmapNodes) {
			positions.add(new Position(node.getX(), node.getY()));
		}

		for (int y = 0; y < EHalfMapHelpers.HALF_MAP_HEIGHT.get(); ++y) {
			for (int x = 0; x < EHalfMapHelpers.HALF_MAP_WIDTH.get(); ++x) {
				if (!positions.contains(new Position(x, y))) {
					throw new InvalidMapException("The map did not contain the correct node coords.");
				}
			}
		}

	}

}
