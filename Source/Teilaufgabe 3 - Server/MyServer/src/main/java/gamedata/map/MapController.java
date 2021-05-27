package gamedata.map;

import java.util.Optional;

import exceptions.GameNotReadyException;
import exceptions.TooManyHalfMapsReceived;

public class MapController {

	final private static int MAX_HALF_MAPS = 2;

	private Optional<HalfMapData> hmdata1 = Optional.empty();
	private Optional<HalfMapData> hmdata2 = Optional.empty();

	private Optional<FullMapData> fullMap = Optional.empty();

	public void receiveHalfMap(HalfMapData hmData) {
		if (hmdata1.isPresent() && hmdata2.isPresent()) {
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
		fullMap = Optional.of(new FullMapData(hmdata1.get(), hmdata2.get()));
	}

	public FullMapData getFullMap() {
		if (fullMap.isPresent()) {
			throw new GameNotReadyException("The game has not received both half maps yet");
		}
		return fullMap.get();
	}

}
