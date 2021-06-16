package game.map;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.TooManyHalfMapsReceived;
import game.map.helpers.EMoveEvent;
import game.move.helpers.SPlayerMove;
import game.player.helpers.SUniquePlayerIdentifier;
import game.propertychange.IRegisterForEvent;
import game.propertychange.PropertyChangeListener;
import game.propertychange.PropertyChangeSupport;

public class MapController {

	private Optional<SHalfMap> hmdata1 = Optional.empty();
	private Optional<SHalfMap> hmdata2 = Optional.empty();
	private Optional<SFullMap> fullMap = Optional.empty();

	private final PropertyChangeSupport<Void> mapReady = new PropertyChangeSupport<>();
	private final PropertyChangeSupport<IMapAccesser> fullMapConstructed = new PropertyChangeSupport<>();
	private final PropertyChangeSupport<SUniquePlayerIdentifier> playerCollectedTreasure = new PropertyChangeSupport<>();
	private final PropertyChangeSupport<SUniquePlayerIdentifier> playerSteppedOnCastle = new PropertyChangeSupport<>();

	private static Logger logger = LoggerFactory.getLogger(MapController.class);

	public void registerToMoveController(IRegisterForEvent<SPlayerMove> playerMoveEvent) {
		playerMoveEvent.register(move -> movePlayer(move));
	}

	public void registerListenForMapReady(PropertyChangeListener<Void> listener) {
		mapReady.register(listener);
	}

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

	private void movePlayer(SPlayerMove move) {
		assert (fullMap.isPresent());

		EMoveEvent event = fullMap.get().movePlayer(move, move.getMove());
		switch (event) {
		case COLLECTED_TREASURE:
			playerCollectedTreasure.fire(move);
		case STEPPED_ON_CASTLE:
			playerSteppedOnCastle.fire(move);
		}

	}

	private void generateFullMap() {
		logger.debug("generating full map");
		fullMap = Optional.of(SFullMap.generateRandomMap(hmdata1.get(), hmdata2.get()));
		mapReady.fire();
		fullMapConstructed.fire(fullMap.get());
	}

	public Optional<IMapAccesser> getFullMap() {
		// cannot simply return full map as I need to convert between SFullMap and
		// ISFullMapAccesser
		if (fullMap.isEmpty() && hmdata1.isEmpty()) {
			return Optional.empty();
		}

		if (fullMap.isEmpty() && hmdata1.isPresent()) {
			return Optional.of(hmdata1.get());
		}

		return Optional.of(fullMap.get());
	}

	public IRegisterForEvent<IMapAccesser> rergisterForFullMap() {
		return fullMapConstructed;
	}

	public IRegisterForEvent<SUniquePlayerIdentifier> rergisterForTreassureCollected() {
		return playerCollectedTreasure;
	}

	public IRegisterForEvent<SUniquePlayerIdentifier> rergisterForSteppedOnCastle() {
		return playerSteppedOnCastle;
	}
}
