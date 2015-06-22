package oop.ex6.main.variables;

/**
 * Thrown when trying to change a final variable.
 */
public class AssignToFinalException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception.
	 */
	public AssignToFinalException() {
		super("Can't assign new value to a constant.");
	}

}
