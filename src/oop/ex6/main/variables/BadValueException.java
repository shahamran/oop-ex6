package oop.ex6.main.variables;

public class BadValueException extends VariableException {
	private static final long serialVersionUID = 1L;
	private String badValue;
	private Variable<?> badVariable;
	
	public BadValueException(String badVal, Variable var) {
		super("Bad variable value: " + badVal);
		this.badValue = badVal;
		this.badVariable = var;
	}
	
	public String getBadValue() {
		return badValue;
	}
	
	public Variable<?> getVariable() {
		return badVariable;
	}
	
}
