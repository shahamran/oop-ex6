package oop.ex6.main.variables;

import java.util.regex.*;

/**
 * This class represents a variable in the code.
 * @author Ran
 *
 */
public class Variable {
	private boolean isFinal = false, isInit = false;
	private String myName, myVal = null;
	private VariableType myType;
	
	/**
	 * Constructs a new variable with a given name and type.
	 * @param newName The name of the variable
	 * @param newType The variable's type (VariableType enum object)
	 * @throws BadVariableNameException 
	 */
	public Variable(String newName, VariableType newType) throws BadVariableNameException {
		myName = newName;
		myType = newType;
		Matcher nameMatch = VariableType.getNamePattern().matcher(newName);
		if (!nameMatch.matches()) {
			throw new BadVariableNameException(newName);
		}
	}
	
	/**
	 * @return The name of this variable object.
	 */
	public String getName() {
		return myName;
	}
	
	/**
	 * @return The value of this variable object.
	 */
	public String getValue() {
		return myVal;
	}
	
	/**
	 * @return True if this variable was initialized, false otherwise.
	 */
	public boolean isInit() {
		return isInit;
	}
	
	/**
	 * @param newVal The string of the value to assign
	 * @throws VariableException Can be thrown either when trying to assign a value to a final (constant)
	 * 							 or when trying to assign a value that this variable can't accept.
	 */
	public void setValue(String newVal) throws VariableException {
		if (isFinal && isInit)
			throw new AssignToFinalException();
		Pattern valPattern; // Checks if the value is in valid syntax.
		if ((valPattern = myType.getSpecificPattern()) != null) {
			Matcher match = valPattern.matcher(newVal);
			if (!match.find()) {
				throw new BadVariableValueException(newVal, this);
			}
			newVal = match.group(1); // Gets the value
		} else {
			throw new BadVariableValueException(newVal, this);
		}
		myVal = newVal;
		isInit = true;
	}
	
	/**
	 * Sets this variable to be a constant - which means that if it's initialized its value
	 * can't be changed, and if its not, it can only be assigned.
	 */
	public void setFinal() {
		isFinal = true;
	}
}
