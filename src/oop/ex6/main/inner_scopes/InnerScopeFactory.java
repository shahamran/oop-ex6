package oop.ex6.main.inner_scopes;

import java.util.List;
import java.util.regex.*;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.SJavaFile;
import oop.ex6.main.Scope;
import oop.ex6.main.variables.Variable;
import oop.ex6.main.variables.VariableType;

public class InnerScopeFactory {
	private static Pattern firstLinePattern = Pattern.compile("^\\s*(\\w+)\\s*" + InnerScope.ARGS_LINE
																				+ "\\{\\s*$");
	private static final String SEPERATOR = "\\s*(?:(?:\\|\\|)|(?:&&))\\s*";
	enum ValidInnerScope{IF("if"),WHILE("while");
		String myStr;
		
		ValidInnerScope(String newStr) {
			myStr = newStr;
		}
		public String toString() {
			return myStr;
		}
	}
	
	public static InnerScope createInnerScope(InnerScope parentScope, List<String> content) 
																		throws IllegalCodeException {
		String firstLine = content.get(0); /////////////////////////////////////////////////////////////////////////////
		Matcher match = firstLinePattern.matcher(firstLine);
		if (!match.matches()) {
			throw new IllegalInnerScopeException(firstLine);
		}
		String name = match.group(1);
		boolean isValid = false;
		for (ValidInnerScope i : ValidInnerScope.values()) {
			if (i.toString().equals(name)) {
				isValid = true;
				break;
			}
		}
		if (!isValid) 
			throw new NoSuchInnerScopeException(name);
		
		String[] args = match.group(2).split(SEPERATOR);
		Variable v;
		for (String arg : args) {
			if ((v = parentScope.getVariable(arg)) != null) {
				arg = v.getValue();
			}
			if (SJavaFile.isExactMatch(VariableType.BOOLEAN.getValuePattern(),arg)) {
				continue;
			} else {
				throw new BadInnerScopeArgumentException(arg);
			}
		}
		List<String> newContent = null;
		//if (content.size() > 1) {
			newContent = Scope.trimContent(content);
		//}
		return new InnerScope(parentScope, newContent);
	}
}
