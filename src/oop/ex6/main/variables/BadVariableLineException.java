package oop.ex6.main.variables;

public class BadVariableLineException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public BadVariableLineException() {
		super("Bad variable line.");
	}
	
	public BadVariableLineException(String badLine) {
		super("This is not a valid variable line: " + badLine);
	}
}
