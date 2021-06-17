package halfMapRulesUnitTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import MessagesBase.HalfMap;
import MessagesBase.UniqueGameIdentifier;
import exceptions.InvalidMapException;
import rules.IRules;
import rules.RuleHalfMapEdges;

class HalfMapEdge_tests {

	@Test
	void HalfMapEdge_TooMuchWaterNorth_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'w', 'w', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapEdges();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapEdge_TooMuchWaterSouth_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'w', 'w', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapEdges();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapEdge_TooMuchWaterEast_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'w' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'w' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapEdges();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapEdge_TooMuchWaterWest_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapEdges();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapEdge_CorrectEdges_shouldNotThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapEdges();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertDoesNotThrow(testRule);
	}

}
