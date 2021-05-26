package validation.halfMap;

import MessagesBase.HalfMap;
import validation.Validator;

public interface HalfMapValidator extends Validator<HalfMap> {
	@Override
	public void validate(HalfMap data);
}
