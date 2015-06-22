package oop.ex6.main.inner_scopes.methods;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.*;
import oop.ex6.main.inner_scopes.InnerScope;
import oop.ex6.main.variables.*;

public class Method extends InnerScope {
	private String myName;
	private List<Variable> arguments;
	
	private static final String  METHOD_NAME ="^\\s*([A-Za-z]\\w*)\\s*" ,
								 ARGS  = "\\((.*)\\)", LINE_END = "\\s*;\\s*$";
	private static Pattern methodCallPattern = Pattern.compile(METHOD_NAME+ARGS+LINE_END);
	
	protected Method(String newName, String[] newArguments, SJavaFile newParent, List<String> newContent) 
																		throws IllegalCodeException{
		super(newParent, newContent);
		myName = newName;
		arguments = new ArrayList<Variable>();
		
		for(String arg : newArguments){
			if (!Pattern.compile("\\S").matcher(arg).find()) /////////////////////////////////////////////////////
				continue;
			Variable newVar = VariableFactory.createArgumentVariable(arg,this);
			arguments.add(newVar); 
		}
		for (Variable v : arguments) {
			if (myVariables.get(v.getName()) != null)
				throw new IllegalParamsException();
			myVariables.put(v.getName(), v);
		}
	}
	
	@Override
	public void readScope() throws IllegalCodeException {
		String lastLine = myContent.get(myContent.size() - 1);
		if (!SJavaFile.isExactMatch(ValidLine.RETURN_STATEMENT.getPattern(),lastLine)) {
			throw new IllegalMethodException(); // If no return; found in the pre-last line of the method
		}
		super.readScope();
	}

	/**
	 * This method gets the name of the method object
	 * @return myName the name of the method
	 */
	public String getName() {
		return myName;
	}
		
	/**
	 * 
	 */
	public List<Variable> getArgs(){
		return arguments;
	}
	
	
	public static void handleMethodCall(String line, SJavaFile ancestor) throws IllegalCodeException {
		String name; String[] args;
		Matcher match = methodCallPattern.matcher(line);
		if (!match.matches())
			throw new IllegalMethodCallException(line);
		
		name = match.group(1); args = match.group(2).split("\\s*,\\s*");
		String[] emptyString = {};
		args = args[0].equals("") ? emptyString : args;
		Method theMethod = ancestor.getMethod(name);
		if (theMethod == null) {
			throw new IllegalMethodCallException(line);
		}
		
		List<Variable> methodArgs = theMethod.getArgs();
		if (methodArgs.size() != args.length)
			throw new IllegalMethodCallException(line);
		
		Variable v;
		for (int i = 0; i < methodArgs.size() ; i++) {
			v = methodArgs.get(i);
			if (SJavaFile.isExactMatch(v.getType().getValuePattern(), args[i])) {
				continue;
			} else if (theMethod.getVariable(args[i]) != null) {
				continue;
			} else {
				throw new IllegalMethodCallException(line);
			}
		}
	}
	
}
