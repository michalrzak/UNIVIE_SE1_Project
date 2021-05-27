package gamedata.player.helpers;

public class PlayerInformation {
	final private String firstName;
	final private String lastName;
	final private String studentID;

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
