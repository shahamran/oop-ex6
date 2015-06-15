package oop.ex6.main.variables;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class VariableFactory {
	private static final String VARIABLE_ASSIGNMENT = "\\s*=\\s*", LINE_END = ";$",
								ARGS_SEPERATOR = "\\s*,\\s*";
	private static Pattern assignPattern = Pattern.compile(VARIABLE_ASSIGNMENT),
						   lineEndPattern = Pattern.compile(LINE_END),
						   seperatorPattern = Pattern.compile(ARGS_SEPERATOR);
	enum VariableType {INT("int"),DOUBLE("double"),CHAR("char"),
					   STRING("String"),BOOLEAN("boolean"),FINAL("final");
		String varStr;
		static final Pattern typePattern = Pattern.compile("^[A-Za-z]+");
		
		VariableType(String newStr) {
			varStr = newStr;
		}
		@Override
		public String toString() {
			return varStr;
		}
		public static Pattern getPattern() {
			return typePattern;
		}
	}
	
	private static boolean isFinal(String lineStr) {
		String lineStart = isMatch(VariableType.getPattern(), lineStr);
		if (lineStart != null)
			return lineStart.equals(VariableType.FINAL.toString());
		return false;
	}
	
	private static String isMatch(Pattern p, String s) {
		Matcher m = p.matcher(s);
		return m.find() ? m.group() : null;
	}
	
	
	public List<Variable<?>> parseVariableLine(String lineStr) throws VariableException {
		ArrayList<Variable<?>> variablesList = new ArrayList<Variable<?>>();
		boolean finalVar = isFinal(lineStr);
		if (finalVar)
			lineStr = lineStr.substring(VariableType.FINAL.toString().length(), lineStr.length());
		if (isMatch(lineEndPattern, lineStr) == null) {
			throw new BadVariableLineException(lineStr);
		}
		lineStr = lineStr.substring(0, lineStr.length() - 1);
		String typeStr = isMatch(VariableType.getPattern(),lineStr); VariableType type = null;
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
		try {
			for (String var : variables) {
				try {
				variablesList.add(createVariable(type, var, finalVar));
				} catch (BadValueException e) {
					for (Variable<?> variable : variablesList) {
						if (variable.getName().equals(e.getBadValue())) {
							
							variablesList.addAll(new Variable<cls>)
						}
					}
				}
			}
		} catch (BadValueException e) {
			
		}
		return variablesList;
	}
	
	private Variable<?> createVariable(VariableType type, String variableArgs, boolean isFinal)
																			throws VariableException {
		if (variableArgs == null)
			throw new VariableException();
		String[] args = variableArgs.split(VARIABLE_ASSIGNMENT);
		String name = args[0], val = null;
		if (args.length > 1)
			val = args[1];
		Variable<?> variable = null;
		try {
			switch (type) {
			case INT:
				variable = new Variable<Integer>(name,Integer.parseInt(val));
				break;
			case DOUBLE:
				variable = new Variable<Double>(name,Double.parseDouble(val));
				break;
			case CHAR:
				variable = new Variable<Character>(name,  val.charAt(0));
				break;
			case STRING:
				variable = new Variable<String>(name, val);
				break;
			case BOOLEAN:
				variable = new Variable<Boolean>(name, Boolean.parseBoolean(val));
				break;
				default:
					throw new VariableException();
			}
		} catch (IllegalArgumentException e) {
			throw new BadValueException(val,new Variable<Object>(name));
		} 
		if (isFinal)
			variable.setFinal();
		return variable;
	}
}