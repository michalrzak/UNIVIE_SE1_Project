package position_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import exceptions.PositionOutOfBoundsException;
import map.mapHelpers.Position;

class Position_Tests {

	@ParameterizedTest
	@CsvSource({ "1,-1", "-5,1", "-110,-92" })
	public void Position_instantiatedWithNegativeCoordinates_ShouldThrow(int x, int y) {

		Executable instantiate = () -> {
			Position pos = new Position(x, y);
		};

		Assertions.assertThrows(PositionOutOfBoundsException.class, instantiate);

	}

	@ParameterizedTest
	@CsvSource({ "1,1", "2,15", "4,2", "100,102" })
	public void Position_instanciateWithValidCoordinates_ShouldCreateValidObjects(int x, int y) {
		Position pos = new Position(x, y);

		Assertions.assertEquals(pos.getx(), x);
		Assertions.assertEquals(pos.gety(), y);
	}

	@ParameterizedTest
	@CsvSource({ "1,1,2,3", "2,15,4,5", "4,2,2,4", "100,102,99,101" })
	public void Position_differentPositions_ShouldNotEqual(int x1, int y1, int x2, int y2) {
		Position pos1 = new Position(x1, y1);
		Position pos2 = new Position(x2, y2);

		Assertions.assertNotEquals(pos1, pos2);
	}

	@ParameterizedTest
	@CsvSource({ "1,1,2,3", "2,15,4,5", "4,2,2,4", "100,102,99,101" })
	public void Position_differentPositions_ShouldHaveDifferentHashCode(int x1, int y1, int x2, int y2) {
		Position pos1 = new Position(x1, y1);
		Position pos2 = new Position(x2, y2);

		Assertions.assertNotEquals(pos1.hashCode(), pos2.hashCode());
	}

}
