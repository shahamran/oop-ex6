package oop.ex6.main;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.methods.Method;
import oop.ex6.main.methods.MethodFactory;

public class SJavaFile extends Scope {
	
	private HashMap<String, Method> methodsList;
	int bracketCount = 0, scopeStart = 0;
		

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
			if (this.bracketCount > 0) { //some inner scope is open
				if (isMatch(ValidLine.METHOD_END.getPattern(),line)) {
					if (this.bracketCount > 1) {
						this.bracketCount--;
						continue;
					} else if (this.bracketCount == 1) { //exactly one not closed scope left
						Method method = MethodFactory.createMethod(this, myContent.subList(this.scopeStart,i));
						this.methodsList.put(method.getName(), method);
						this.mySubScopes.add(method);
						this.bracketCount --;
						continue;
					} else {
						throw new IllegalCodeException(line,i);
					}
					
				} else {
					continue;
				}
			} else if (isMatch(ValidLine.VARIABLE_INIT.getPattern(),line)) {
				super.handleVariableLine(line);
				continue;
			} else if (isMatch(ValidLine.METHOD_START.getPattern(),line)) {
				this.scopeStart = i;
				this.bracketCount++;
				continue;
			} else { // If not one of the following
				throw new IllegalCodeException(line, i);
			}
		}
		// Actually reading
		for(Scope subScope : this.mySubScopes){
			subScope.readScope();
		}
	}
	
	private boolean isMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.find();
	}
	
	/**
	 * 
	 * @param methodName name of the method of interest
	 * @return the Method with such name or null if none found
	 */
	public Method getMethod(String methodName){
		return methodsList.get(methodName);
		
	}
	
}
