package rules;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import exceptions.InvalidMapException;
import rules.helpers.EHalfMapHelpers;

public class RuleHalfMapTerrainCount implements IRules {

	@Override
	public void validateHalfMap(HalfMap halfmap) {
		var halfmapNodes = halfmap.getNodes();

		long grass = halfmapNodes.stream().map(node -> node.getTerrain()).filter(ele -> ele == ETerrain.Grass).count();
		long water = halfmapNodes.stream().map(node -> node.getTerrain()).filter(ele -> ele == ETerrain.Water).count();
		long mountain = halfmapNodes.stream().map(node -> node.getTerrain()).filter(ele -> ele == ETerrain.Mountain)
				.count();

		if (grass < EHalfMapHelpers.MIN_GRASS_COUNT.get() || water < EHalfMapHelpers.MIN_WATER_COUNT.get()
				|| mountain < EHalfMapHelpers.MIN_MOUNTAIN_COUNT.get()) {
			throw new InvalidMapException("Invalid minimum count of fields!");
		}

	}

}
