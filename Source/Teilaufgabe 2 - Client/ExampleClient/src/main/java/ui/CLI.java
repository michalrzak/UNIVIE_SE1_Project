package ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fullMap.FullMapData;
import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class CLI implements PropertyChangeListener {

	// TODO: re think how this class works!

	private static Logger logger = LoggerFactory.getLogger(CLI.class);

	// Maybe just save a FullMap instance? Not sure what to do here.
	private Map<EGameEntity, Position> gameEntities;
	private final List<List<Character>> terrain;
	private boolean treasureCollected;

	public CLI(FullMapData fm) {
		fm.addListener(this);

		// this may need to get changed I am not sure this method is the best idea
		terrain = hashMapToListList(fm.getTerrain(), fm.getWidth(), fm.getHeight());
		gameEntities = fm.getGameEntities();
		treasureCollected = fm.getTreasureCollected();

		// maybe remove this and only print after the first move?
		printData();
	}

	private static List<List<Character>> hashMapToListList(Map<Position, ETerrain> map, int width, int height) {
		List<List<Character>> ret = new ArrayList<>();

		for (int y = 0; y < height; ++y) {
			ret.add(new ArrayList<>());
			for (int x = 0; x < width; ++x) {

				switch (map.get(new Position(x, y))) {
				case GRASS:
					ret.get(ret.size() - 1).add('#');
					break;
				case MOUNTAIN:
					ret.get(ret.size() - 1).add('A');
					break;
				case WATER:
					ret.get(ret.size() - 1).add('~');
					break;
				}
			}
		}

		return ret;
	}

	private List<List<Character>> assignedGameEntities() {
		List<List<Character>> ret = terrain.stream().map(row -> new ArrayList<>(row)).collect(Collectors.toList());
		gameEntities.entrySet().stream().forEach(ele -> {
			switch (ele.getKey()) {

			case ENEMYCASTLE:
			case MYCASTLE:
				ret.get(ele.getValue().gety()).set(ele.getValue().getx(), 'M');
				break;

			case MYTREASURE:
				ret.get(ele.getValue().gety()).set(ele.getValue().getx(), '=');
				break;

			case MYPLAYER:
				ret.get(ele.getValue().gety()).set(ele.getValue().getx(), '1');
				break;

			case ENEMYPLAYER:
				ret.get(ele.getValue().gety()).set(ele.getValue().getx(), '2');
				break;
			}
		});

		return ret;
	}

	public void printData() {

		List<List<Character>> printing = assignedGameEntities();

		for (var y : printing) {
			for (var x : y) {
				System.out.print(x);
			}
			System.out.print('\n');
		}
		System.out.println("Your trerasure state: " + (treasureCollected ? "collected" : "not collected"));
		System.out.println('\n');

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub

		Object received = event.getNewValue();

		switch (event.getPropertyName()) {

		case "gameEntities":
			if (!(received instanceof Map<?, ?>)) {
				logger.error(
						"The event with name 'gameEntities' was triggered but the received object was not of type Map<EGameEntity, Position>");
				throw new RuntimeException(
						"The event with name 'gameEntities' was triggered but the received object was not of type Map<EGameEntity, Position>");
			}

			gameEntities = (Map<EGameEntity, Position>) received;

			// if new gameEntities are received (the player position among them, reprint the
			// map
			printData();
			break;

		case "treasureCollected":
			if (!(received instanceof Boolean)) {
				logger.error(
						"The event with name 'treasureCollected' was triggered but the received object was not of type Boolean");
				throw new RuntimeException(
						"The event with name 'treasureCollected' was triggered but the received object was not of type Boolean");
			}

			treasureCollected = (Boolean) received;
			break;

		default:
			logger.error("An unrecognised propertyChange occured. Name: " + event.getPropertyName());
			throw new RuntimeException("Unrecogniszed property change! Change was: " + event.getPropertyName());
		}
	}

}
