package map.mapHelpers;

public enum ETerrain {
	GRASS(1, 15), WATER(1000, 4), MOUNTAIN(2, 3);

	private final int cost;
	private final int minAmount;

	private ETerrain(int cost, int minAmount) {
		this.cost = cost;
		this.minAmount = minAmount;
	}

	public int cost() {
		return cost;
	}

	public int minAmount() {
		return minAmount;
	}
}
