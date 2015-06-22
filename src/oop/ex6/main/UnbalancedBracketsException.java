package oop.ex6.main;

/**
 * This is thrown if the SJava file contains unbalanced set of brackets.
 */
public class UnbalancedBracketsException extends IllegalCodeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with a default message
	 */
	public UnbalancedBracketsException() {
		super("Unbalanced brackets in file");
	}
	
	/**
	 * Constructs a new exception specifying the line number.
	 */
	public UnbalancedBracketsException(int lineNum) {
		super("Unbalanced bracket token in line: " + lineNum);
	}

}
