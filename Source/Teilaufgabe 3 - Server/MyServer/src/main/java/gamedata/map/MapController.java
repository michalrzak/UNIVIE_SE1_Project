package gamedata.map;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.TooManyHalfMapsReceived;

public class MapController {

	private Optional<SHalfMap> hmdata1 = Optional.empty();
	private Optional<SHalfMap> hmdata2 = Optional.empty();

	private Optional<SFullMap> fullMap = Optional.empty();

	private static Logger logger = LoggerFactory.getLogger(MapController.class);

	public void receiveHalfMap(SHalfMap hmData) {
		if (hmdata1.isPresent() && hmdata2.isPresent()) {
			logger.warn("Tried adding a third halfmap");
			throw new TooManyHalfMapsReceived("The given game has already received 2 halfmaps");
		}

		if (hmdata1.isEmpty()) {
			hmdata1 = Optional.of(hmData);
		} else {
			hmdata2 = Optional.of(hmData);
		}

		if (hmdata2.isPresent()) {
			generateFullMap();
		}
	}

	private void generateFullMap() {
		logger.debug("generating full map");
		fullMap = Optional.of(SFullMap.generateRandomMap(hmdata1.get(), hmdata2.get()));
	}

	public Optional<ISFullMapAccesser> getFullMap() {
		// cannot simply return full map as I need to convert between SFullMap and
		// ISFullMapAccesser
		if (fullMap.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(fullMap.get());
	}

}
