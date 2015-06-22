package oop.ex6.main.variables;

import java.util.*;
import java.util.regex.*;
import oop.ex6.main.Scope;
import oop.ex6.main.SJavaFile;

public class VariableFactory {
	private static final String VARIABLE_ASSIGNMENT = "^\\s*(\\w+)\\s*=\\s*(.+)",
								VARIABLE_DEFINITION = "\\s*(\\w+)\\s*(?:=\\s*(.+))?",
								LINE_END = "\\s*;\\s*$", ARGS_SEPERATOR = "\\s*,\\s*", EMPTY_STRING = "";
	private static Pattern assignPattern = Pattern.compile(VARIABLE_ASSIGNMENT),
						   definePattern = Pattern.compile(VARIABLE_DEFINITION),
						   lineEndPattern = Pattern.compile(LINE_END);
	
	/**
	 * Parses a variable line to define new variables or change variable's value.
	 * If the line fits definition pattern, this method will return all variables defined.
	 * Otherwise, if it was a valid variable assignment line, this method updates the variables changed.
	 * If non of the above was met, an exception will be thrown.
	 * Good lines example: - char myChar = '&'; - myChar = '#'; - char newChar = myChar; -
	 * @param lineStr The variable line string.
	 * @param parentScope The scope object that called this method.
	 * @return A list of variables created by this line, if it was a definition line, null otherwise.
	 * @throws VariableException
	 */
	public static List<Variable> parseVariableLine(String lineStr, Scope parentScope) 
	     															throws VariableException {
		if (SJavaFile.isMatch(lineEndPattern, lineStr) == null) {
			// If a line doesn't end properly (without ;), throw an exception
			throw new BadVariableLineException(lineStr);
		} else {
			lineStr = trimString(lineEndPattern, lineStr); // Remove the ; from the line
		}
		boolean finalVar = isFinal(lineStr);
		if (finalVar) // Remove the 'final' from the line
			lineStr = trimString(VariableType.getTypePattern(), lineStr); 
		// Check if this line is an assignment line, and not definition.
		if ((SJavaFile.isMatch(assignPattern, lineStr)) != null) {
			changeValue(lineStr, parentScope);
			return null;
		}
		VariableType type = getTypeFromLine(lineStr);
		lineStr = trimString(VariableType.getTypePattern(),lineStr); // removes the type string from line
		return createAllVariables(lineStr, type, finalVar, parentScope);
	}
	
	/**
	 * Creates a new variable that acts as if it was initialized. Accepts 'final' values also.
	 * @param argStr The variable string in the format: (*final*) *type* *name* 
	 * @param parentScope The scope that called this method.
	 * @return A variable object in the right type set as initialized.
	 * @throws VariableException If the line/type/name is in invalid format 
	 */
	public static Variable createArgumentVariable(String argStr, Scope parentScope) throws VariableException {
		boolean finalVar = isFinal(argStr);
		if (finalVar) // Remove the 'final' from the line
			argStr = trimString(VariableType.getTypePattern(), argStr); 
		VariableType type = getTypeFromLine(argStr); // Gets the variable type
		argStr = trimString(VariableType.getTypePattern(),argStr); // removes the type string from line
		Variable variable = createVariable(type,argStr,parentScope); // creates the variable
		if (finalVar) 
			variable.setFinal();
		variable.setInit();
		return variable;
	}
	
	public static Variable copyVariable(Variable toCopy) throws VariableException {
		Variable newVar = new Variable(toCopy.getName(), toCopy.getType());
		if (toCopy.isInit())
			newVar.setValue(toCopy.getValue());
		if (toCopy.isFinal())
			newVar.setFinal();
		return newVar;
	}
	
	/**
	 * Extracts the variable type from the definition line.
	 * @param line The string of the definition line - assumes only that it matches the *type* pattern.
	 * @return The variable type as specified by the line string.
	 * @throws NoSuchTypeException If the type is in invalid format.
	 * @throws BadVariableLineException If bad variable definition line was given.
	 */
	private static VariableType getTypeFromLine(String line) throws NoSuchTypeException,
																	BadVariableLineException {
		VariableType myType = null;
		String typeStr = SJavaFile.isMatch(VariableType.getTypePattern(),line);
		if (typeStr == null) // This it isn't a valid variable definition
			throw new BadVariableLineException(line);
		for (VariableType type : VariableType.values()) {
			if (typeStr.equals(type.toString())) {
				myType = type;
				break;
			}
		}
		if (myType == null)
			throw new NoSuchTypeException(typeStr);
		return myType;
	}
	
