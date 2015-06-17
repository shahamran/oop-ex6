package oop.ex6.main.methods;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.Scope;

public class MethodFactory {
	
	private static final int INIT_LINE_IDX = 0;
	
	// regex for parsing the first line and the body of the scope
	private static final String SCOPE_START = "\\s*{$", SCOPE_END = "}$",
						RETURN_LINE = "^\\s*(return)\\s*;",
						VOID = "^\\s*(void)", METHOD_NAME = "\\s+(\\b[A-Za-z]+[0-9]*_{0,1}[A-Za-z0-9]+\\b)",
						ARGS_LINE =  "\\s*\\(\\s*[A_Za-z,0-9 ]+\\s*\\)";
						
						

	private static Pattern scopeStartPattern = Pattern.compile(SCOPE_START),
						   scopeEndEndPattern = Pattern.compile(SCOPE_END),
						   returnPattern = Pattern.compile(RETURN_LINE),
						   argsPattern = Pattern.compile(ARGS_LINE),
						   methodNamePattern = Pattern.compile(METHOD_NAME);
						   
	
						   
	
	enum VariableType {INT("int"),DOUBLE("double"),CHAR("char"), //need to have these for parameteres
		       			STRING("String"),BOOLEAN("boolean");
		       			String varStr;
		       			static final Pattern typePattern = Pattern.compile("^[A-Za-z]+");
		       			
		       			VariableType(String newStr){
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
	
	
	/**
	 * Method tries to parse the line as if it was the method initialization line
	 * @param line method line initialization
	 * @return String[] parameters for the creation of the method
	 * @throws IllegalParamsException 
	 * @throws IllegalInitLineException is thrown if the line doesn't stand
	 *  	   by demands of proper initialization line
	 */
	public static ArrayList<String> parseInitLine(String line) throws IllegalMethodNameException,
																	  IllegalParamsException,
																	  IllegalInitLineException{
		ArrayList<String> params = new ArrayList<String>();
		Pattern firstLinePattern = Pattern.compile(VOID+METHOD_NAME+ARGS_LINE+SCOPE_START);
		Matcher lineMatch = firstLinePattern.matcher(line);
		if(lineMatch.matches()){
			//looking for real name
			Matcher nameMatch = methodNamePattern.matcher(line);
			if(nameMatch.find()){
				params.add(nameMatch.group(1));
			}else{
				throw new IllegalMethodNameException();
			}
			//looking for arguments
			Matcher argsMatch = argsPattern.matcher(line);
			if(argsMatch.find()){
				params.add(argsMatch.group(1));
			}else{
				throw new IllegalParamsException();
			}
			
		}else{
			throw new IllegalInitLineException();
		}
		return params;	
	}
	
	/**
	 * Method tries to create new Method
	 * @param parent bigger scope
	 * @param code whole the lines that make up the methods scope
	 * @return Method method mew method created
	 * @throws IllegalMethodException thrown if occured problem with parsing
	 */
	public static Method createMethod(Scope parent, List<String> code) throws IllegalMethodException{
		try{
			ArrayList<String> param = parseInitLine(code.get(INIT_LINE_IDX));
			return new Method(param.get(1), param.get(2).split(","), parent, code);
			
		}catch(IllegalMethodException e){
			throw new IllegalMethodException();
		}
	}
}
