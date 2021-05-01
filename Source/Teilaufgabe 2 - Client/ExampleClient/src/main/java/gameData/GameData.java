package gameData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import gameData.helpers.EGameState;

public class GameData {

	private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

	private EGameState gameState = EGameState.UNDETERMINED;
	private int turnCount = 0;
	private boolean myTurn;

	public GameData(boolean myTurn) {
		this.myTurn = myTurn;
	}

	public void nextTurn() {
		++turnCount;
		myTurn = !myTurn;
		changes.firePropertyChange("turnCount", (Integer) (turnCount - 1), (Integer) turnCount);
		changes.firePropertyChange("myTurn", (Boolean) !myTurn, (Boolean) myTurn);
	}

	public void updateGameState(EGameState newState) {
		changes.firePropertyChange("gameState", gameState, newState);
		gameState = newState;
	}

	public EGameState getGameState() {
		return gameState;
	}

	public void addListener(PropertyChangeListener view) {
		changes.addPropertyChangeListener(view);
	}

}
