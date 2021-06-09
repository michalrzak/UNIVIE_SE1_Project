package network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import gamedata.SGameState;
import gamedata.map.FullMapState;
import gamedata.map.helpers.Position;
import gamedata.player.IPlayerAccesser;
import gamedata.player.helpers.ESPlayerGameState;

public class GameStateExtractor {

	public GameState extractGameState(SGameState gameState) {
		Collection<PlayerState> ps = new ArrayList<>();

		ps.add(extractPlayerState(gameState.getOwnerPlayer(), gameState.getOwnerPlayerGameState()));
		ps.add(extractPlayerState(gameState.getOtherPlayer(), gameState.getOtherPlayerGameState()));

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
			MessagesBase.ETerrain terrain = nt.internalTerrainToNetwork(positionTerrainPair.getValue());

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
}
