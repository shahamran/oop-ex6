package oop.ex6.main;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.methods.Method;
import oop.ex6.main.methods.MethodFactory;

public class SJavaFile extends Scope {
	private HashMap<String, Method> methodsList;
	int bracketCount = 0, scopeStart = 0;
	
	enum ValidLine{VARIABLE_INIT("^[A-Za-z]+"),METHOD_START("{$"), METHOD_END("}$");
		Pattern myRegex;
		ValidLine(String regex) {
			myRegex = Pattern.compile(regex);
		}
		
		Pattern getPattern() {
			return myRegex;
		}
	}
	
	public SJavaFile(List<String> newContent) {
		super(null,newContent);
		methodsList = new HashMap<String,Method>();
	}
	
	public HashMap<String,Method> getMethods() {
		return methodsList;
	}

	@Override
	public void readScope() throws IllegalCodeException {
		Matcher match;
		for (int i = 0; i < myContent.size(); i++) {
			String line = myContent.get(i);
			
			if (this.bracketCount > 0) { //some inner scope is open
				match = ValidLine.METHOD_END.getPattern().matcher(line);
				if (match.find()) {
					//
					if (this.bracketCount > 1) {
						this.bracketCount--;
						continue;
					} else if (this.bracketCount == 1) { //exactly one not closed scope left
						Method method = MethodFactory.createMethod(this, myContent.subList(this.scopeStart,i));
						methodsList.put(method.getName(), method);
						this.mySubScopes.add(method);
						this.bracketCount --;
						continue;
					}
						throw new IllegalCodeException();
					
				}else{
					continue;
				}
			}
			
			match = ValidLine.VARIABLE_INIT.getPattern().matcher(line);
			if (match.find()) {
				// variables.add (new variable);
				continue;
			}
			match = ValidLine.METHOD_START.getPattern().matcher(line);
			if (match.find()) {
				//
				this.scopeStart = i;
				this.bracketCount++;
				continue;
			}
			
			
		}
		//Actually reading
		try{
			for(Scope subScope :this.mySubScopes){
				subScope.readScope();
			}
		}catch( IllegalCodeException e){
			throw new IllegalCodeException();
		}

	}
	
	/**
	 * 
	 * @param methodName name of the method of interest
	 * @return the Method with such name or null if none found
	 */
	public Method getMethod(String methodName){
		return methodsList.get(methodName);
		
	}
	
	/**
	private int handleLine(ValidLine l, String line) {
		Matcher match = l.getPattern().matcher(line);
		if (bracketCount > 0) {
			
		}
		switch (l){
		case VARIABLE_INIT:
			//
			return -1;
		case METHOD_START:
			return 0;
		}
		return 0;
			
	}
	*/

}
