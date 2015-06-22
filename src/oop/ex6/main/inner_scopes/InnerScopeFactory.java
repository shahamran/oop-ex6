package oop.ex6.main.inner_scopes;

import java.util.List;
import java.util.regex.*;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.SJavaFile;
import oop.ex6.main.Scope;
import oop.ex6.main.variables.Variable;
import oop.ex6.main.variables.VariableType;

/**
 * This class creates inner scope objects.
 */
public class InnerScopeFactory {
	private static Pattern firstLinePattern = Pattern.compile("^\\s*(\\w+)\\s*" + InnerScope.ARGS_LINE
																				+ "\\{\\s*$");
	// Arguments seperated by || and && - hence this regex
	private static final String SEPERATOR = "\\s*(?:(?:\\|\\|)|(?:&&))\\s*";
	
	/**
	 * Enum that represents a valid inner scope.
	 */
	enum ValidInnerScope{IF("if"),WHILE("while");
		String myName;
		
		ValidInnerScope(String newName) {
			myName = newName;
		}
		@Override
		/**
		 * Returns the name of this inner scope.
		 */
		public String toString() {
			return myName;
		}
	}
	
	/**
	 * Attempts to create a new InnerScope object.
	 * @param parentScope The InnerScope object in which this InnerScope is nested (MUST BE INNER SCOPE)
	 * @param content The entire block of text that defined by the InnerScope that will be created.
	 * @return A freshly created InnerScope object.
	 * @throws IllegalCodeException Thrown if an illegal inner scope line was given. Can also throw
	 * variable exception if an un-initialized variable was used.
	 */
	public static InnerScope createInnerScope(InnerScope parentScope, List<String> content) 
																		throws IllegalCodeException {
		String firstLine = content.get(0);
		Matcher match = firstLinePattern.matcher(firstLine);
		if (!match.matches()) {
			throw new IllegalInnerScopeException(firstLine);
		}
		String name = match.group(1); // The first capturing group is the name.
		ValidInnerScope validScope = null;
		// Check if the name given is a valid inner scope name:
		for (ValidInnerScope i : ValidInnerScope.values()) {
			if (i.toString().equals(name)) {
				validScope = i;
				break;
			}
		}
		if (validScope == null) // If no such inner scope name was found:
			throw new NoSuchInnerScopeException(name);
		// Checks the arguments:
		String[] args = match.group(2).split(SEPERATOR); // The arguments are the 2nd capturing group
		Variable v;
		for (String arg : args) {
			// Check if there exists a variable with a name equals to the argument given
			if ((v = parentScope.getVariable(arg)) != null) { 
				arg = v.getValue(); // If so, get its value.
			}
			// Check the argument is a valid boolean value
			if (SJavaFile.isExactMatch(VariableType.BOOLEAN.getValuePattern(), arg)) {
				continue;
			} else {
				throw new BadInnerScopeArgumentException(validScope, arg);
			}
		}
		List<String> newContent = Scope.trimContent(content);
		return new InnerScope(parentScope, newContent);
	}
}
