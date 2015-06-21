package oop.ex6.main.inner_scopes;

import oop.ex6.main.IllegalCodeException;

public class IllegalInnerScopeException extends IllegalCodeException {
	private static final long serialVersionUID = 1L;
	
	public IllegalInnerScopeException() {
		super("Invalid inner scope definition.");
	}
	
	public IllegalInnerScopeException(String badLine) {
		super("Invalid inner scope definition: " + badLine);
	}

}
