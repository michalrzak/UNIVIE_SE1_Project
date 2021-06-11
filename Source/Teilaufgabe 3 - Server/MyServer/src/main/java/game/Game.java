package game;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import exceptions.GameNotReadyException;
import exceptions.PlayerInvalidTurn;
import game.map.ISFullMapAccesser;
import game.map.MapController;
import game.map.SHalfMap;
import game.player.IPlayerAccesser;
import game.player.PlayersController;
import game.player.helpers.ESPlayerGameState;
import game.player.helpers.PlayerInformation;
import game.player.helpers.SUniquePlayerIdentifier;
import game.propertychange.PropertyChangeListener;
import game.propertychange.PropertyChangeSupport;

@Component
public class Game implements IGameAccesser {

	private final PlayersController players = new PlayersController();
	private final MapController map = new MapController();

	private boolean isAlive = true;

	private final PropertyChangeSupport<Void> gameDied = new PropertyChangeSupport<>();

	private static Logger logger = LoggerFactory.getLogger(Game.class);

	public SUniquePlayerIdentifier registerPlayer(PlayerInformation playerInf) {
		return players.registerPlayer(playerInf);
	}

	public void receiveHalfMap(SUniquePlayerIdentifier playerID, SHalfMap hmData) {
		if (!getReady()) {
			logger.warn("Tried tp send a HalfMap to a game that is not ready");
			throw new GameNotReadyException("Tried to send a HalfMap to a game that is not ready");
		}

		if (!players.checkPlayerTurn(playerID)) {
			players.setAsLooser(playerID);
			logger.warn("A player with playerID: " + playerID.getPlayerIDAsString()
					+ "; tried sending a HalfMap, but it was not his turn! It was ");
			throw new PlayerInvalidTurn(
					"It is not the players with playerID: " + playerID.getPlayerIDAsString() + "; turn!");
		}
		map.receiveHalfMap(hmData);
		players.nextTurn();
	}

	public void registerListenForDeath(PropertyChangeListener<Void> listener) {
		gameDied.register(listener);
	}

	public boolean checkPlayer(SUniquePlayerIdentifier playerID) {
		return players.checkPlayer(playerID);
	}

	public void setLooser(SUniquePlayerIdentifier playerID) {
		players.setAsLooser(playerID);
	}

	public boolean getAlive() {
		return isAlive;
	}

	// 10 minutes in milliseconds. Does not allow to use my constants enum or final
	// static variables defined in this class
	@Scheduled(fixedRate = 1 * 60 * 1000)
	private void die() {
		isAlive = false;
		gameDied.fire();
	}

	@Override
	public boolean getReady() {
		return players.getReady();
	}

	@Override
	public Optional<ISFullMapAccesser> getFullMap() {
		return map.getFullMap();
	}

	@Override
	public ESPlayerGameState getPlayerState(SUniquePlayerIdentifier playerID) {
		return players.getPlayerState(playerID);
	}

	@Override
	public SUniquePlayerIdentifier getOtherPlayer(SUniquePlayerIdentifier myPlayer) {
		return players.getOtherPlayer(myPlayer);
	}

	@Override
	public int getTurn() {
		return players.getTurn();
	}

	@Override
	public IPlayerAccesser getPlayer(SUniquePlayerIdentifier playerID) {
		return players.getPlayer(playerID);
	}

}
