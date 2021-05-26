package validation.halfMap;

import MessagesBase.HalfMap;
import exceptions.InvalidDataException;

public class AllFieldsPresent implements HalfMapValidator {

	@Override
	public void validate(HalfMap data) {
		if (!data.isDefined() || data.getNodes() == null) {
			throw new InvalidDataException("The halfmap had a null field or the usreID was of length 0");
		}
	}

}
