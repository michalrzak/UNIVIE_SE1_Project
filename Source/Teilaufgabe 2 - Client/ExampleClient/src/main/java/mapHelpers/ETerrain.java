package mapHelpers;

public enum ETerrain {
	GRASS(1), WATER(100), MOUNTAIN(2);

	private final int cost;

	private ETerrain(int cost) {
		this.cost = cost;
	}

	public int cost() {
		return cost;
	}
}
