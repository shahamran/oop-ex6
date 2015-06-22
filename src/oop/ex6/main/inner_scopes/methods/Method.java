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
	private static Pattern methodCallPattern = Pattern.compile(METHOD_NAME+ARGS+LINE_END),
						   notAllWhitespacePattern = Pattern.compile("\\S");
	
	/**
	 * Constructor
	 * @param newName new method's name
	 * @param newArguments new method's arguments as a list of strings
	 * @param newParent SJavaFile in which method is defined
	 * @param newContent the inner lines of the method scope
	 * @throws IllegalCodeException
	 */
	Method(String newName, String[] newArguments, SJavaFile newParent, List<String> newContent) 
																		throws IllegalCodeException{
		super(newParent, newContent);
		myName = newName;
		arguments = new ArrayList<Variable>();
		if (SJavaFile.isReservedName(myName))
			throw new IllegalMethodNameException("The name '" + myName + "' is reserved.");
		for(String arg : newArguments){
			if (!notAllWhitespacePattern.matcher(arg).find()) 
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
		if (myContent.size() == 0)
			throw new IllegalMethodException("No return statement found");
		String lastLine = myContent.get(myContent.size() - 1);
		if (!SJavaFile.isExactMatch(ValidLine.RETURN_STATEMENT.getPattern(),lastLine)) {
			// If no return; found in the pre-last line of the method
			throw new IllegalMethodException("No return statement found"); 
		}
		super.readScope();
	}

	/**
	 * Method gets the name of the method object
	 * @return myName the name of the method
	 */
	public String getName() {
		return myName;
	}
		
	/**
	 * Method gets the list of arguments of the method object
	 */
	public List<Variable> getArgs(){
		return arguments;
	}
	
	/**
	 * Method tries to read the given line as method call, throws exception if syntax is not legal
	 * @param line - the line to be read as method call line
	 * @param ancestor - the SJavaFile ancestor of the Scope, where method is called
	 * @throws IllegalCodeException
	 */
	public static void handleMethodCall(String line, SJavaFile ancestor) throws IllegalCodeException {
		String name; String[] args;
		Matcher match = methodCallPattern.matcher(line);
		if (!match.matches())
			throw new IllegalMethodCallException(line); // whole line is not a proper method call
		
		name = match.group(1); args = match.group(2).split("\\s*,\\s*");
		//catches separately methods name and the parameters
		String[] emptyString = {};
		args = args[0].equals("") ? emptyString : args;
		Method theMethod = ancestor.getMethod(name);//looks for the method by its name in ancestors' methods
		if (theMethod == null) { //the call was for method that wasn't defined
			throw new IllegalMethodCallException(line);
		}
		
		List<Variable> methodArgs = theMethod.getArgs();
		if (methodArgs.size() != args.length) //not right amount of parameters is given
			throw new IllegalMethodCallException(line);
		
		Variable v, otherVar;
		for (int i = 0; i < methodArgs.size() ; i++) {
			v = methodArgs.get(i);
			if (SJavaFile.isExactMatch(v.getType().getValuePattern(), args[i])) { //parameter fits argument
				continue;
			} else if ( (otherVar = theMethod.getVariable(args[i])) != null) { //found variable given
				if(v.getType()!= otherVar.getType()){//variable doesn't fit the argument by type
					throw new IllegalParamsException(line);
				}
				continue;//parameter fit the arguments
			} else {
				throw new IllegalMethodCallException(line);
			}
		}
	}
	
}
