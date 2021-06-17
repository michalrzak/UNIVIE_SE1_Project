package halfMapRulesUnitTests;

import java.util.Collection;
import java.util.HashSet;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;

public class Helper {
	public static HalfMap halfMapFromArray(char[][] nodes) {
		Collection<HalfMapNode> hmnodes = new HashSet<>();
		int x = 0;
		int y = 0;
		for (char[] row : nodes) {
			for (char element : row) {
				switch (element) {
				case 'g':
					hmnodes.add(new HalfMapNode(x, y, ETerrain.Grass));
					break;
				case 'w':
					hmnodes.add(new HalfMapNode(x, y, ETerrain.Water));
					break;
				case 'm':
					hmnodes.add(new HalfMapNode(x, y, ETerrain.Mountain));
					break;
				case 'G':
					hmnodes.add(new HalfMapNode(x, y, true, ETerrain.Grass));
					break;
				}
				++x;
			}
			x = 0;
			++y;
		}

		return new HalfMap("amazing-ID", hmnodes);
	}
}
