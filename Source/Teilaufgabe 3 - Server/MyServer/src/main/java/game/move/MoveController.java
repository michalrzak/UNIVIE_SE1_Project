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

		if (!moveDirection.containsKey(playerID) || moveDirection.get(playerID) != move) {
			// calculate how many moves the player needs to send
			Position playerPos = finnishedFullMap.get().getPlayerPosition(playerID);
			ESTerrain standingAt = finnishedFullMap.get().getTerrainAt(playerPos);
			ESTerrain next = finnishedFullMap.get().getTerrainAt(playerPos.addOffset(move.getXDiff(), move.getYDiff()));
			assert (next != ESTerrain.WATER);
			int count = standingAt.cost() + next.cost();

			logger.debug(String.format("A player with id %s started a move which will take %s turns",
					playerID.getPlayerIDAsString(), Integer.toString(count)));

			moveDirection.put(playerID, move);
			moveCommandsLeft.put(playerID, count - 1);
		} else {
			Integer newCount = moveCommandsLeft.get(playerID) - 1;
			moveCommandsLeft.put(playerID, newCount);
		}

		if (moveCommandsLeft.get(playerID) == 0) {
			SPlayerMove playerMove = new SPlayerMove(playerID, move);
			moveDirection.remove(playerMove);
			moveCommandsLeft.remove(playerMove);

			logger.debug(String.format("Player with id %s finnished a move", playerID.getPlayerIDAsString()));

			playerExecutedMove.fire(playerMove);
		}
	}

	public IRegisterForEvent<SPlayerMove> registerPlayerMove() {
		return playerExecutedMove;
	}

	private void receiveFullMap(IMapAccesser fullmap) {
		finnishedFullMap = Optional.of(fullmap);
	}

}
