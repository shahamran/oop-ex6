package oop.ex6.main.variables;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class VariableFactory {
	private static final String VARIABLE_ASSIGNMENT = "(\\w+)\\s*(?:=\\s*(.+))?", LINE_END = "\\s*;\\s*$",
								ARGS_SEPERATOR = "\\s*,\\s*";
	private static Pattern assignPattern = Pattern.compile(VARIABLE_ASSIGNMENT),
						   lineEndPattern = Pattern.compile(LINE_END);
						   //seperatorPattern = Pattern.compile(ARGS_SEPERATOR);
	
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
		return m.find() ? m.group(1) : null;
	}
	
	/**
	 * Parses a variable line to extract all variable objects from it.
	 * @param lineStr The variable line string
	 * @return A list of variables created by this line.
	 * @throws VariableException
	 */
	public List<Variable> parseVariableLine(String lineStr) throws VariableException {
		ArrayList<Variable> variablesList = new ArrayList<Variable>();
		boolean finalVar = isFinal(lineStr);
		if (finalVar) // // // // \\ \\ \\ \\
			lineStr = lineStr.substring(VariableType.FINAL.toString().length(), lineStr.length());
		if (isMatch(lineEndPattern, lineStr) == null) {
			// If a line doesn't end properly (without ;), throw an exception
			throw new BadVariableLineException(lineStr);
		}
		lineStr = lineStr.substring(0, lineStr.length() - 1);
		String typeStr = isMatch(VariableType.getTypePattern(),lineStr); 
		VariableType type = null;
		if (typeStr == null) 
			throw new VariableException();
		for (VariableType t : VariableType.values()) {
			if (typeStr.equals(t.toString())) {
				type = t;
				lineStr = lineStr.substring(t.toString().length(), lineStr.length());
				break;
			}
		}
		if (type == null)
			throw new NoSuchTypeException(typeStr);
		
		String[] variables = lineStr.split(ARGS_SEPERATOR);
		Variable currVariable = null;
		for (String varStr : variables) {
			try {
				currVariable = createVariable(type, varStr, finalVar);
			} catch (BadValueException e) {
				for (Variable variable : variablesList) {
					if (variable.getName().equals(e.getBadValue())) {
						e.getVariable().setValue(variable.getValue().toString());
						currVariable = e.getVariable();
						break;
					}
				}
				if (currVariable == null)
					System.out.println(); ///////////
			}
			variablesList.add(currVariable);
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
	private Variable createVariable(VariableType type, String variableArgs, boolean isFinal)
																			throws VariableException {
		if (variableArgs == null)
			throw new BadVariableLineException();
		Matcher match = assignPattern.matcher(variableArgs);
		String name = null, val = null;
		if (match.find()) {
			name = match.group(1); val = match.group(3);
		} else {
			throw new BadVariableLineException(variableArgs);
		}
		Variable variable = new Variable(name,type);
		if (val != null) {
			variable.setValue(val);
		}
		
		if (isFinal)
			variable.setFinal();
		return variable;
	}
}