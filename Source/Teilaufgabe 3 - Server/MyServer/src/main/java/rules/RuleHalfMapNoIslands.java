package rules;

import java.util.Map;
import java.util.Optional;

import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidMapException;
import game.map.helpers.Position;
import network.translation.NetworkHalfMapTranslator;

public class RuleHalfMapNoIslands implements IRules {

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		// extract map from halfmap
		NetworkHalfMapTranslator halfMapTrans = new NetworkHalfMapTranslator();
		Map<Position, ETerrain> positionMap = halfMapTrans.extractNetorkHalfMapTerrainMap(halfmap);

		// find a start node (the first it finds with grass on it)
		Optional<Position> startOpt = Optional.empty();
		for (var entry : positionMap.entrySet()) {
			if (entry.getValue() == ETerrain.Grass) {
				startOpt = Optional.of(entry.getKey());
				break;
			}
		}

		if (startOpt.isEmpty()) {
			throw new InvalidMapException("The map did not contain any grass nodes!");
		}

		Position start = startOpt.get();

		while (positionMap.get(start) == ETerrain.Water) {
			start = new Position(start.getx() + 1, start.gety());
		}

		floodfill(start, positionMap);

		// map contains islands if it is not empty and at least one node inside is not
		// water
		if (positionMap.size() != 0 && !positionMap.values().stream().allMatch(ele -> ele == ETerrain.Water)) {
			positionMap.entrySet().stream()
					.forEach(ele -> System.out.println(ele.getKey().toString() + " " + ele.getValue().toString()));
			throw new InvalidMapException("The half map contains an island!");
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
