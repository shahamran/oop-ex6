package oop.ex6.main.variables;

/**
 * This is thrown when trying to assign a value to a variable that it can't accept.<br>
 * Holds the bad value string that caused this exception to be thrown, and the variable that tried
 * to receive this value.
 */
public class BadVariableValueException extends VariableException {
	private static final long serialVersionUID = 1L;
	private String badVal;
	private Variable var;
	
	/**
	 * Constructs a new exception holding the faulty value and the variable to which
	 * it was assigned
	 * @param badValue The faulty value
	 * @param variable The variable to assign.
	 */
	public BadVariableValueException(String badValue, Variable variable) {
		super("Bad variable value: " + badValue + " for type: " + variable.getType());
		this.badVal = badValue;
		this.var = variable;
	}
	
	/**
	 * @return The value that's causing your problems.
	 */
	public String getBadValue() {
		return badVal;
	}
	
	/**
	 * @return The variable that still has a chance.
	 */
	public Variable getVariable() {
		return var;
	}
}
