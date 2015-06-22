package oop.ex6.main.inner_scopes;

import oop.ex6.main.IllegalCodeException;

/**
 * Thrown if the inner scope line is in invalid syntax.
 */
public class IllegalInnerScopeException extends IllegalCodeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with default message.
	 */
	public IllegalInnerScopeException() {
		super("Invalid inner scope definition.");
	}
	
	/**
	 * Constructs a new exception specifying the bad line.
	 */	
	public IllegalInnerScopeException(String badLine) {
		super("Invalid inner scope definition: " + badLine);
	}

}
