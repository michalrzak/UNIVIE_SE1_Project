package game.move;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.map.IMapAccesser;
import game.map.helpers.ESTerrain;
import game.map.helpers.Position;
import game.move.helpers.ESMove;
import game.move.helpers.SPlayerMove;
import game.player.helpers.SUniquePlayerIdentifier;
import game.propertychange.IRegisterForEvent;
import game.propertychange.PropertyChangeSupport;

public class MoveController {
	private final Map<SUniquePlayerIdentifier, ESMove> moveDirection = new HashMap<>();
	private final Map<SUniquePlayerIdentifier, Integer> moveCommandsLeft = new HashMap<>();

	private Optional<IMapAccesser> finnishedFullMap = Optional.empty();

	private final PropertyChangeSupport<SPlayerMove> playerExecutedMove = new PropertyChangeSupport<>();

	private static Logger logger = LoggerFactory.getLogger(MoveController.class);

	public void listenToAllRequiredProperties(IRegisterForEvent<IMapAccesser> mapFinnished) {
		listenToMap(mapFinnished);
	}

	private void listenToMap(IRegisterForEvent<IMapAccesser> mapFinnished) {
		mapFinnished.register(fullmap -> receiveFullMap(fullmap));
	}

	public void move(SUniquePlayerIdentifier playerID, ESMove move) {
		assert (finnishedFullMap.isPresent());

		// if player is NOT already moving in the appropriate direction
		if (!moveDirection.containsKey(playerID) || moveDirection.get(playerID) != move) {
			// calculate how many moves the player needs to send
			int count = getDistance(playerID, move);

			logger.debug(String.format("A player with id %s started a move which will take %s turns",
					playerID.toString(), Integer.toString(count)));

			moveDirection.put(playerID, move);
			moveCommandsLeft.put(playerID, count);

		}

		// the player performed a command
		waitATurn(playerID);

		if (finnishedWaiting(playerID)) {
			finnishTurn(playerID);
		}
	}

	private int getDistance(SUniquePlayerIdentifier playerID, ESMove move) {
		Position playerPos = finnishedFullMap.get().getPlayerPosition(playerID);
		ESTerrain standingAt = finnishedFullMap.get().getTerrainAt(playerPos);
		ESTerrain next = finnishedFullMap.get().getTerrainAt(playerPos.addOffset(move.getXDiff(), move.getYDiff()));
		assert (next != ESTerrain.WATER);

		return standingAt.cost() + next.cost();
	}

	private void waitATurn(SUniquePlayerIdentifier playerID) {
		Integer newCount = moveCommandsLeft.get(playerID) - 1;
		moveCommandsLeft.put(playerID, newCount);
	}

	private void finnishTurn(SUniquePlayerIdentifier playerID) {
		SPlayerMove playerMove = new SPlayerMove(playerID, moveDirection.get(playerID));
		moveDirection.remove(playerMove);
		moveCommandsLeft.remove(playerMove);

		logger.debug(String.format("Player with id %s finnished a move", playerID.toString()));

		playerExecutedMove.fire(playerMove);
	}

	private boolean finnishedWaiting(SUniquePlayerIdentifier playerID) {
		return moveCommandsLeft.get(playerID) == 0;
	}

	public IRegisterForEvent<SPlayerMove> registerPlayerMove() {
		return playerExecutedMove;
	}

	private void receiveFullMap(IMapAccesser fullmap) {
		finnishedFullMap = Optional.of(fullmap);
	}

}
