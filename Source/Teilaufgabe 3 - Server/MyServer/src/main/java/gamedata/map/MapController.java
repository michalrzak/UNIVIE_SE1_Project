package gamedata.map;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.TooManyHalfMapsReceived;

public class MapController {

	private Optional<HalfMapData> hmdata1 = Optional.empty();
	private Optional<HalfMapData> hmdata2 = Optional.empty();

	private Optional<FullMapData> fullMap = Optional.empty();

	private static Logger logger = LoggerFactory.getLogger(MapController.class);

	public void receiveHalfMap(HalfMapData hmData) {
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
		fullMap = Optional.of(new FullMapData(hmdata1.get(), hmdata2.get()));
	}

	public Optional<FullMapData> getFullMap() {
		return fullMap;
	}

}
