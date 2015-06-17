package oop.ex6.main.variables;

/**
 * This is thrown when trying to assign a value to a variable that it can't accept.
 * @author Ran
 *
 */
public class BadVariableValueException extends VariableException {
	private static final long serialVersionUID = 1L;
	private String badValue;
	private Variable variable;
	
	/**
	 * Constructs a new exception holding the faulty value and the variable to which
	 * it was assigned
	 * @param badVal The faulty value
	 * @param var The variable to assign.
	 */
	public BadVariableValueException(String badVal, Variable var) {
		super("Bad variable value: " + badVal);
		this.badValue = badVal;
		this.variable = var;
	}
	
	/**
	 * @return The value that's causing your problems.
	 */
	public String getBadValue() {
		return badValue;
	}
	
	/**
	 * @return The variable that still has a chance.
	 */
	public Variable getVariable() {
		return variable;
	}
	
}
