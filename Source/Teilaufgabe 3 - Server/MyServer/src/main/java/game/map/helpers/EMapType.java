package game.map.helpers;

import java.util.Random;

public enum EMapType {
	LONGMAP(16, 4, 8, 4, new Position(8, 0)), SQUAREMAP(8, 8, 8, 4, new Position(0, 4));

	private static int MAP_TYPES = 2;

	final private int width;
	final private int height;
	final private int halfWidth;
	final private int halfHeight;
	final private Position secondHalfOffset;

	private EMapType(int width, int height, int halfWidth, int halfHeight, Position secondHalfOffset) {
		this.width = width;
		this.height = height;
		this.halfWidth = halfWidth;
		this.halfHeight = halfHeight;
		this.secondHalfOffset = secondHalfOffset;
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
		return halfWidth;
	}

	public int getHalfHeight() {
		return halfHeight;
	}

	public Position getSecondHalfOffset() {
		return secondHalfOffset;
	}
}
