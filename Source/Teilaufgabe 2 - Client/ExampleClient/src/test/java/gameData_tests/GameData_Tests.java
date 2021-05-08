package gameData_tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import gameData.GameData;
import gameData.helpers.EGameState;

class GameData_Tests {

	private PropertyChangeListener pcl;
	private GameData gd;

	@BeforeEach
	public void setupMockedListener() {
		pcl = Mockito.mock(PropertyChangeListener.class);
	}

	@BeforeEach
	public void setupGameData() {
		gd = new GameData(true);
	}

	@Test
	public void GameData_updateGameState_ShouldFireChange() {
		gd.addListener(pcl);
		gd.updateGameState(EGameState.LOST);

		// PropertyChangeEvent pce = new PropertyChangeEvent(gd, , pcl, gd)

		ArgumentMatcher<PropertyChangeEvent> gameStateLostMatcher = new ArgumentMatcher<PropertyChangeEvent>() {
			@Override
			public boolean matches(PropertyChangeEvent argument) {
				return argument.getNewValue().equals(EGameState.LOST);
			};
		};

		Mockito.verify(pcl, Mockito.times(1)).propertyChange(Mockito.argThat(gameStateLostMatcher));
	}

	@Test
	public void GameData_nextTurn_ShouldFireChangeTwice() {
		gd.addListener(pcl);
		gd.nextTurn();

		// PropertyChangeEvent pce = new PropertyChangeEvent(gd, , pcl, gd)

		ArgumentMatcher<PropertyChangeEvent> gameStateInteger = new ArgumentMatcher<PropertyChangeEvent>() {
			@Override
			public boolean matches(PropertyChangeEvent argument) {
				return argument.getNewValue() instanceof Integer;
			};
		};
		ArgumentMatcher<PropertyChangeEvent> gameStateBoolean = new ArgumentMatcher<PropertyChangeEvent>() {
			@Override
			public boolean matches(PropertyChangeEvent argument) {
				return argument.getNewValue() instanceof Boolean;
			};
		};

		Mockito.verify(pcl, Mockito.times(1)).propertyChange(Mockito.argThat(gameStateInteger));
		Mockito.verify(pcl, Mockito.times(1)).propertyChange(Mockito.argThat(gameStateBoolean));

	}

}
