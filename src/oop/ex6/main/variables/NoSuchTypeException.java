package oop.ex6.main.variables;

/**
 * Thrown if a bad type name was given upon initialization.
 * @author Ran
 */
public class NoSuchTypeException extends VariableException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new variable with default message.
	 */
	public NoSuchTypeException() {
		super("Bad variable type.");
	}
	/**
	 * Constructs a new variable with a message that specifies the bad type name that was given.
	 * @param badType The bad type name that was given.
	 */
	public NoSuchTypeException(String badType) {
		super("No such variable type: " + badType);
	}

}
