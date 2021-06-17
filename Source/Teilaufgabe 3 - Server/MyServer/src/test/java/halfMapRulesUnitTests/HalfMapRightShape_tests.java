package halfMapRulesUnitTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import MessagesBase.HalfMap;
import MessagesBase.UniqueGameIdentifier;
import exceptions.InvalidMapException;
import rules.IRules;
import rules.RuleHalfMapDimensions;

class HalfMapRightShape_tests {

	char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
			{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };

	@Test
	void HalfMapInvalidNumberOfNodes_MissingEntireRow_ShouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapDimensions();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapInvalidNumberOfNodes_MissingOneField_ShouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapDimensions();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapInvalidNumberOfNodes_OneRowTooMany_ShouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapDimensions();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapInvalidNumberOfNodes_OneFieldTooMany_ShouldThrow() {
		char[][] nodes = { { 'G', 'w', 'g', 'w', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'w', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapDimensions();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapInvalidNumberOfNodes_OneFieldTooManyInOneRowOneTooLittleInOther_ShouldThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'w' }, { 'g', 'm', 'w', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'm', 'g', 'g', 'g', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapDimensions();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertThrows(InvalidMapException.class, testRule);
	}

	@Test
	void HalfMapCorrectNumberOfNodes_CorrectMap_ShouldNotThrow() {
		char[][] nodes = { { 'G', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' },
				{ 'm', 'w', 'w', 'g', 'g', 'g', 'g', 'g' }, { 'g', 'm', 'w', 'g', 'g', 'g', 'm', 'm' } };

		HalfMap testMap = Helper.halfMapFromArray(nodes);

		IRules testingRule = new RuleHalfMapDimensions();

		Executable testRule = () -> {
			testingRule.validateNewHalfMap(new UniqueGameIdentifier(), testMap);
		};

		assertDoesNotThrow(testRule);
	}

}
