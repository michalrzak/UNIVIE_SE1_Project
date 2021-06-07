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
import gamedata.game.IGameAccesser;
import gamedata.map.ISFullMapAccesser;
import gamedata.map.helpers.Position;
import gamedata.player.IPlayerAccesser;
import gamedata.player.helpers.ESPlayerGameState;
import gamedata.player.helpers.SUniquePlayerIdentifier;

public class GameStateExtractor {

	private final SUniquePlayerIdentifier me;
	private final SUniquePlayerIdentifier other;

	public GameStateExtractor(SUniquePlayerIdentifier me, SUniquePlayerIdentifier other) {
		this.me = me;
		this.other = other;
	}

	public GameState extractGameState(IGameAccesser game) {
		Collection<IPlayerAccesser> players = game.getPlayers();
		Collection<PlayerState> ps = new ArrayList<>();
		for (IPlayerAccesser iplayer : players) {
			ESPlayerGameState playerState = game.getPlayerState(iplayer.getPlayerID());
			ps.add(extractPlayerState(iplayer, playerState));
		}

		Optional<ISFullMapAccesser> fmd = game.getFullMap();

		if (fmd.isEmpty()) {
			return new GameState(ps, generateGameStateID());
		}

		return new GameState(Optional.of(extractFullMap(fmd.get())), ps, generateGameStateID());
	}

	private PlayerState extractPlayerState(IPlayerAccesser player, ESPlayerGameState playerState) {

		EPlayerGameState gameState = translatePlayerState(playerState);

		NetworkTranslator nt = new NetworkTranslator();

		UniquePlayerIdentifier playerID = nt.internalPlayerIDToNetwork(player.getPlayerID());

		PlayerState ret = new PlayerState(player.getFirstName(), player.getLastName(), player.getStudentID(), gameState,
				playerID, player.getCollectedTreasure());

		return ret;
	}

	private FullMap extractFullMap(ISFullMapAccesser fullmap) {
		NetworkTranslator nt = new NetworkTranslator();

		var positionToTerrainMap = fullmap.getTerrain();

		Set<FullMapNode> mapNodes = new HashSet<>();
		for (var positionTerrainPair : positionToTerrainMap.entrySet()) {
			MessagesBase.ETerrain terrain = nt.internalTerrainToNetwork(positionTerrainPair.getValue());

			Position currentPos = positionTerrainPair.getKey();

			EPlayerPositionState playerPositionState = EPlayerPositionState.NoPlayerPresent;
			if (fullmap.getPlayerPosition(me).equals(currentPos)) {
				playerPositionState = EPlayerPositionState.MyPosition;
			}
			if (fullmap.getPlayerPosition(other).equals(currentPos)) {
				if (playerPositionState == EPlayerPositionState.MyPosition) {
					playerPositionState = EPlayerPositionState.BothPlayerPosition;
				} else {
					playerPositionState = EPlayerPositionState.EnemyPlayerPosition;
				}
			}

			ETreasureState treasureState = ETreasureState.NoOrUnknownTreasureState;
			// fullmap.getTreasurePosition() returns an optional
			if (fullmap.getTreasurePosition(me, me).isPresent()
					&& fullmap.getTreasurePosition(me, me).get().equals(currentPos)) {
				treasureState = ETreasureState.MyTreasureIsPresent;
			}

			EFortState fortState = EFortState.NoOrUnknownFortState;
			if (fullmap.getCastlePosition(me, me).isPresent()
					&& fullmap.getCastlePosition(me, me).get().equals(currentPos)) {
				fortState = EFortState.MyFortPresent;
			}
			if (fullmap.getCastlePosition(me, other).isPresent()
					&& fullmap.getCastlePosition(me, other).get().equals(currentPos)) {
				fortState = EFortState.EnemyFortPresent;
			}

			mapNodes.add(new FullMapNode(terrain, playerPositionState, treasureState, fortState, currentPos.getx(),
					currentPos.gety()));

		}

		return new FullMap(mapNodes);
	}

	private String generateGameStateID() {
		return "Not implemented!";
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
