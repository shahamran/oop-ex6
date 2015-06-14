package sjava.variables;

import java.util.regex.*;

/**
 * This class represents a data member in S Java files.
 * @param <T> The data-member's type.
 */
public class Variable<T> {
	String myName;
	T myVal;
	boolean wasInit = false, isFinal = false;
	
	private static final String NAME_STR_REGEX = "(?:(?:[A-Za-z])|(?:_\\w))\\w*",
			TYPE_STR_REGEX = "^[A-Za-z]+",
			ASSIGN_STR_REGEX = "=\\s*[^,]+", LINE_END_REGEX = ";$",
			INIT_STR_REGEX = "(?:" + NAME_STR_REGEX + ")\\s*" + "(?:" + ASSIGN_STR_REGEX + ")?",
			LINE_STR_REGEX = TYPE_STR_REGEX + "\\s+" + INIT_STR_REGEX + 
			"\\s*(?:,\\s*" + INIT_STR_REGEX + ")*" + "\\s*" + LINE_END_REGEX;
	public static final Pattern initPattern = Pattern.compile(INIT_STR_REGEX),
			 					typePattern = Pattern.compile(TYPE_STR_REGEX),
			 					linePattern = Pattern.compile(LINE_STR_REGEX),
			 					namePattern = Pattern.compile(NAME_STR_REGEX),
			 					assignPattern = Pattern.compile(ASSIGN_STR_REGEX);
	private static final String[] validTypes = {"String", "int", "double", "boolean"};
	
	
	public Variable(String newName) throws VariableException {
		if (!namePattern.matcher(newName).find())
			throw new BadVariableNameException(newName);
		myName = newName;
	}
	
	public Variable(String newName, T newVal) throws VariableException {
		this(newName);
		this.setVal(newVal);
	}
	
	public boolean setFinal() {
		isFinal = wasInit;
		return isFinal;
	}
	
	public void setVal(T newVal) {
		myVal = newVal;
		wasInit = true;
	}
}
