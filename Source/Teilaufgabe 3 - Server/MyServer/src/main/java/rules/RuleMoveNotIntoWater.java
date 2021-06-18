package rules;

import java.util.Optional;

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
import game.map.helpers.ESTerrain;
import game.map.helpers.Position;
import game.player.helpers.SUniquePlayerIdentifier;
import network.translation.NetworkTranslator;

public class RuleMoveNotIntoWater implements IRules {

	private final IGameControllerAccesser games;

	public RuleMoveNotIntoWater(IGameControllerAccesser games) {
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
		// assume move is not out of bounds
		// assume gameID is validated
		// assume playerID is validated
		NetworkTranslator translate = new NetworkTranslator();
		SUniqueGameIdentifier serverGameID = translate.networkGameIDToInternal(gameID);
		SUniquePlayerIdentifier serverPlayerID = translate.networkPlayerIDToInternal(playerMove);

		Optional<IMapAccesser> mapOpt = games.getMapAccesser(serverGameID);

		// assume it has been validated that the map is ready
		assert (mapOpt.isPresent());
		IMapAccesser map = mapOpt.get();

		if (getTerrainInDirPlayer(map, playerMove.getMove(), serverPlayerID) == ESTerrain.WATER) {
			throw new InvalidMoveException("The player tried to move into water");
		}

	}

	private ESTerrain getTerrainInDirPlayer(IMapAccesser map, EMove dir, SUniquePlayerIdentifier playerID) {
		Position playerPos = map.getPlayerPosition(playerID);

		Optional<Position> posInDirOpt = Optional.empty();

		switch (dir) {
		case Up:
			posInDirOpt = Optional.of(playerPos.addOffset(0, -1));
			break;
		case Down:
			posInDirOpt = Optional.of(playerPos.addOffset(0, 1));
			break;
		case Left:
			posInDirOpt = Optional.of(playerPos.addOffset(-1, 0));
			break;
		case Right:
			posInDirOpt = Optional.of(playerPos.addOffset(1, 0));
			break;
		}
		assert (posInDirOpt.isPresent());
		Position posInDir = posInDirOpt.get();

		return map.getTerrainAt(posInDir);

	}

}
