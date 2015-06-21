package oop.ex6.main;

import java.util.List;
import java.util.regex.*;

import oop.ex6.main.methods.Method;

public abstract class InnerScope extends Scope {
	protected enum ValidLine{SCOPE_START("\\{\\s*$"), SCOPE_END("^\\s*\\}\\s*$"),
						     METHOD_CALL("^\\s*[A-Za-z]\\w*\\s*\\(.*\\)\\s*;$"), VARIABLE_LINE(";\\s*$");
		Pattern myPattern;
	
		ValidLine(String regex) {
			myPattern = Pattern.compile(regex);
		}
		
		protected Pattern getPattern() {
			return myPattern;
		}
	}
	
	protected InnerScope(Scope newParent, List<String> newContent) {
		super(newParent, newContent);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public void readScope() throws IllegalCodeException {
		String line;
		for (int i = 0; i < myContent.size() ; i++) {
			line = myContent.get(i);
			if (isMatch(ValidLine.SCOPE_END.getPattern(),line)) {
				handleInnerScope(i,line);
				continue;
			}
			
			if (isMatch(ValidLine.SCOPE_START.getPattern(),line)) {
				if (bracketCount == 0)
					bracketCount++;
				continue;
			}
			
			if (isMatch(ValidLine.METHOD_CALL.getPattern(),line)) {
				Method.handleMethodCall(line);
				continue;
			}
			
			if (isMatch(ValidLine.VARIABLE_LINE.getPattern(),line)) {
				super.handleVariableLine(line);
				continue;
			}
			// This means this is not a valid line.
			throw new IllegalCodeException(line);
		}
		

	}
	
	private void handleInnerScope(int i, String line) throws IllegalCodeException {
		if (bracketCount > 1) {
			bracketCount--;
		} else if (bracketCount == 1) {
			bracketCount--;
			// Create new condition / loop \\
			// read it.
		} else {
			throw new IllegalCodeException(line);
		}
	}
	
	private static boolean isMatch(Pattern p, String s) {
		return p.matcher(s).find();
	}

}
