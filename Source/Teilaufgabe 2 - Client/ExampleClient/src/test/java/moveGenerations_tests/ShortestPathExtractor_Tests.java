package moveGenerations_tests;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import map.mapHelpers.Position;
import moveGeneration.ShortestPathExtractor;

public class ShortestPathExtractor_Tests {

	@Test
	public void ShortestPathExtractor_SendValidPositions_ShouldReturnAllSentNodes() {

		Set<Position> toVisit = new HashSet<>();

		toVisit.add(new Position(0, 1));
		toVisit.add(new Position(3, 1));
		toVisit.add(new Position(2, 4));
		toVisit.add(new Position(1, 1));
		toVisit.add(new Position(10, 2));

		ShortestPathExtractor spe = new ShortestPathExtractor();

		Queue<Position> inOrder = spe.visitInOrder(toVisit, new Position(0, 0));

		// remove any potential duplicates
		Set<Position> inOrderNoDup = inOrder.stream().collect(Collectors.toSet());

		Assertions.assertEquals(inOrderNoDup.size(), toVisit.size());
		Assertions.assertEquals(inOrderNoDup.stream().allMatch(ele -> toVisit.contains(ele)), true);
	}

}
