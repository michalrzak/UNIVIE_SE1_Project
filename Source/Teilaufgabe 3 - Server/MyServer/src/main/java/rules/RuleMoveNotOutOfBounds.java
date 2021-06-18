package rules;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.EMove;
import MessagesBase.HalfMap;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import exceptions.InvalidMoveException;
import game.IGameControllerAccesser;
import game.helpers.SUniqueGameIdentifier;
import game.map.IMapAccesser;
import game.map.helpers.EMapType;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;
import network.translation.NetworkTranslator;

public class RuleMoveNotOutOfBounds implements IRules {

	private final IGameControllerAccesser games;

	private static Logger logger = LoggerFactory.getLogger(RuleMoveNotOutOfBounds.class);

	public RuleMoveNotOutOfBounds(IGameControllerAccesser games) {
		this.games = games;
	}

	@Override
	public void validateNewPlayer(UniqueGameIdentifier gameID, PlayerRegistration playerRegistration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateNewHalfMap(UniqueGameIdentifier gameID, HalfMap halfmap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateGetGameState(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateReceiveMove(UniqueGameIdentifier gameID, PlayerMove playerMove) {
		// assume gameID is validated
		// assume playerID is validated

		NetworkTranslator translate = new NetworkTranslator();
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		SUniquePlayerIdentifier serverPlayerID = translate.networkPlayerIDToInternal(playerMove);

		// assume both half maps are already sent
		Optional<IMapAccesser> mapOpt = games.getMapAccesser(serverGameID);
		assert (mapOpt.isPresent());
		IMapAccesser map = mapOpt.get();

		Position playerPos = map.getPlayerPosition(serverPlayerID);

		// if this throws that means the Position was negative
		try {
			playerPos = positionInDirection(playerPos, playerMove.getMove());
		} catch (IllegalArgumentException e) {
			System.out.println(String.format("Player Position was: %s, the direction was: %s", playerPos.toString(),
					playerMove.getMove().toString()));
			throw new InvalidMoveException("You tried to move out of the map bounds1!");
		}

		EMapType mapType = map.getMapType();

		if (playerPos.getx() >= mapType.getWidth() || playerPos.gety() >= mapType.getHeight()) {
			throw new InvalidMoveException("You tried to move out of the map bounds!");
		}
	}

	private Position positionInDirection(Position pos, EMove dir) {
		Optional<Position> posInDirOpt = Optional.empty();

		switch (dir) {
		case Up:
			posInDirOpt = Optional.of(pos.addOffset(0, -1));
			break;
		case Down:
			posInDirOpt = Optional.of(pos.addOffset(0, 1));
			break;
		case Left:
			posInDirOpt = Optional.of(pos.addOffset(-1, 0));
			break;
		case Right:
			posInDirOpt = Optional.of(pos.addOffset(1, 0));
			break;
		}
		assert (posInDirOpt.isPresent());
		Position posInDir = posInDirOpt.get();

		return posInDir;
	}

}
