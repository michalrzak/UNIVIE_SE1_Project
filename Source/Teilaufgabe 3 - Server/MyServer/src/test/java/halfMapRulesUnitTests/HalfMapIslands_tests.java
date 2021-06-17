package halfMapRulesUnitTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import MessagesBase.HalfMap;
import MessagesBase.UniqueGameIdentifier;
import exceptions.InvalidMapException;
import rules.IRules;
import rules.RuleHalfMapNoIslands;

class HalfMapIslands_tests {

	@Test
	void HalfMapCastle_Island_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'w', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'w', 'g', 'w' },
				{ 'g', 'g', 'w', 'g', 'g', 'g', 'w', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapNoIslands();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapCastle_OtherIsland_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapNoIslands();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapCastle_NoIsland_shouldNotThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapNoIslands();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertDoesNotThrow(testRule);
	}

}
