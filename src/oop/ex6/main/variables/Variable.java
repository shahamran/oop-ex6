package oop.ex6.main.variables;

import java.util.regex.*;

/**
 * This class represents a variable in the code.
 * @author Ran
 *
 */
public class Variable {
	private boolean isFinal = false, isInit = false;
	private String myName;
	private Object myVal;
	private VariableType type;
	
	/**
	 * Constructs a new variable with a given name and type.
	 * @param newName The name of the variable
	 * @param newType The variable's type (VariableType enum object)
	 */
	public Variable(String newName, VariableType newType) {
		myName = newName;
		type = newType;
	}
	
	/**
	 * Constructs a new variable with a given name type and value.
	 * @param newName The name of the variable
	 * @param newValue The value to set
	 * @param newType The variable's type
	 * @throws VariableException Thrown when trying to assign a bad value.
	 */
	public Variable(String newName, String newValue, VariableType newType) throws BadValueException {
		this(newName, newType);
		if (!(newValue == null)) {
			try {
				setValue(newValue);
			} catch (BadValueException e) {
				throw e;
			} catch (VariableException e) {
				// This shouldn't be reached - a new variable is not final by default.
			}
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
	public Object getValue() {
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
		Pattern p; // Check whether this variable's type has specific requirements for the value.
		if ((p = type.getSpecificPattern()) != null) {
			Matcher match = p.matcher(newVal);
			if (!match.find()) {
				throw new BadValueException(newVal, this);
			}
			newVal = match.group(1); // Gets the value inside the quotes
		}
		try {
			switch (type) {
			case INT:
				myVal = Integer.parseInt(newVal);
				break;
			case DOUBLE:
				myVal = Double.parseDouble(newVal);
				break;
			case STRING:
				myVal = newVal;
				break;
			case CHAR:
				if (newVal.length() != 1) {
					throw new BadValueException(newVal, this);
				}
				myVal = newVal.charAt(0);
				break;
			case BOOLEAN:
				myVal = Boolean.parseBoolean(newVal);
				break;
			default:
				break;
			}
		} catch (IllegalArgumentException e) {
			throw new BadValueException(newVal,this);
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
