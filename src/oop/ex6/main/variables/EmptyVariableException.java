package oop.ex6.main.variables;

public class EmptyVariableException extends VariableException {
	private static final long serialVersionUID = 1L;
	
	public EmptyVariableException() {
		super("Can't assign un-initialized variable");
	}
	
	public EmptyVariableException(Variable v) {
		super("Can't assign un-initialized variable: " + v.getName());
	}
}
