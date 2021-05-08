package map.mapHelpers;

public enum EMapDimensions {
	HALFMAP(8, 4), LONGMAP(16, 4), SQUAREMAP(8, 8);

	private final int maxWidth;
	private final int maxHeight;

	private EMapDimensions(int maxWidth, int maxHeight) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}

	public int maxWidth() {
		return maxWidth;
	}

	public int maxHeight() {
		return maxHeight;
	}
}
