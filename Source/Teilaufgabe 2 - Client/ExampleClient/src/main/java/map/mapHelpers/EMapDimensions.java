package map.mapHelpers;

public enum EMapDimensions {
	HALFMAP(8, 4), LONGMAP(16, 4), SQUAREMAP(8, 8);

	private final int width;
	private final int height;

	private EMapDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}
}