	/**
	 * Reads the variable line to try and change the variable in it.
	 * @param lineStr The variable line.
	 * @param parentScope The scope in which this line written.
	 * @throws VariableException Thrown if a problem has occurred with changing the variable's value, or
	 * if no such variable exists.
	 */
	private static void changeValue(String lineStr, Scope parentScope) throws VariableException {
		Matcher match = assignPattern.matcher(lineStr);
		if (!match.matches())
			throw new BadVariableLineException(lineStr);
		String name = match.group(1), value = match.group(2);
		Variable variable;
		if ((variable = parentScope.getVariable(name)) == null)  // If no such variable exists.
			throw new BadVariableLineException(lineStr);
		try {
			variable.setValue(value);
		} catch (BadVariableValueException e) {
			Variable other;
			// Check if the bad value assigned is actually another variable
			if ((other = parentScope.getVariable(value)) == null)
				throw e; // if not, throw an exception
			variable.setValue(other.getValue());
		}
	}
	
	/**
	 * Creates a list of variables from a line with the format: *name* (= *value*), ...
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
				currVariable = createVariable(type, varStr, parentScope);
				
			// Check if the bad value is actually another variable's name in the same definition line
			} catch (BadVariableValueException e) {
				String badValue = e.getBadValue();
				for (Variable variable : variablesList) {
					if (variable.getName().equals(badValue)) {
						currVariable = e.getVariable();
						currVariable.setValue(variable.getValue());
						break;
					}
				} // If not found, throw an exception
				throw e;
			} // Try - catch ends here
			if (currVariable == null)
				throw new VariableException(); // Shouldn't be reached.	
			if (isFinal) { // Set as constant if needed.
				if (currVariable.getValue() != null)
					currVariable.setFinal(); /////////////////////////////////////////////////////
			} 
			variablesList.add(currVariable);
			currVariable = null;
		} // For loop ends here.
		return variablesList;
	}
	
	/**
	 * @param type The variable type
	 * @param variableArgs The variable line string (without type)
	 * @param isFinal 
	 * @return a variable object.
	 * @throws VariableException
	 */
	private static Variable createVariable(VariableType type, String variableArgs, Scope parentScope)
																				throws VariableException{
		if (variableArgs == null)
			throw new BadVariableLineException();
		Matcher match = definePattern.matcher(variableArgs);
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
		
		// If there exists another variable with the same name in the same scope
		if (parentScope.getVariables().get(name) != null) {
			throw new VariableNameOverloadException(name);
		}
		Variable variable = new Variable(name,type);
		if (val != null) {
			try { // Tries to assign this variable with a value.
				variable.setValue(val);
			} catch (BadVariableValueException e) { // Checks if the bad value given is
													// another variable name in an outer scope.
				Variable other = parentScope.getVariable(e.getBadValue());
				if (other != null) {
					variable.setValue(other.getValue());
				} else {
					throw e;
				}
			}
		}
		return variable;
	}
	
	/**
	 * @param lineStr The string of the variable line to check
	 * @return True if the first word is 'final' false otherwise.
	 */
	private static boolean isFinal(String lineStr) {
		String lineStart = SJavaFile.isMatch(VariableType.getTypePattern(), lineStr);
		if (lineStart != null)
			return lineStart.equals(VariableType.FINAL.toString());
		return false;
	}
	
	/**
	 * Gets a pattern and a string and returns the string without the first appearance of this pattern.
	 * @param p The pattern to match
	 * @param s The string to trim
	 * @return The given string with an empty string instead of the matched pattern if found, the same
	 * 		   string otherwise
	 */
	private static String trimString(Pattern p, String s) {
		if (SJavaFile.isMatch(p,s) != null) {
			return s.replaceFirst(p.pattern(), EMPTY_STRING);
		} else {
			return s;
		}
	}
}