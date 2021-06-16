package network.translation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import MessagesBase.ETerrain;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import exceptions.InternalServerException;
import game.SGameState;
import game.map.helpers.ESTerrain;
import game.map.helpers.FullMapState;
import game.map.helpers.Position;
import game.player.IPlayerAccesser;
import game.player.helpers.ESPlayerGameState;

public class GameStateExtractor {

	protected GameState extractGameState(SGameState gameState) {
		Collection<PlayerState> ps = new ArrayList<>();

		ps.add(extractPlayerState(gameState.getOwnerPlayer(), gameState.getOwnerPlayerGameState()));

		Optional<IPlayerAccesser> otherPlayer = gameState.getOtherPlayer();
		if (otherPlayer.isPresent()) {
			ps.add(extractPlayerState(otherPlayer.get(), gameState.getOtherPlayerGameState().get()));
		}

		Optional<FullMapState> fullMapState = gameState.getFullMap();

		if (fullMapState.isEmpty()) {
			return new GameState(ps, generateGameStateID(gameState));
		}

		return new GameState(Optional.of(extractFullMap(fullMapState.get())), ps, generateGameStateID(gameState));
	}

	private PlayerState extractPlayerState(IPlayerAccesser player, ESPlayerGameState playerState) {

		EPlayerGameState gameState = translatePlayerState(playerState);

		NetworkTranslator nt = new NetworkTranslator();

		UniquePlayerIdentifier playerID = nt.internalPlayerIDToNetwork(player.getPlayerID());

		PlayerState ret = new PlayerState(player.getFirstName(), player.getLastName(), player.getStudentID(), gameState,
				playerID, player.getCollectedTreasure());

		return ret;
	}

	private FullMap extractFullMap(FullMapState fullMapState) {
		NetworkTranslator nt = new NetworkTranslator();

		var positionToTerrainMap = fullMapState.getTerrain();

		Set<FullMapNode> mapNodes = new HashSet<>();
		for (var positionTerrainPair : positionToTerrainMap.entrySet()) {
			ETerrain terrain = internalTerrainToNetwork(positionTerrainPair.getValue());

			Position currentPos = positionTerrainPair.getKey();

			EPlayerPositionState playerPositionState = EPlayerPositionState.NoPlayerPresent;
			if (fullMapState.getOwnerPosition().equals(currentPos)) {
				playerPositionState = EPlayerPositionState.MyPosition;
			}
			if (fullMapState.getOtherPosition().equals(currentPos)) {
				if (playerPositionState == EPlayerPositionState.MyPosition) {
					playerPositionState = EPlayerPositionState.BothPlayerPosition;
				} else {
					playerPositionState = EPlayerPositionState.EnemyPlayerPosition;
				}
			}

			ETreasureState treasureState = ETreasureState.NoOrUnknownTreasureState;
			// fullmap.getTreasurePosition() returns an optional
			if (fullMapState.getOwnerTreasure().isPresent()
					&& fullMapState.getOwnerTreasure().get().equals(currentPos)) {
				treasureState = ETreasureState.MyTreasureIsPresent;
			}

			EFortState fortState = EFortState.NoOrUnknownFortState;
			if (fullMapState.getOwnerCastle().equals(currentPos)) {
				fortState = EFortState.MyFortPresent;
			}
			if (fullMapState.getOtherCastle().isPresent() && fullMapState.getOtherCastle().get().equals(currentPos)) {
				fortState = EFortState.EnemyFortPresent;
			}

			mapNodes.add(new FullMapNode(terrain, playerPositionState, treasureState, fortState, currentPos.getx(),
					currentPos.gety()));

		}

		return new FullMap(mapNodes);
	}

	private String generateGameStateID(SGameState gameState) {
		return Integer.toString(gameState.getTurn());
	}

	private EPlayerGameState translatePlayerState(ESPlayerGameState playerState) {
		switch (playerState) {
		case WON:
			return EPlayerGameState.Won;

		case LOST:
			return EPlayerGameState.Lost;

		case SHOULD_ACT_NEXT:
			return EPlayerGameState.ShouldActNext;

		case SHOULD_WAIT:
			return EPlayerGameState.ShouldWait;
		}

		throw new InternalServerException(
				"Sorry, but something went wrong on the server. (Player game state conversion)");
	}

	public ETerrain internalTerrainToNetwork(ESTerrain terrain) {
		switch (terrain) {
		case GRASS:
			return ETerrain.Grass;
		case MOUNTAIN:
			return ETerrain.Mountain;
		case WATER:
			return ETerrain.Water;
		}

		throw new InternalServerException(
				"Sorry, but something went wrong on the server. (Internal Terrain conversion)");
	}
}
