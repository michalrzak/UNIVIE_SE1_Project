package game.map.helpers;

public enum ESMove {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

	private int xDiff;
	private int yDiff;

	private ESMove(int xDiff, int yDiff) {
		this.xDiff = xDiff;
		this.yDiff = yDiff;
	}

	public int getXDiff() {
		return xDiff;
	}

	public int getYDiff() {
		return yDiff;
	}

}
