package networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerMove;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import map.fullMap.FullMapData;
import map.halfMap.HalfMapData;
import map.mapHelpers.EGameEntity;
import map.mapHelpers.ETerrain;
import map.mapHelpers.Position;
import moveHelpers.EMove;

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
		Map<Position, ETerrain> terrain = new HashMap<>();
		Map<EGameEntity, Position> gameEntities = new HashMap<>();

		for (var ele : nodes) {

			ETerrain t = networkTerrainToInternal(ele.getTerrain());

			Position pos = new Position(ele.getX(), ele.getY());

			terrain.put(pos, t);

			List<EGameEntity> entities = extractGameEntitiesFromNode(ele);
			for (EGameEntity ent : entities)
				gameEntities.put(ent, pos);

		}

		return new FullMapData(terrain, gameEntities);
	}

	private static MessagesBase.EMove internalMovetoNetwork(EMove ele) {
		switch (ele) {
		case UP:
			return MessagesBase.EMove.Up;
		case DOWN:
			return MessagesBase.EMove.Down;
		case LEFT:
			return MessagesBase.EMove.Left;
		case RIGHT:
			return MessagesBase.EMove.Right;
		default:
			logger.error("Received unexpected EMove value that could not be mapped to any network representation");
			throw new IllegalArgumentException("ele cannot be translated to the network representation");
		}
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

	public void sendMove(EMove dir) {
		if (dir == null) {
			logger.error("sendMove received null argument");
			throw new IllegalArgumentException("dir cannot be null");
		}

		MessagesBase.EMove d = internalMovetoNetwork(dir);

		ne.sendMove(PlayerMove.of(playerID, d));
	}

	public Map<EGameEntity, Position> getEntities() {
		MessagesGameState.FullMap fm;
		try {
			// this can fail and throw a NoSuchElementException
			fm = ne.getGameState(playerID).getMap().get();
		} catch (NoSuchElementException e) {
			logger.error("The returned gamestate did not contain any fullmap");
			throw e; // TODO: This really needs to be a new type
		}

		// ofload to separate function
		Map<EGameEntity, Position> ret = new HashMap<>();

		for (var ele : fm.getMapNodes())
			for (var ent : extractGameEntitiesFromNode(ele))
				ret.put(ent, new Position(ele.getX(), ele.getY()));

		return ret;

		// return networkMapToInternalMap(fm.getMapNodes());
	}
}
