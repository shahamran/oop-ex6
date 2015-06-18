package oop.ex6.main.variables;

public class EmptyVariableException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public EmptyVariableException() {
		super("Variable is not initialized");
	}
	
	public EmptyVariableException(Variable v) {
		super("Variable '" + v.getName() + "' is not initialized!");
	}
}
