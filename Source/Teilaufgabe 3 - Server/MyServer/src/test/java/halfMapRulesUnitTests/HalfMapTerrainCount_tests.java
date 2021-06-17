package halfMapRulesUnitTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import MessagesBase.HalfMap;
import MessagesBase.UniqueGameIdentifier;
import exceptions.InvalidMapException;
import rules.IRules;
import rules.RuleHalfMapTerrainCount;

class HalfMapTerrainCount_tests {

	@Test
	void HalfMapTerrainCount_OnlyOneTerrainType_shouldThrow() {
		char[][] nodes = { { 'G', 'g', 'g', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' },
				{ 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'g', 'g', 'g', 'g', 'g', 'g', 'g' } };
		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapTerrainCount();

		Executable applyRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, applyRule);
	}

	@Test
	void HalfMapTerrainCount_OneLessWater_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'm', 'm', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'm', 'g', 'g', 'g', 'm', 'm' } };
		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapTerrainCount();

		Executable applyRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, applyRule);
	}

	@Test
	void HalfMapTerrainCount_OneLessMountain_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'g', 'm' } };
		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapTerrainCount();

		Executable applyRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, applyRule);
	}

	@Test
	void HalfMapTerrainCount_OneLessGrass_shouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'm' }, { 'm', 'w', 'w', 'm', 'm', 'm', 'm', 'm' } };
		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapTerrainCount();

		Executable applyRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, applyRule);
	}

	@Test
	void HalfMapTerrainCount_CorrectMap_shouldNotThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'w', 'w', 'g', 'g', 'g', 'w', 'm' },
				{ 'w', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'm', 'w', 'w', 'g', 'g', 'm', 'm', 'm' } };
		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapTerrainCount();

		Executable applyRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertDoesNotThrow(applyRule);
	}

}
