package rules.helpers;

public enum EHalfMapHelpers {
	WIDTH(8), HEIGHT(4), MIN_GRASS_COUNT(15), MIN_MOUNTAIN_COUNT(3), MIN_WATER_COUNT(4),
	WIDTH_EDGE_MAX_WATER(WIDTH.get() / 2), HEIGHT_EDGE_MAX_WATER(HEIGHT.get() / 2);

	private final int value;

	private EHalfMapHelpers(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}

}
