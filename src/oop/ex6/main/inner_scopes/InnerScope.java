package oop.ex6.main.inner_scopes;

import java.util.List;
import java.util.regex.*;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.SJavaFile;
import oop.ex6.main.Scope;
import oop.ex6.main.inner_scopes.methods.Method;

public class InnerScope extends Scope {
	public static final String ARGS_LINE =  "\\((.*)\\)\\s*";
	public static Pattern argsPattern = Pattern.compile(ARGS_LINE);
	protected enum ValidLine{SCOPE_START("\\{\\s*$"), SCOPE_END("^\\s*\\}\\s*$"),
						     METHOD_CALL("^\\s*[A-Za-z]\\w*\\s*\\(.*\\)\\s*;$"), VARIABLE_LINE(";\\s*$"),
						     RETURN_STATEMENT("^\\s*return\\s*;\\s*$");
		Pattern myPattern;
	
		ValidLine(String regex) {
			myPattern = Pattern.compile(regex);
		}
		
		public Pattern getPattern() {
			return myPattern;
		}
	}
	
	protected InnerScope(Scope newParent, List<String> newContent) {
		super(newParent, newContent);
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
			
			if (SJavaFile.isExactMatch(ValidLine.METHOD_CALL.getPattern(),line)) {
				Method.handleMethodCall(line, getAncestor(this));
				continue;
			}
			
			if (SJavaFile.isExactMatch(ValidLine.RETURN_STATEMENT.getPattern(), line)) {
				continue;
			}
			
			if (isMatch(ValidLine.VARIABLE_LINE.getPattern(),line)) {
				super.handleVariableLine(line);
				continue;
			}
			// This means this is not a valid line.
			throw new IllegalCodeException(line);
		} // For ends here.
		

	}
	
	private void handleInnerScope(int i, String line) throws IllegalCodeException {
		if (bracketCount > 1) {
			bracketCount--;
		} else if (bracketCount == 1) {
			bracketCount--;
			List<String> newContent = myContent.subList(scopeStart, i);
			InnerScope is = InnerScopeFactory.createInnerScope(this, newContent);
			is.readScope();
		} else {
			throw new IllegalCodeException(line);
		}
	}
	
	public static SJavaFile getAncestor(InnerScope scope) {
		Scope currScope = scope;
		while (currScope.getParent() != null) {
			currScope = currScope.getParent();
		}
		return (SJavaFile) currScope;
	}
	
	private static boolean isMatch(Pattern p, String s) {
		return p.matcher(s).find();
	}

}
