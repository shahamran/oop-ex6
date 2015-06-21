package oop.ex6.main.variables;

/**
 * Thrown when trying to create more than one variable with the same name.
 */
public class VariableNameOverloadException extends BadVariableNameException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new exception indicating what name was overloaded.
	 * @param badName The overloaded name.
	 */
	public VariableNameOverloadException(String badName) {
		super("Can't create more that one variable with the name: " + badName);
	}
	
	/**
	 * Constructs a default new exception with general message.
	 */
	public VariableNameOverloadException() {
		super("Can't create more that one variable with the same name.");
	}
}
