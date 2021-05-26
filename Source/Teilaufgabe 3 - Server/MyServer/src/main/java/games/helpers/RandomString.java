package games.helpers;

import java.util.Random;

public class RandomString {

	static final private int NUM_OF_LETTERS = (int) 'Z' - (int) 'A' + 1;
	static final private int INDEX_OF_UPPER_A = (int) 'A';
	static final private int INDEX_OF_LOWER_A = (int) 'a';
	static final private int LOWER_UPPER_OFFSET = (int) 'a' - (int) 'Z' - 1;

	final private Random rand = new Random();

	public String nextString(int length) {
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < length; ++i) {
			ret.append(nextChar());
		}

		return ret.toString();
	}

	private char nextChar() {
		int randomInt = rand.nextInt(NUM_OF_LETTERS * 2);

		if (randomInt >= NUM_OF_LETTERS) {
			randomInt += LOWER_UPPER_OFFSET;
		}
		randomInt += INDEX_OF_UPPER_A;

		return (char) randomInt;
	}

}
