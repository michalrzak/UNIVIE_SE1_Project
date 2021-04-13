package ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fullMap.FullMapData;
import mapHelpers.EGameEntity;
import mapHelpers.Position;

public class CLI implements PropertyChangeListener {

	private static Logger logger = LoggerFactory.getLogger(CLI.class);

	HashMap<EGameEntity, Position> gameEntities;

	public CLI(FullMapData fm) {
		fm.addListener(this);
		// TODO: somehow extract initilally needed data
		// TODO: extract terrain
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
