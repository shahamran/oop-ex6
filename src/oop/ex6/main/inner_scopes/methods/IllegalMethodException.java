package oop.ex6.main.inner_scopes.methods;

import oop.ex6.main.IllegalCodeException;

public class IllegalMethodException extends IllegalCodeException {

	private static final long serialVersionUID = 1L;

	public IllegalMethodException() {
		super("Illegal method");
	}
	
	public IllegalMethodException(String msg) {
		super(msg);
	}
	
}
