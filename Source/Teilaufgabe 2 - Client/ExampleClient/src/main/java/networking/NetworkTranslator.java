package networking;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesBase.PlayerRegistration;
import MessagesBase.UniqueGameIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import halfMap.HalfMapData;
import mapHelpers.Position;

public class NetworkTranslator {
	private NetworkEndpoint ne;

	private static Logger logger = LoggerFactory.getLogger(NetworkTranslator.class);

	// maybe it is good to pass a created network endpoint instance here not sure
	// yet
	public NetworkTranslator(String gameURL, String gameID) {
		ne = new NetworkEndpoint(gameURL, new UniqueGameIdentifier(gameID));
	}

	public void registerPlayer(String firstName, String lastName, String id) {
		ne.registerPlayer(new PlayerRegistration(firstName, lastName, id));
	}

	private HalfMap internalHalfMapToNetworkHalfMap(HalfMapData hmdata) {
		Position castle = hmdata.castlePosition();
		List<HalfMapNode> hmnodes = hmdata.getStream().map(ele -> {
			MessagesBase.ETerrain et;
			switch (ele.getValue()) {
			case GRASS:
				et = MessagesBase.ETerrain.Grass;
				break;
			case WATER:
				et = MessagesBase.ETerrain.Water;
				break;
			case MOUNTAIN:
				et = MessagesBase.ETerrain.Mountain;
				break;
			default:
				logger.error("HalfMap object contains a terrain type that is not defined in the network protocol");
				throw new RuntimeException(
						"HalfMap object contains a terrain type that is not defined in the network protocol!");
			}

			return new HalfMapNode(ele.getKey().getx(), ele.getKey().gety(), castle.equals(ele.getKey()), et);
		}).collect(Collectors.toList());

		return new HalfMap(ne.getPlayerID(), hmnodes);
	}

	public void sendHalfMap(HalfMapData hmdata) {
		HalfMap hm = internalHalfMapToNetworkHalfMap(hmdata);
		ne.sendHalfMap(hm);
	}

	public boolean myTurn() {
		GameState gs = ne.getGameState();

		Set<PlayerState> players = gs.getPlayers();

		var myID = ne.getPlayerID();
		// this throws a NoSuchElementException if the object is not found
		PlayerState me = players.stream().filter(p -> p.equals(myID)).findFirst().get();

		/*
		 * for (PlayerState player : players) { if
		 * (player.getUniquePlayerID().equals(id)) { me = player; break; } }
		 */

		return me.getState() == EPlayerGameState.ShouldActNext;
	}
}
