package oop.ex6.main.variables;

public class NoSuchTypeException extends VariableException {
	private static final long serialVersionUID = 1L;

	public NoSuchTypeException() {
		super("Bad variable type.");
	}
	public NoSuchTypeException(String badType) {
		super("No such variable type: " + badType);
	}

}
