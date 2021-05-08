package exceptions;

public class NetworkCommunicationException extends Exception {
	public NetworkCommunicationException(Exception e) {
		super("There was an error in the network communication! The error message is: " + e.getMessage());
	}

	public NetworkCommunicationException(String exceptionMessage) {
		super("There was an error in the network communication! The error message is: " + exceptionMessage);
	}

	public NetworkCommunicationException() {
		super("There was an error in the network communication!");
	}
}
