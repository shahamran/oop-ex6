package oop.ex6.main.inner_scopes;

import java.util.List;
import java.util.regex.*;

import oop.ex6.main.*;
import oop.ex6.main.inner_scopes.methods.Method;

/**
 * This object represents an inner scope in SJava files - which is every block of code, except the 
 * class itself (which is SJavaFile object). This includes: Methods and If/While statements in our case.
 */
public class InnerScope extends Scope {
	// This is used by MethodFactory module, that's why it's public.
	public static final String ARGS_LINE =  "\\((.*)\\)\\s*";
	public static Pattern argsPattern = Pattern.compile(ARGS_LINE);
		
	/**
	 * Constructs a new inner scope object.
	 * @param newParent The parent of this scope
	 * @param newContent The content of this scope
	 */
	protected InnerScope(Scope newParent, List<String> newContent) {
		super(newParent, newContent);
	}
	
	@Override
	public void readScope() throws IllegalCodeException {
		String line;
		for (int lineNum = 0; lineNum < myContent.size() ; lineNum++) {
			line = myContent.get(lineNum);
			// Handle scope start line (ends with { )
			if (isMatch(ValidLine.SCOPE_START.getPattern(),line)) {
				if (bracketCount == 0)
					scopeStart = lineNum;
				bracketCount++;
				continue;
			}
			// Handle scope end line ( only } )			
			if (isMatch(ValidLine.SCOPE_END.getPattern(),line)) {
				handleScopeEnd(lineNum);
				continue;
			}
			// If a bracket is open, skip this line.
			if (bracketCount > 0)
				continue;
			// Handle a method call line.
			if (SJavaFile.isExactMatch(ValidLine.METHOD_CALL.getPattern(),line)) {
				Method.handleMethodCall(line, getAncestor(this));
				continue;
			}
			// Handle return statement
			if (SJavaFile.isExactMatch(ValidLine.RETURN_STATEMENT.getPattern(), line)) {
				continue; // return; is a valid line inside every inner-scope.
			}
			// Handle a variable line.
			if (isMatch(ValidLine.VARIABLE_LINE.getPattern(),line)) {
				super.handleVariableLine(line);
				continue;
			}
			// If non of the above was met, this is not a valid line.
			throw new IllegalCodeException(line, "Not a valid inner scope line.");
		} // For loop ends here.
		if (bracketCount != 0)
			throw new UnbalancedBracketsException();

	}
	
	/**
	 * This method is called when reached a scope closing line.
	 * @param lineNum The number of current line
	 * @param line The string of this line.
	 * @throws IllegalCodeException If a new inner scope is created by calling this, this method reads it
	 * to check if it's valid. If not, this exception is thrown.
	 * @throws UnbalancedBracketsException
	 */
	private void handleScopeEnd(int lineNum) throws UnbalancedBracketsException, IllegalCodeException {
		bracketCount--;
		if (bracketCount > 0) {
			return; // This means a set of brackets is still open, do nothing.
		} else if (bracketCount == 0) {
			InnerScope inS = InnerScopeFactory.createInnerScope(this,
																myContent.subList(scopeStart,lineNum+1));
			inS.readScope(); // Read once created, to check if legal.
		} else {
			throw new UnbalancedBracketsException();
		}
	}
	
	/**
	 * A simple helper method that checks if a given string matches a given pattern.
	 * @param p The pattern to check
	 * @param s The string to check
	 * @return True if the string matches the pattern, false otherwise.
	 */
	private static boolean isMatch(Pattern p, String s) {
		return p.matcher(s).find();
	}

}
