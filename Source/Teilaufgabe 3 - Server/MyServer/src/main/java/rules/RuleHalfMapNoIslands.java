package rules;

import java.util.Map;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import exceptions.InvalidMapException;
import game.map.helpers.Position;
import network.translation.NetworkHalfMapTranslator;

public class RuleHalfMapNoIslands implements IRules {

	@Override
	public void validateHalfMap(HalfMap halfmap) {
		// extract map from halfmap
		NetworkHalfMapTranslator halfMapTrans = new NetworkHalfMapTranslator();
		Map<Position, ETerrain> positionMap = halfMapTrans.extractNetorkHalfMapTerrainMap(halfmap);

		// start floodfill from 0, 0
		floodfill(new Position(0, 0), positionMap);

		// map contains islands if it is not empty and at least one node inside is not
		// water
		if (positionMap.size() != 0 && !positionMap.values().stream().allMatch(ele -> ele == ETerrain.Water)) {
			throw new InvalidMapException("The half map contains an island!");
		}
	}

	private void floodfill(Position node, Map<Position, ETerrain> nodes) {
		// THE HASHMAP PASSES AS ARGUMENT HERE WILL BE MODIFIED. PROVIDE A COPY
		// THE START NODE CANNOT BE WATER
		if (!nodes.containsKey(node) || nodes.get(node) == ETerrain.Water)
			return;

		nodes.remove(node);

		// north
		if (node.gety() - 1 >= 0)
			floodfill(new Position(node.getx(), node.gety() - 1), nodes);

		floodfill(new Position(node.getx(), node.gety() + 1), nodes);
		// east
		if (node.getx() - 1 >= 0)
			floodfill(new Position(node.getx() - 1, node.gety()), nodes);
		// west
		floodfill(new Position(node.getx() + 1, node.gety()), nodes);
	}

}
