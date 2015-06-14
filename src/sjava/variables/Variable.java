package sjava.variables;

import java.util.regex.*;

/**
 * This class represents a data member in S Java files.
 * @param <T> The data-member's type.
 */
public class Variable {
	String myName, myType;
	Object myVal;
	
	boolean wasInit = false;
	private static final String NAME_STR_REGEX = "(?:(?:[A-Za-z]\\w*)|(?:_\\w+))",
			TYPE_STR_REGEX = "^[A-Za-z]+",
			ASSIGN_STR_REGEX = "=\\s*[^,]+", LINE_END_REGEX = ";$",
			INIT_STR_REGEX = "(?:" + NAME_STR_REGEX + ")\\s*" + "(?:" + ASSIGN_STR_REGEX + ")?",
			LINE_STR_REGEX = TYPE_STR_REGEX + "\\s+" + INIT_STR_REGEX + 
			"\\s*(?:,\\s*" + INIT_STR_REGEX + ")*" + "\\s*" + LINE_END_REGEX;
	public static final Pattern initPattern = Pattern.compile(INIT_STR_REGEX),
			 					typePattern = Pattern.compile(TYPE_STR_REGEX),
			 					linePattern = Pattern.compile(LINE_STR_REGEX);
	private static final int TYPE_ARG = 0, NAME_ARG = 1, ASSIGN_ARG = 2, VALUE_ARG = 3, MIN_ARGS = 2;
	private static final String[] validTypes = {"String", "int", "double", "boolean"};
	
	/**
	 * @param newName The name this data members will hold.
	 * @throws VariableException 
	 */
	public Variable(String variableLine) throws VariableException {
	
		
	}
	
	private static boolean isValidType(String aType) {
		for (String type : validTypes) {
			if (type.equals(aType))
				return true;
		}
		return false;
	}
	
	private static boolean isValidName(String aName) {
		Matcher matcher = namePattern.matcher(aName);
		return matcher.find();
	}
	
	public static boolean isVariableInitLine(String line) {
		Matcher match = linePattern.matcher(line);
		return match.matches();
	}
	
}
