package ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fullMap.FullMapData;
import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class CLI implements PropertyChangeListener {

	private static Logger logger = LoggerFactory.getLogger(CLI.class);

	// Maybe just save a FullMap instance? Not sure what to do here.
	HashMap<EGameEntity, Position> gameEntities;
	HashMap<Position, ETerrain> terrain;
	int width;
	int height;

	public CLI(FullMapData fm) {
		fm.addListener(this);

		// this may need to get changed I am not sure this method is the best idea
		terrain = fm.getTerrain();
		gameEntities = fm.getGameEntities();

		width = fm.getWidth();
		height = fm.getHeight();
	}

	public void printData() {

		List<List<Character>> printing = new ArrayList<>();

		for (int y = 0; y < height; ++y) {
			printing.add(new ArrayList<>());
			for (int x = 0; x < width; ++x) {

				switch (terrain.get(new Position(x, y))) {
				case GRASS:
					printing.get(printing.size() - 1).add('#');
					break;
				case MOUNTAIN:
					printing.get(printing.size() - 1).add('A');
					break;
				case WATER:
					printing.get(printing.size() - 1).add('~');
					break;
				}
			}
		}

		gameEntities.entrySet().stream().forEach(ele -> {
			switch (ele.getKey()) {
			case MYPLAYER:
				printing.get(ele.getValue().getx()).set(ele.getValue().gety(), '1');
				break;

			case ENEMYPLAYER:
				printing.get(ele.getValue().getx()).set(ele.getValue().gety(), '2');
				break;

			case ENEMYCASTLE:
			case MYCASTLE:
				printing.get(ele.getValue().getx()).set(ele.getValue().gety(), 'M');
				break;

			case MYTREASURE:
				printing.get(ele.getValue().getx()).set(ele.getValue().gety(), '=');
				break;
			}
			// printing.get(ele.getValue().gety()).get(ele.getValue().getx()) = ;
		});

		// TODO: print the results

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub

		Object received = event.getNewValue();

		switch (event.getPropertyName()) {

		case "gameEntities":
			if (!(received instanceof HashMap<?, ?>)) {
				logger.error(
						"The event with name 'gameEntities' was triggered but the received object was not of type HashMap<EGameEntity, Position>");
				throw new RuntimeException(
						"The event with name 'gameEntities' was triggered but the received object was not of type HashMap<EGameEntity, Position>");
			}

			gameEntities = (HashMap<EGameEntity, Position>) received;
			break;

		default:
			logger.error("An unrecognised propertyChange occured. Name: " + event.getPropertyName());
			throw new RuntimeException("Unrecogniszed property change! Change was: " + event.getPropertyName());
		}

		// TODO: add actuall printing
	}

}
