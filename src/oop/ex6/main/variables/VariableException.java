package oop.ex6.main.variables;

import oop.ex6.main.IllegalCodeException;

public class VariableException extends IllegalCodeException {
	private static final long serialVersionUID = 1L;

	public VariableException() {
		this("Bad variable initialization");
	}
	
	public VariableException(String msg) {
		super(msg);
	}
}
