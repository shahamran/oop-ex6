package oop.ex6.main;

import java.util.*;
import java.util.regex.Pattern;

import oop.ex6.main.variables.*;

/**
 * This class represents a scope in the SJava file. A scope is an object that holds a map
 * of variables defined in it, a reference to the scope in which it is nested, the lines of code
 * in this scope and a list of all Scopes nested in it.
 */
public abstract class Scope {
	protected Scope myParent;
	protected List<String> myContent;
	protected HashMap<String, Variable> myVariables;
	protected int bracketCount = 0, scopeStart;
	
	/**
	 * This enum represents a valid line in every scope.
	 */
	protected enum ValidLine{SCOPE_START("\\s*\\{\\s*$"), SCOPE_END("^\\s*\\}\\s*$"),
	     					 METHOD_CALL("^\\s*[A-Za-z]\\w*\\s*\\(.*\\)\\s*;$"), 
	     					 VARIABLE_LINE(";\\s*$"), RETURN_STATEMENT("^\\s*return\\s*;\\s*$");
		
		Pattern myPattern;

		ValidLine(String regex) {
			myPattern = Pattern.compile(regex);
		}
		/**
	 	* @return The pattern to check if the line of string matches this kind of line
		*/
		public Pattern getPattern() {
			return myPattern;
		}
	} // Enum ends here
	
	/**
	 * Constructs a new Scope object.
	 * @param newParent The scope in which this scope is nested.
	 * @param newContent The lines of code this scope holds.
	 */
	protected Scope(Scope newParent, List<String> newContent) {
		myParent = newParent;
		myContent = newContent != null ? newContent : new ArrayList<String>();
		myVariables = new HashMap<String, Variable>();
	}
	
	/**
	 * This method reads each line in this scope and checks if it is a valid line.
	 * @throws IllegalCodeException If invalid code is found in this scope.
	 */
	public abstract void readScope() throws IllegalCodeException;
	
	/**
	 * @return The Scope object this scope is nested in.
	 */
	public Scope getParent(){
		return myParent;
	}
	
	/**
	 * @return A map of all variables defined in this scope, mapped by name.
	 */
	public Map<String, Variable> getVariables() {
		return myVariables;
	}
	
	/**
	 * Gets a variable line and adds all variables defined by it to this scope's list of variables.
	 * If no variables were defined, this method does not change this scope's members.
	 * @param line The string of the variable line.
	 * @return True if new variables were defined, false otherwise.
	 * @throws VariableException If the line given was not a valid variable line.
	 */
	protected boolean handleVariableLine(String line) throws VariableException {
		List<Variable> newVariables = VariableFactory.parseVariableLine(line, this);
		if (newVariables != null) {
			for (Variable var : newVariables) {
				myVariables.put(var.getName(), var);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Searches for the variable initialized with a given name in
	 * this scope and all the outer ones.
	 * @param varName name of the Variable we are looking for
	 * @return The variable with such name or null if not found.
	 * If in an inner scope, returns the variable object. If in SJava scope, returns a deep copy
	 * of the variable (methods can't change global variables).
	 */
	public Variable getVariable(String varName) {
		Scope theScope = this;
		Variable theVar = null;
		while (theScope != null) {
			theVar = theScope.getVariables().get(varName);
			if (theVar != null) {
				if (theScope == getAncestor(this)) { // If this scope is the outmost scope:
					return VariableFactory.copyVariable(theVar); // then the variable is global
				} else {
					return theVar;
				}
			}
			theScope = theScope.getParent();
		}
		return theVar;
	}
	
	/**
	 * The outmost scope for all inner scopes is always a SJavaFile object. This method returns it.
	 * @param scope The scope to check.
	 * @return The outermost parent scope for this scope (ancestor).
	 */
	protected static SJavaFile getAncestor(Scope scope) {
		Scope currScope = scope;
		while (currScope.getParent() != null) {
			currScope = currScope.getParent();
		}
		return (SJavaFile) currScope; // Will always run. 
	}
	
	/**
	 * Removes the first and last lines from a list of strings if the string has more than one line.
	 * If it has only one line, returns an empty list of strings.
	 * @param oldContent The List of Strings to trim.
	 * @return The trimmed List of Strings.
	 */
	public static List<String> trimContent(List<String> oldContent) {
		if (oldContent == null)
			return null;
		int n = oldContent.size();
		List<String> newContent;
		if (n > 1) {
			newContent = oldContent.subList(1, n - 1);
		} else {
			newContent = new ArrayList<String>();
		}
		return newContent;
	}
}