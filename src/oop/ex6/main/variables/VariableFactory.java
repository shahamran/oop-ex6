package oop.ex6.main.variables;

import java.util.*;
import java.util.regex.*;
import oop.ex6.main.Scope;

public class VariableFactory {
	private static final String VARIABLE_ASSIGNMENT = "(\\w+)\\s*(?:=\\s*(.+))?", LINE_END = "\\s*;\\s*$",
								ARGS_SEPERATOR = "\\s*,\\s*", EMPTY_STRING = "";
	private static Pattern assignPattern = Pattern.compile(VARIABLE_ASSIGNMENT),
						   lineEndPattern = Pattern.compile(LINE_END);
	
	/**
	 * Parses a variable definition line to extract all variable objects from it.
	 * @param lineStr The variable line string
	 * @return A list of variables created by this line.
	 * @throws VariableException
	 */
	public static List<Variable> parseVariableLine(String lineStr, Scope parentScope) 
																				throws VariableException {
		if (isMatch(lineEndPattern, lineStr) == null) {
			// If a line doesn't end properly (without ;), throw an exception
			throw new BadVariableLineException(lineStr);
		}
		lineStr = trimString(lineEndPattern, lineStr); // Remove the ; from the line
		boolean finalVar = isFinal(lineStr);
		if (finalVar) // Remove the 'final' from the line
			lineStr = trimString(VariableType.getTypePattern(), lineStr); 
		
		String typeStr = isMatch(VariableType.getTypePattern(),lineStr); 
		VariableType type = null;
		if (typeStr == null) 
			throw new VariableException();
		for (VariableType t : VariableType.values()) { // Checks what type this variable is
			if (typeStr.equals(t.toString())) {
				type = t;
				lineStr = trimString(VariableType.getNamePattern(),lineStr); // remove the <type> argument
				break;
			}
		}
		if (type == null) // If no type with the given name was found, throw an exception
			throw new NoSuchTypeException(typeStr);
		return createAllVariables(lineStr, type, finalVar, parentScope);
	}
	
	/**
	 * Creates a list of variables from a line with the format: <name> (= <value>), ...
	 * @param lineStr The line with the mentioned format
	 * @param type The type of variables to create
	 * @param isFinal true if the desired variables are final or false if not.
	 * @param parentScope The parent scope.
	 * @return A list of freshly created variable objects.
	 * @throws VariableException
	 */
	private static List<Variable> createAllVariables(String lineStr, VariableType type, boolean isFinal, 
										                    Scope parentScope) throws VariableException {
		ArrayList<Variable> variablesList = new ArrayList<Variable>(); // the output list
		String[] variables = lineStr.split(ARGS_SEPERATOR); // Split by ','
		Variable currVariable = null;
		for (String varStr : variables) {
			try {
				currVariable = createVariable(type, varStr);
			// Check if the bad value is actually another variable's name
			} catch (BadVariableValueException e) { 
				for (Variable variable : variablesList) {
					if (variable.getName().equals(e.getBadValue())) {
						if (!variable.isInit())
							throw new EmptyVariableException(variable);
						e.getVariable().setValue(variable.getValue());
						currVariable = e.getVariable();
						break;
					}
				} // If not found, check if an outer scope knows this value.				
				if (currVariable == null) {
					Variable v = checkParentScopes(e.getBadValue(),parentScope);
					if (v != null) { // If so, make sure it is initialized
						if (v.isInit()) {
							e.getVariable().setValue(v.getValue());
						} else {
							throw new EmptyVariableException(v);
						}
					} else {
						throw e;
					}
				}
			}
			if (isFinal) // Set as constant if needed.
				currVariable.setFinal();
			variablesList.add(currVariable);
			currVariable = null;
		}
		return variablesList;
	}
	
	/**
	 * @param type The variable type
	 * @param variableArgs The variable line string (without type)
	 * @param isFinal 
	 * @return a variable object.
	 * @throws VariableException
	 */
	private static Variable createVariable(VariableType type, String variableArgs) throws VariableException{
		if (variableArgs == null)
			throw new BadVariableLineException();
		Matcher match = assignPattern.matcher(variableArgs);
		String name = null, val = null;
		if (match.matches()) {
			name = match.group(1);
			try {
				val = match.group(2);
			} catch (Exception e) {
				val = null;
			}
		} else {
			throw new BadVariableLineException(variableArgs);
		}
		Variable variable = new Variable(name,type);
		if (val != null) {
			variable.setValue(val);
		}
		return variable;
	}
	
	/**
	 * Checks all scopes that are in a higher order for variable objects with the given name.
	 * @param name
	 * @param parent
	 * @return
	 */
	private static Variable checkParentScopes(String name,Scope parent) {
		Scope currParent = parent;
		Variable var;
		while (currParent != null) {
			// Check if a variable with the same name exists in the scope.
			var = currParent.getVariables().get(name);
			if (var != null) {
				return var;
			}
			currParent = currParent.getParent();
		}
		return null;
	}
	
	/**
	 * @param lineStr The string of the variable line to check
	 * @return True if the first word is 'final' false otherwise.
	 */
	private static boolean isFinal(String lineStr) {
		String lineStart = isMatch(VariableType.getTypePattern(), lineStr);
		if (lineStart != null)
			return lineStart.equals(VariableType.FINAL.toString());
		return false;
	}
	
	/**
	 * Checks if a certain pattern matches a given string argument.
	 * @param p The regex compiled pattern
	 * @param s The string to match
	 * @return null if the expression is not found in the text, or the string caught by the
	 * 			first capturing group if a match was found.
	 */
	private static String isMatch(Pattern p, String s) {
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
	 * Gets a pattern and a string and returns the string without the first appearance of this pattern.
	 * @param p The pattern to match
	 * @param s The string to trim
	 * @return The given string with an empty string instead of the matched pattern if found, the same
	 * 		   string otherwise
	 */
	private static String trimString(Pattern p, String s) {
		if (isMatch(p,s) != null) {
			return s.replaceFirst(p.pattern(), EMPTY_STRING);
		} else {
			return s;
		}
	}
}