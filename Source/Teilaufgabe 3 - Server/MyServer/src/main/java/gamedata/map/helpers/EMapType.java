package gamedata.map.helpers;

import java.util.Random;

public enum EMapType {
	LONGMAP(32, 8, 16, 8, new Position(16, 0)), SQUAREMAP(16, 16, 16, 8, new Position(0, 16));

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