package oop.ex6.main.variables;

/**
 * This is thrown whenever trying to use un-initialized variable.
 */
public class EmptyVariableException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with default message.
	 */
	public EmptyVariableException() {
		super("Variable is not initialized");
	}
	
	/**
	 * Constructs a new exception with a message that specifies the name of the bad variable used.
	 * @param v The bad variable that was used.
	 */
	public EmptyVariableException(Variable v) {
		super("Variable '" + v.getName() + "' is not initialized!");
	}
}
