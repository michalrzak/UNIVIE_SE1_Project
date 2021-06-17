package halfMapRulesUnitTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import MessagesBase.HalfMap;
import MessagesBase.UniqueGameIdentifier;
import exceptions.InvalidMapException;
import rules.IRules;
import rules.RuleHalfMapCastle;

class HalfMapCastle_tests {

	@Test
	void HalfMapCastle_TwoCastles_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'G', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapCastle();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapCastle_NoCastle_shouldThrow() {
		char[][] nodes = { { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapCastle();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapCastle_CorrectCastle_shouldNotThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'w', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapCastle();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertDoesNotThrow(testRule);
	}

}
