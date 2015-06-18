package oop.ex6.main.variables;

/**
 * Thrown when a variable line fails to meet the SJava syntax
 * @author Ran
 */
public class BadVariableLineException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an exception with default message.
	 */
	public BadVariableLineException() {
		super("Bad variable line.");
	}
	
	/**
	 * Constructs a new exception specifying the bad line
	 * @param badLine The bad line string
	 */
	public BadVariableLineException(String badLine) {
		super("This is not a valid variable line:\n" + badLine);
	}
}
