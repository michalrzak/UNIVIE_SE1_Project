package validation.registration;

import MessagesBase.PlayerRegistration;
import exceptions.InvalidDataException;

public class AllFieldsPresent implements RegistrationValidator {

	@Override
	public void validate(PlayerRegistration data) {
		if (data.getStudentFirstName() == null || data.getStudentLastName() == null || data.getStudentID() == null) {
			throw new InvalidDataException("One of the passed fields was null!");
		}
		if (data.getStudentFirstName().length() == 0 || data.getStudentLastName().length() == 0
				|| data.getStudentID().length() == 0) {
			throw new InvalidDataException("One of the passed fields was empty!");
		}
	}

}
