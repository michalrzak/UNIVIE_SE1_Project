package rules;

import java.util.Map;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import exceptions.InvalidMapException;
import gamedata.map.helpers.Position;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapEdges implements IRules {

	@Override
	public void validateHalfMap(HalfMap halfmap) {
		Map<Position, ETerrain> positionMap = EHalfMapHelpers.extractMap(halfmap);

		// check if half of the edge is made up of water

		var mapset = positionMap.entrySet();

		long leftEdgeCount = mapset.stream().filter(ele -> ele.getKey().getx() == 0 && ele.getValue() == ETerrain.Water)
				.count();
		long rightEdgeCount = mapset.stream()
				.filter(ele -> ele.getKey().getx() == EHalfMapHelpers.HALF_MAP_WIDTH.get() - 1
						&& ele.getValue() == ETerrain.Water)
				.count();
		long topEdgeCount = mapset.stream().filter(ele -> ele.getKey().gety() == 0 && ele.getValue() == ETerrain.Water)
				.count();
		long botEdgeCount = mapset.stream()
				.filter(ele -> ele.getKey().gety() == EHalfMapHelpers.HALF_MAP_HEIGHT.get() - 1
						&& ele.getValue() == ETerrain.Water)
				.count();

		final int SHORTEDGE_MAX = EHalfMapHelpers.HALF_MAP_HEIGHT.get() / 2;
		final int LONGEDGE_MAX = EHalfMapHelpers.HALF_MAP_WIDTH.get() / 2;

		if (leftEdgeCount >= SHORTEDGE_MAX || rightEdgeCount >= SHORTEDGE_MAX || topEdgeCount >= LONGEDGE_MAX
				|| botEdgeCount >= LONGEDGE_MAX) {
			throw new InvalidMapException("The edges contained too much water!");
		}

	}

}
