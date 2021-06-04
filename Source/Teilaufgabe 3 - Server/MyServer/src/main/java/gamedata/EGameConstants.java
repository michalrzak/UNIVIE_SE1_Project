package gamedata;

public enum EGameConstants {
	MAX_PLAYER_COUNT(2), MAX_NUM_OF_GAMES(999), MAX_PART_MAPS(2);

	private final int value;

	private EGameConstants(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
