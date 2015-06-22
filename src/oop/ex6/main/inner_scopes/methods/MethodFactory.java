package oop.ex6.main.inner_scopes.methods;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.SJavaFile;
import oop.ex6.main.inner_scopes.*;

public class MethodFactory {
	
	private static final int INIT_LINE_IDX = 0;
	
	// regex for parsing the first line and the body of the scope
	private static final String SCOPE_START = "\\{\\s*$", 
								VOID = "^\\s*(void)\\s+", METHOD_NAME = "([A-Za-z]\\w*)\\s*",
								ARGS_SPLIT = ",";

	private static Pattern methodNamePattern = Pattern.compile(METHOD_NAME),
					  firstLinePattern = Pattern.compile(VOID+METHOD_NAME+InnerScope.ARGS_LINE+SCOPE_START);
						   
	
	
	/**
	 * Method tries to parse the line as if it was the method initialization line
	 * @param line method line initialization
	 * @return String[] parameters for the creation of the method
	 * @throws IllegalParamsException 
	 * @throws IllegalInitLineException is thrown if the line doesn't stand
	 *  	   by demands of proper initialization line
	 */
	private static List<String> parseMethodInitLine(String line) throws IllegalMethodNameException,
																	  IllegalParamsException,
																	  IllegalInitLineException{
		ArrayList<String> params = new ArrayList<String>();
		Matcher lineMatch = firstLinePattern.matcher(line);
		if(lineMatch.matches()){
			//looking for real name
			Matcher nameMatch = methodNamePattern.matcher(line);
			if(nameMatch.find()){
				params.add(lineMatch.group(2)); //first will be void, second - the name
			}else{
				throw new IllegalMethodNameException(line); //shouldn't actually happen - already checked
			}
			//looking for arguments
			Matcher argsMatch = InnerScope.argsPattern.matcher(line);
			if(argsMatch.find()){
				params.add(argsMatch.group(1)); // should capture what is inside the brackets
			}else{
				throw new IllegalParamsException(line);
			}
			
		}else{
			throw new IllegalInitLineException(line);
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
	public static Method createMethod(SJavaFile parent, List<String> code) throws IllegalCodeException {
		List<String> param = parseMethodInitLine(code.get(INIT_LINE_IDX));
		String newName = param.get(0);
		String[] newArgs = param.get(1).split(ARGS_SPLIT);
		List<String> newContent = code.subList(1, code.size() - 1);
		return new Method(newName,newArgs, parent, newContent);
	}
}
