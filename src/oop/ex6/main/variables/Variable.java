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
	Variable(String newName, VariableType newType) throws BadVariableNameException {
		myName = newName;
		myType = newType;
		Matcher nameMatch = VariableType.getNamePattern().matcher(newName);
		if (!nameMatch.matches()) {
			throw new BadVariableNameException(newName);
		}
	}
	
	Variable(Variable oldVar) {
		myName = oldVar.getName();
		myType = oldVar.getType();
		myVal = oldVar.myVal;
		isFinal = oldVar.isFinal;
		isInit = oldVar.isInit;
	}
	
	/**
	 * @return The name of this variable object.
	 */
	public String getName() {
		return myName;
	}
	
	/**
	 * @return The value of this variable object.
	 * @throws EmptyVariableException thrown if the value was not initialized.
	 */
	public String getValue() throws EmptyVariableException {
		if (!isInit) // Checks if the variable was initialised.
			throw new EmptyVariableException(this);
		return myVal;
	}
	
	/**
	 * @param newVal The string of the value to assign
	 * @throws VariableException Can be thrown either when trying to assign a value to a final (constant)
	 * 							 or when trying to assign a value that this variable can't accept.
	 */
	void setValue(String newVal) throws VariableException {
		if (isFinal && isInit)
			throw new AssignToFinalException();
		if (newVal == null)
			return;
		Pattern valPattern; // Checks if the value is in valid syntax.
		if ((valPattern = myType.getValuePattern()) != null) {
			Matcher match = valPattern.matcher(newVal);
			if (!match.matches()) {
				throw new BadVariableValueException(newVal, this);
			}
			newVal = match.group(1); // Gets the value
		} else {
			throw new NoSuchTypeException(myType.toString());
		}
		myVal = newVal;
		isInit = true;
	}
	
	/**
	 * Sets this variable to be a constant - which means that if it's initialized its value
	 * can't be changed, and if its not, it can only be assigned.
	 */
	void setFinal() {
		isFinal = true;
	}
	
	public boolean isFinal() {
		return isFinal;
	}
	
	/**
	 * Sets the variable as if it was initialized. Only used for 'toy' variables - for methods.
	 */
	void setInit() {
		isInit = true;
	}
	
	/**
	 * @return True if this variable was initialized, false otherwise.
	 */
	public boolean isInit() {
		return isInit;
	}
	
	/**
	 * @return The VariableType name for this variable
	 */
	public VariableType getType() {
		return myType;
	}
}
