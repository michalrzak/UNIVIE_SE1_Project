package gamedata.map;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import exceptions.TooManyHalfMapsReceived;

public class MapController {

	final static private int MAX_HALF_MAPS = 2;

	final private Set<HalfMapData> halfMaps = new HashSet<>();

	private Optional<FullMapData> fullMap = Optional.empty();

	public void receiveHalfMap(HalfMapData hmData) {
		if (halfMaps.size() >= MAX_HALF_MAPS) {
			throw new TooManyHalfMapsReceived("The given game has already received all halfmaps");
		}

		halfMaps.add(hmData);

		if (halfMaps.size() == MAX_HALF_MAPS) {
			generateFullMap();
		}
	}

	private void generateFullMap() {
		// TODO: add real logic
		fullMap = Optional.of(new FullMapData());
		return;
	}

}
