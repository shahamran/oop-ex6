package oop.ex6.main;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.inner_scopes.methods.*;
import oop.ex6.main.variables.VariableType;

/**
 * This is the main "class" for the SJava file - Accepts only method and variable definitions.
 */
public class SJavaFile extends Scope {
	public static final String[] RESERVED_WORDS = {"void","return","if","while","true","false"};
	private List<Method> myMethods = new ArrayList<Method>();		
	
	/**
	 * Constructs a new global scope with the given content.
	 * @param newContent The content of this scope.
	 */
	public SJavaFile(List<String> newContent) {
		super(null,newContent); // This parent is always null!
	}
	
	/**
	 * Method that parses content of the scope
	 * and then reads inner scopes
	 */
	@Override
	public void readScope() throws IllegalCodeException {
		String line;
		for (int i = 0; i < myContent.size(); i++) {
			line = myContent.get(i);
			// Handle a scope start line.
			if (isMatch(ValidLine.SCOPE_START.getPattern(), line) != null) {
				if (bracketCount == 0)
					scopeStart = i;
				bracketCount++;
				continue;
			}
			// Check if some inner scope is open
			if (bracketCount > 0) { 
				if (isMatch(ValidLine.SCOPE_END.getPattern(),line) != null) {
					handleMethodEnd(i, line);
					continue;
				} else {
					continue;
				}
			}
			// Handle variable line
			if (isMatch(ValidLine.VARIABLE_LINE.getPattern(),line) != null) {
				boolean isDefined = super.handleVariableLine(line);
				if (!isDefined)
					throw new IllegalCodeException("Can only define variables in global scope!");
				continue;				
			}
			// If not one of the following:
			throw new IllegalCodeException(line, "This line is not a valid global scope line.");
		} // For loop ends here.
		if (bracketCount != 0)
			throw new UnbalancedBracketsException();
		// Actually reading
		for(Method method : myMethods){
			method.readScope();
		}
	}
	
	/**
	 * This handles the event of finding a line that closes a scope ( } ) - 
	 * either creation of a new method or just updating the brackets count.
	 * @param lineNum The line number
	 * @param line The line string
	 * @throws IllegalCodeException Can be thrown by creation of new method or unbalanced brackets.
	 */
	private void handleMethodEnd(int lineNum, String line) throws IllegalCodeException {
		bracketCount--;
		if (bracketCount > 0) {
			return;
		} else if (bracketCount == 0) {
			Method newMethod = MethodFactory.createMethod(this, myContent.subList(scopeStart, lineNum+1));
			myMethods.add(newMethod);
			return;
		} else {
			throw new UnbalancedBracketsException();
		}
	}	

	/**
	 * Checks if a certain pattern matches a given string argument.
	 * @param p The compiled pattern
	 * @param s The string to search
	 * @return null if the expression is not found in the text, or the string caught by the
	 * first capturing group if a match was found. Note that this method uses find() and not
	 * matches(), meaning it only looks for a substring that matches and not the whole string.
	 */
	public static String isMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		if (m.find()) {
			if (m.groupCount() > 0) {
				return m.group(1);
			} else {
				return m.group();
			}
		}
		return null;
	}
	
	/**
	 * Checks if a certain pattern exactly matches a given string argument.
	 * @param p The compiled pattern
	 * @param s The string to search
	 * @return True if the strings matches the pattern, false otherwise.
	 */
	public static boolean isExactMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	/**
	 * @param methodName name of the method of interest
	 * @return the Method object with such name or null if none found
	 */
	public Method getMethod(String methodName) {
		for (Method m : myMethods) {
			if (m.getName().equals(methodName))
				return m;
		}
		return null;
	}
	
	/**
	 * @param name The name to check
	 * @return True if the given name is reserved, false otherwise.
	 */
	public static boolean isReservedName(String name) {
		for (String reserved : SJavaFile.RESERVED_WORDS) {
			if (reserved.equals(name))
				return true;
		}
		for (VariableType type : VariableType.values()) {
			if (type.toString().equals(name))
				return true;
		}
		return false;
	}
	
}
