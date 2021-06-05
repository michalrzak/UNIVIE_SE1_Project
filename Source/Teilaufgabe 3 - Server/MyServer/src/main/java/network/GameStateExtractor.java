package network;

import java.util.HashSet;
import java.util.Set;

import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import gamedata.game.Game;
import gamedata.map.FullMapData;
import gamedata.map.helpers.EGameEntity;
import gamedata.map.helpers.OwnedGameEntity;
import gamedata.player.Player;
import gamedata.player.helpers.ServerUniquePlayerIdentifier;

public class GameStateExtractor {

	private final ServerUniquePlayerIdentifier playerID;

	public GameStateExtractor(ServerUniquePlayerIdentifier playerID) {
		this.playerID = playerID;
	}

	public GameState extractGameState(Game game) {
		return null;
	}

	private PlayerState extractPlayerState(Player player) {

		// TODO: MAKE THIS MEANINGFULL
		EPlayerGameState gameState = EPlayerGameState.ShouldWait;

		NetworkTranslator nt = new NetworkTranslator();

		UniquePlayerIdentifier playerID = nt.internalPlayerIDToNetwork(player.getPlayerID());

		PlayerState ret = new PlayerState(player.getFirtName(), player.getLastName(), player.getStudentID(), gameState,
				playerID, player.getCollectedTreasure());

		return ret;
	}

	private FullMap extractFullMap(FullMapData fullmap) {
		NetworkTranslator nt = new NetworkTranslator();

		var gameEntitiesToPositionMap = fullmap.getEntities();
		var positionToTerrainMap = fullmap.getTerrain();

		// TODO: I dont like how this works
		OwnedGameEntity myPlayer = new OwnedGameEntity(playerID, EGameEntity.PLAYER);
		OwnedGameEntity myTreasure = new OwnedGameEntity(playerID, EGameEntity.TREASURE);
		OwnedGameEntity myFort = new OwnedGameEntity(playerID, EGameEntity.CASTLE);

		Set<FullMapNode> mapNodes = new HashSet<>();
		for (var positionTerrainPair : positionToTerrainMap.entrySet()) {
			MessagesBase.ETerrain terrain = nt.internalTerrainToNetwork(positionTerrainPair.getValue());

			// TODO: how do i get the ID of the other player?
			// TODO: or how do i rework this to not need it. Technically it is not necessery
			// to bind this information to an ID
			MessagesGameState.EPlayerPositionState playerPositionState = MessagesGameState.EPlayerPositionState.NoPlayerPresent;
			if (gameEntitiesToPositionMap.get(myPlayer).equals(positionTerrainPair.getKey())) {
				playerPositionState = MessagesGameState.EPlayerPositionState.MyPosition;
			}

			MessagesGameState.ETreasureState treasureState = MessagesGameState.ETreasureState.NoOrUnknownTreasureState;
			if (gameEntitiesToPositionMap.get(myTreasure).equals(positionTerrainPair.getKey())) {
				treasureState = MessagesGameState.ETreasureState.MyTreasureIsPresent;
			}

			MessagesGameState.EFortState fortState = MessagesGameState.EFortState.NoOrUnknownFortState;
			if (gameEntitiesToPositionMap.get(myFort).equals(positionTerrainPair.getKey())) {
				fortState = MessagesGameState.EFortState.MyFortPresent;
			}

			mapNodes.add(new FullMapNode(terrain, playerPositionState, treasureState, fortState,
					positionTerrainPair.getKey().getx(), positionTerrainPair.getKey().gety()));

		}

		return new FullMap(mapNodes);
	}

	private String generateGameStateID() {
		return "Not implemented!";
	}
}
