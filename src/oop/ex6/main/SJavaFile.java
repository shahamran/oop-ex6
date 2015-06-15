package oop.ex6.main;

import java.util.*;
import java.util.regex.*;

public class SJavaFile extends Scope {
	private ArrayList<?> methodsList;
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
	}
	
	public List<?> getMethods() {
		return methodsList;
	}

	@Override
	public void readScope() throws IllegalCodeException {
		Matcher match;
		
		for (int i = 0; i < myContent.size(); i++) {
			String line = myContent.get(i);
			if (bracketCount > 0) {
				match = ValidLine.METHOD_END.getPattern().matcher(line);
				if (match.find()) {
					//
					if (bracketCount > 1) {
						bracketCount--;
						continue;
					} else if (bracketCount == 1) {
						// method = new Scope(myContent.subList(scopeStart,i));
						bracketCount --;
						continue;
					}
						throw new IllegalCodeException();
					
				} else {
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
				scopeStart = i;
				bracketCount++;
				continue;
			}
			
			
		}

	}
	
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

}
