package oop.ex6.main.variables;

import java.util.regex.Pattern;

/**
 * This represents a variable type in SJava. Each object has a string, some have
 * specific requirements for their values which is given by getSpecificPattern() method.
 * @author Ran
 *
 */
public enum VariableType {INT("int","\\s*(-?\\d+)\\s*"),DOUBLE("double","\\s*(-?\\d+(?:\\.\\d+)?)\\s*"),
						  CHAR("char","\\s*('.')\\s*"), STRING("String","\\s*(\".*\")\\s*"),
						  BOOLEAN("boolean","\\s*((?:true|false)|(?:-?\\d+(?:\\.\\d+)?))\\s*"),
						  FINAL("final");
	String varStr;
	static final Pattern typePattern = Pattern.compile("^\\s*([A-Za-z]+)"),
						 namePattern = Pattern.compile("\\s*((?:[A-Za-z]|_\\w)\\w*)\\s*");
	private Pattern myPattern = null;
	
	/**
	 * @param newStr The type name to search in text
	 */
	VariableType(String newStr) {
		varStr = newStr;
	}
	/**
	 * Constructs a new Type with a given name and specific value format.
	 * @param newStr
	 * @param regex
	 */
	VariableType(String newStr, String regex) {
		this(newStr);
		myPattern = Pattern.compile(regex);
	}
	@Override
	public String toString() {
		return varStr;
	}
	/**
	 * @return The regex pattern to extract the name argument from a line.
	 * Use matcher.group(1) to get the string value.
	 */
	public static Pattern getNamePattern() {
		return namePattern;
	}
	/**
	 * @return The regex pattern to extract the type argument from a line.
	 * Use matcher.group(1) to get the string value.
	 */
	public static Pattern getTypePattern() {
		return typePattern;
	}
	/**
	 * @return The regex pattern for a type's value, if there is one, null otherwise.
	 * e.g. String type will return the regex \\s*(".*")\\s*
	 * use Matcher.group(1) to get the value argument.
	 */
	public Pattern getValuePattern() {
		return myPattern;
	}
}
