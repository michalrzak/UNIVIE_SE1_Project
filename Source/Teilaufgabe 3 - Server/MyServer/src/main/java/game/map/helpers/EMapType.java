package game.map.helpers;

import java.util.Optional;
import java.util.Random;

public enum EMapType {
	LONGMAP(16, 4, 8, 4, new Position(8, 0)), SQUAREMAP(8, 8, 8, 4, new Position(0, 4)), HALFMAP(8, 4);

	private static int MAP_TYPES = 2;

	final private int width;
	final private int height;
	final private Optional<Integer> halfWidth;
	final private Optional<Integer> halfHeight;
	final private Optional<Position> secondHalfOffset;

	private EMapType(int width, int height) {
		this.width = width;
		this.height = height;
		halfWidth = Optional.empty();
		halfHeight = Optional.empty();
		secondHalfOffset = Optional.empty();
	}

	private EMapType(int width, int height, int halfWidth, int halfHeight, Position secondHalfOffset) {
		this.width = width;
		this.height = height;
		this.halfWidth = Optional.of(halfWidth);
		this.halfHeight = Optional.of(halfHeight);
		this.secondHalfOffset = Optional.of(secondHalfOffset);
	}

	public static EMapType getRandomMapType() {
		Random rand = new Random();

		switch (rand.nextInt(MAP_TYPES)) {
		case 0:
			return LONGMAP;
		case 1:
			return SQUAREMAP;
		}
		throw new RuntimeException("Unhadled map type.");
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getHalfWidth() {
		if (halfWidth.isEmpty()) {
			throw new RuntimeException("The deffined maptype does not have a halfWidth specified");
		}
		return halfWidth.get();
	}

	public int getHalfHeight() {
		if (halfHeight.isEmpty()) {
			throw new RuntimeException("The deffined maptype does not have a halfHeight specified");
		}
		return halfHeight.get();
	}

	public Position getSecondHalfOffset() {
		if (secondHalfOffset.isEmpty()) {
			throw new RuntimeException("The deffined maptype does not have a second half offset sspecified");
		}
		return secondHalfOffset.get();
	}
}
