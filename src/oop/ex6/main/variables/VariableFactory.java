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
	public static List<Variable> parseVariableLine(String lineStr, Scope parentScope) throws VariableException {
		ArrayList<Variable> variablesList = new ArrayList<Variable>(); // The output list
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
		
		String[] variables = lineStr.split(ARGS_SEPERATOR);
		Variable currVariable = null;
		for (String varStr : variables) {
			try {
				currVariable = createVariable(type, varStr);
			} catch (BadVariableValueException e) {
				for (Variable variable : variablesList) {
					if (variable.getName().equals(e.getBadValue())) {
						if (!variable.isInit())
							throw new EmptyVariableException(variable);
						e.getVariable().setValue(variable.getValue());
						currVariable = e.getVariable();
						break;
					}
				}				
				if (currVariable == null) {
					Variable v = checkParentScopes(e.getBadValue(),parentScope);
					if (v != null) {
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
			if (finalVar)
				currVariable.setFinal();
			variablesList.add(currVariable);
			currVariable = null;
		}
		return variablesList;
	}
	
	/**
	 * Checks all scopes that are in a higher order for variable objects with the given name.
	 * @param name
	 * @param parent
	 * @return
	 */
	private static Variable checkParentScopes(String name,Scope parent) {
		Scope currParent = parent;
		while (currParent != null) {
			for (Variable v : currParent.getVariables()) {
				if (name.equals(v.getName()))
					return v;
			}
			currParent = currParent.getParent();
		}
		return null;
	}
	
	/**
	 * @param type The variable type
	 * @param variableArgs The variable line string (without type)
	 * @param isFinal 
	 * @return a variable object.
	 * @throws VariableException
	 */
	private static Variable createVariable(VariableType type, String variableArgs) throws VariableException {
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
	
	private static String trimString(Pattern p, String s) {
		if (isMatch(p,s) != null) {
			return s.replaceFirst(p.pattern(), EMPTY_STRING);
		} else {
			return s;
		}
	}
}