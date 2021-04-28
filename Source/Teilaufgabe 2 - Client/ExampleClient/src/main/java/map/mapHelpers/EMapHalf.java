package map.mapHelpers;

public enum EMapHalf {
	LONGMAPORIGIN(0, 7, 0, 3), LONGMAPOPPOSITE(8, 15, 0, 3), SQUAREMAPORIGIN(0, 7, 0, 3), SQUAREMAPOPPOSITE(0, 7, 4, 7);

	private final int xlower;
	private final int xupper;
	private final int ylower;
	private final int yupper;

	private EMapHalf(int xlower, int xupper, int ylower, int yupper) {
		this.xlower = xlower;
		this.xupper = xupper;
		this.ylower = ylower;
		this.yupper = yupper;
	}

	// always inclusive
	public int getxLowerBound() {
		return xlower;
	}

	public int getxUpperBound() {
		return xupper;
	}

	public int getyLowerBound() {
		return ylower;
	}

	public int getyUpperBound() {
		return yupper;
	}
}
