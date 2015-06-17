package oop.ex6.main.variables;

/**
 * This is thrown whenever trying to define a variable with a bad name.
 * @author Ran
 *
 */
public class BadVariableNameException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with default message.
	 */
	public BadVariableNameException() {
		super("Bad variable name given.");
	}
	
	/**
	 * Constructs a new exception.
	 * @param badName The illegal name that was given.
	 */
	public BadVariableNameException(String badName) {
		super("Bad variable name: " + badName);
	}
}
