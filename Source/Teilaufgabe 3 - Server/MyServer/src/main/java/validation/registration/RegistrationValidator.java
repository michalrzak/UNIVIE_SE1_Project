package validation.registration;

import MessagesBase.PlayerRegistration;
import validation.Validator;

public interface RegistrationValidator extends Validator<PlayerRegistration> {
	@Override
	public void validate(PlayerRegistration data);
}
