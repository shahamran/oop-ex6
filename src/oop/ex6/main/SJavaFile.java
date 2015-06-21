package oop.ex6.main;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.inner_scopes.methods.Method;
import oop.ex6.main.inner_scopes.methods.MethodFactory;

public class SJavaFile extends Scope {
	private List<Method> mySubScopes = new ArrayList<Method>();
	private HashMap<String, Method> methodsList;
		

	enum ValidLine{VARIABLE_INIT("^[A-Za-z]+"),METHOD_START("\\{\\s*$"), METHOD_END("^\\s*\\}\\s*$");
		Pattern myPattern;

		ValidLine(String regex) {
			myPattern = Pattern.compile(regex);
		}
		
		Pattern getPattern() {
			return myPattern;
		}
	}
	
	/**
	 * 
	 * @param newContent
	 */
	public SJavaFile(List<String> newContent) {
		super(null,newContent);
		methodsList = new HashMap<String,Method>();
	}
	

	/**
	 * Method gets the methods defined in the scope
	 * @return all the methods defined in this file
	 */

	public Map<String,Method> getMethods() {

		return methodsList;
	}

	/**
	 * Method that parses content of the scope
	 * and then reads inner scopes
	 * 
	 */
	@Override
	public void readScope() throws IllegalCodeException {
		String line;
		for (int i = 0; i < myContent.size(); i++) {
			line = myContent.get(i);
			if (bracketCount > 0) { //some inner scope is open
				if (isMatch(ValidLine.METHOD_END.getPattern(),line) != null) {
					if (bracketCount > 1) {
						bracketCount--;
						continue;
					} else if (this.bracketCount == 1) { //exactly one not closed scope left
						Method method = MethodFactory.createMethod(this, myContent.subList(scopeStart,i+1));
						methodsList.put(method.getName(), method);
						mySubScopes.add(method);
						bracketCount --;
						continue;
					} else {
						throw new IllegalCodeException(i,line);
					}
					
				} else {
					continue;
				}
			} else if (isMatch(ValidLine.METHOD_START.getPattern(),line) != null) {
				scopeStart = i;
				bracketCount++;
				continue;
			} else if (isMatch(ValidLine.VARIABLE_INIT.getPattern(),line) != null) {
				try {
					super.handleVariableLine(line);
				} catch (IllegalCodeException e) {
					throw new IllegalCodeException(i,line, e.getMessage());
				}
				continue;
			} else { // If not one of the following
				throw new IllegalCodeException(i, line);
			}
		}
		// Actually reading
		for(Method method : mySubScopes){
			method.readScope();
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
	 * 
	 * @param methodName name of the method of interest
	 * @return the Method with such name or null if none found
	 */
	public Method getMethod(String methodName) {
		return methodsList.get(methodName);
		
	}
	
}
