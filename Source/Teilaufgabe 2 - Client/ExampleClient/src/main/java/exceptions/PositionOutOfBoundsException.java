package exceptions;

import map.helpers.Position;

public class PositionOutOfBoundsException extends IllegalArgumentException {
	public PositionOutOfBoundsException(String s, Position pos) {
		super(s + "; Illegal Position object: " + pos.toString());
	}
}
