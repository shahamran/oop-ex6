package oop.ex6.main.variables;

import java.util.regex.Pattern;

/**
 * This represents a variable type in SJava. Each object has a string, some have
 * specific requirements for their values which is given by getSpecificPattern() method.
 * @author Ran
 *
 */
public enum VariableType {INT("int"),DOUBLE("double"),CHAR("char","\\s*'(.*)'\\s*"),
						  STRING("String","\\s*\"(.*)\"\\s*"),BOOLEAN("boolean"),FINAL("final");
	String varStr;
	static final Pattern typePattern = Pattern.compile("(^[A-Za-z]+)");
	private Pattern myPattern = null;

	VariableType(String newStr) {
		varStr = newStr;
	}
	
	VariableType(String newStr, String regex) {
		this(newStr);
		myPattern = Pattern.compile(regex);
	}
	
	@Override
	public String toString() {
		return varStr;
	}
	/**
	 * @return The regex pattern to extract the type argument from a line.
	 * Use matcher.group(1) to get the string value.
	 */
	public static Pattern getPattern() {
		return typePattern;
	}
	/**
	 * @return The regex pattern for a type's value, if there is one, null otherwise.
	 * e.g. String type will return the regex \\s*"(.*)"\\s*
	 * use Matcher.group(1) to get the argument inside.
	 */
	public Pattern getSpecificPattern() {
		return myPattern;
	}
}
