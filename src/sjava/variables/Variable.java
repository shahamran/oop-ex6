package sjava.variables;

import java.util.regex.*;

/**
 * This class represents a data member in S Java files.
 * @param <T> The data-member's type.
 */
public class Variable<T> {
	T value;
	String name;
	boolean wasInit = false;
	private static final String NAME_STR_REGEX = "(?:[A-Za-z]|_)\\w*";
	private static final Pattern namePattern = Pattern.compile(NAME_STR_REGEX);
	
	private boolean isValidName(String aName) {
		Matcher matcher = namePattern.matcher(aName);
		return matcher.matches();
	}
	
	/**
	 * @param newName The name this data members will hold.
	 * @throws BadVariableNameException 
	 */
	Variable(String newName) throws BadVariableNameException {
		if (!isValidName(newName))
			throw new BadVariableNameException(newName);
		name = newName;
	}
	
	/**
	 * @param newName The name this data members will hold.
	 * @param newVal The value it holds.
	 * @throws BadVariableNameException 
	 */
	Variable(String newName, T newVal) throws BadVariableNameException {
		this(newName);
		value = newVal;
		wasInit = true;
	}
	
	/**
	 * @return The value this data member holds
	 */
	T getValue() {
		return value;
	}
	
	/**
	 * @param newVal Sets a new value to this data member
	 */
	void setValue(String newVal) {
		try {
			T val = (T) newVal;
			value = val;
		} catch (Exception e) {
			
		}
		wasInit = true;
	}
	
	/**
	 * @return True if the data-member was initialised, false otherwise.
	 */
	boolean isInitialized() {
		return wasInit;
	}
	
}
