package gamedata.player.helpers;

public class PlayerInformation {
	private final String firstName;
	private final String lastName;
	private final String studentID;

	public PlayerInformation(String firstName, String lastName, String studentID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentID = studentID;
	}

	public String getFirtName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStudentID() {
		return studentID;
	}
}
