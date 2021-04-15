package networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import fullMap.FullMapData;
import halfMap.HalfMapData;
import mapHelpers.EGameEntity;
import mapHelpers.ETerrain;
import mapHelpers.Position;

public class NetworkTranslator {
	private NetworkEndpoint ne;

	private UniquePlayerIdentifier playerID;

	private static Logger logger = LoggerFactory.getLogger(NetworkTranslator.class);

	// maybe it is good to pass a created network endpoint instance here not sure
	// yet

	// CONSTRUCTOR
	public NetworkTranslator(String gameURL, String gameID) {
		ne = new NetworkEndpoint(gameURL, new UniqueGameIdentifier(gameID));
	}

	// PRIVATE METHODS
	private static ETerrain networkTerrainToInternal(MessagesBase.ETerrain ele) {
		switch (ele) {
		case Grass:
			return ETerrain.GRASS;
		case Water:
			return ETerrain.WATER;
		case Mountain:
			return ETerrain.MOUNTAIN;
		default:
			logger.error("Received an unknown terrain type from server");
			throw new RuntimeException("Received an unknown terrain type from server");
		}
	}

	private static MessagesBase.ETerrain internalTerrainToNetwork(ETerrain ele) {
		switch (ele) {
		case GRASS:
			return MessagesBase.ETerrain.Grass;
		case WATER:
			return MessagesBase.ETerrain.Water;
		case MOUNTAIN:
			return MessagesBase.ETerrain.Mountain;
		default:
			logger.error("HalfMap object contains a terrain type that is not defined in the network protocol");
			throw new RuntimeException(
					"HalfMap object contains a terrain type that is not defined in the network protocol!");
		}
	}

	private HalfMap internalHalfMapToNetworkHalfMap(HalfMapData hmdata) {
		Position castle = hmdata.castlePosition();
		List<HalfMapNode> hmnodes = hmdata.getStream().map(ele -> {
			MessagesBase.ETerrain et = internalTerrainToNetwork(ele.getValue());
			return new HalfMapNode(ele.getKey().getx(), ele.getKey().gety(), castle.equals(ele.getKey()), et);
		}).collect(Collectors.toList());

		return new HalfMap(playerID, hmnodes);
	}

	private static List<EGameEntity> extractGameEntitiesFromNode(MessagesGameState.FullMapNode node) {
		List<EGameEntity> ret = new ArrayList<>();

		if (node.getFortState() == MessagesGameState.EFortState.MyFortPresent)
			ret.add(EGameEntity.MYCASTLE);
		if (node.getFortState() == MessagesGameState.EFortState.EnemyFortPresent)
			ret.add(EGameEntity.ENEMYCASTLE);

		if (node.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.MyPosition
				|| node.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.BothPlayerPosition)
			ret.add(EGameEntity.MYPLAYER);
		if (node.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.EnemyPlayerPosition
				|| node.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.BothPlayerPosition)
			ret.add(EGameEntity.ENEMYPLAYER);

		if (node.getTreasureState() == MessagesGameState.ETreasureState.MyTreasureIsPresent)
			ret.add(EGameEntity.MYTREASURE);

		return ret;
	}

	private static FullMapData networkMapToInternalMap(Collection<MessagesGameState.FullMapNode> nodes) {
		HashMap<Position, ETerrain> terrain = new HashMap<>();
		HashMap<EGameEntity, Position> gameEntities = new HashMap<>();

		for (var ele : nodes) {

			ETerrain t = networkTerrainToInternal(ele.getTerrain());

			Position pos = new Position(ele.getX(), ele.getY());

			terrain.put(pos, t);

			List<EGameEntity> entities = extractGameEntitiesFromNode(ele);
			for (EGameEntity ent : entities)
				gameEntities.put(ent, pos);

			if (ele.getFortState() == MessagesGameState.EFortState.MyFortPresent)
				gameEntities.put(EGameEntity.MYCASTLE, pos);
			if (ele.getFortState() == MessagesGameState.EFortState.EnemyFortPresent)
				gameEntities.put(EGameEntity.ENEMYCASTLE, pos);

			if (ele.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.MyPosition
					|| ele.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.BothPlayerPosition)
				gameEntities.put(EGameEntity.MYPLAYER, pos);
			if (ele.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.EnemyPlayerPosition
					|| ele.getPlayerPositionState() == MessagesGameState.EPlayerPositionState.BothPlayerPosition)
				gameEntities.put(EGameEntity.ENEMYPLAYER, pos);

			if (ele.getTreasureState() == MessagesGameState.ETreasureState.MyTreasureIsPresent)
				gameEntities.put(EGameEntity.MYTREASURE, pos);

		}

		return new FullMapData(terrain, gameEntities);
	}

	// PUBLIC METHODS
	public void registerPlayer(String firstName, String lastName, String id) {
		playerID = ne.registerPlayer(new PlayerRegistration(firstName, lastName, id));
	}

	public void sendHalfMap(HalfMapData hmdata) {
		HalfMap hm = internalHalfMapToNetworkHalfMap(hmdata);
		ne.sendHalfMap(hm);
	}

	public boolean myTurn() {
		GameState gs = ne.getGameState(playerID);

		Set<PlayerState> players = gs.getPlayers();

		// this throws a NoSuchElementException if the object is not found
		PlayerState me = players.stream().filter(p -> p.equals(playerID)).findFirst().get();

		logger.debug("Game state is: " + me.getState().toString());

		return me.getState() == EPlayerGameState.ShouldActNext;
	}

	public FullMapData getFullMap() {

		MessagesGameState.FullMap fm;
		try {
			// this can fail and throw a NoSuchElementException
			fm = ne.getGameState(playerID).getMap().get();
		} catch (NoSuchElementException e) {
			logger.error("The returned gamestate did not contain any fullmap");
			throw e; // TODO: This really needs to be a new type
		}

		return networkMapToInternalMap(fm.getMapNodes());
	}
}
