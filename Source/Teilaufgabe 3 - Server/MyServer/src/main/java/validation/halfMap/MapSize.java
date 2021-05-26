package validation.halfMap;

import MessagesBase.HalfMap;
import exceptions.InvalidMapException;

public class MapSize implements HalfMapValidator {

	@Override
	public void validate(HalfMap data) {
		if (data.getNodes().size() != 32) {
			throw new InvalidMapException(
					"The passed half map did not have 32 fields. Passed field count: " + data.getNodes().size());
		}

	}

}
