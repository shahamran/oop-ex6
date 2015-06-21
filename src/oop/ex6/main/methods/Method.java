package oop.ex6.main.methods;

import java.util.*;
import java.util.regex.*;

import oop.ex6.main.*;
import oop.ex6.main.variables.*;

public class Method extends Scope {
	private String myName;
	private static SJavaFile myParent;
	private Variable[] arguments;
	
	private static final String  METHOD_NAME ="^\\s*([A-Za-z]\\w*)\\b" ,
								 ARGS  = "\\((.*)\\)";
	
	private static Pattern argsPattern = Pattern.compile(ARGS),
						   methodNamePattern = Pattern.compile(METHOD_NAME),
						   methodCallPattern = Pattern.compile(METHOD_NAME+ARGS);
			
	enum ValidLine{SINGLE_LINE(";$"),COND_START("\\{\\s*$"), COND_END("^\\s*\\}\\s*$"), 
					METHOD_CALL(METHOD_NAME + ARGS), RETURN_LINE("^\\s*\\b(return)\b;$");
	Pattern myRegex;
	ValidLine(String regex) {
		myRegex = Pattern.compile(regex);
	}
	
	Pattern getPattern() {
		return myRegex;
	}
}
	
	protected Method(String newName, String[] newArguments, SJavaFile newParent, List<String> newContent) 
																		throws IllegalCodeException{
		super(newParent, newContent);
		myName = newName;
		try{
			for(String arg : newArguments){
				if (!Pattern.compile("\\S").matcher(arg).find())
					continue;
				Variable newVar = VariableFactory.createArgumentVariable(arg,this);
				this.myVariables.put(newVar.getName(),newVar); 
			}
		}catch(VariableException e){
			throw e;
		}
	}

	@Override //exactly as not elegant as the File.readScope()
	public void readScope() throws IllegalMethodException, VariableException {
		Matcher match;
		
		//check if ended with return statement
		String lastLine = myContent.get(myContent.size() - 1);
		match = ValidLine.SINGLE_LINE.getPattern().matcher(lastLine);
		if (!match.find()) {
			throw new IllegalMethodException();
		}
		
		//Creating variables and inner Scopes
		for (int i = 0; i < myContent.size() - 1; i++) {
			
			String line = myContent.get(i);
			
			if(this.bracketCount > 0){//Some inner scope is open
				match = ValidLine.COND_END.getPattern().matcher(line);
				if (match.find()) {
					if (this.bracketCount > 1) { // more then one scope open 
						this.bracketCount--; // close one scope
						continue;
					} else if (bracketCount == 1) { // exactly one not closed scope left
						
						//Condition newCondition = new Condition(this, myContent.subList(this.scopeStart,i));
						//this.mySubScopes.add(newCondition); 
						this.bracketCount --;
						this.scopeStart = 0;
						continue;
					}
					
				} else {
					continue;
				}
			}
			
			match = ValidLine.COND_START.getPattern().matcher(line);
			if(match.find()) {
				//new conditional scope is open
				this.scopeStart = i;
				this.bracketCount ++;
				continue;
			}
			
			match = ValidLine.SINGLE_LINE.getPattern().matcher(line);
			if (match.find()) {
				 //regular line found
				
				match = ValidLine.METHOD_CALL.getPattern().matcher(line);
				if(match.find()){
					if(!isLegalCall(line)){
						throw  new IllegalMethodCallException();
					}else{
						continue;
					}
					
				}else{// not a method call - has to be variable operation
					try{
						VariableFactory.parseVariableLine(line, this);
						continue;
					}catch(VariableException e){
						throw e;
					}
				}
			
				
			}else{ // there are no legal options left outside some scope- the line is illegal
				throw new IllegalMethodException();
			}
			
		}
		
		
		//Actually reading inner scopes

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
	public Variable[] getArgs(){
		return arguments;
	}
	
	
	
	public static void handleMethodCall(String line) throws IllegalMethodCallException {
		String name; String[] args;
		Matcher match = methodCallPattern.matcher(line);
		if (!match.find())
			throw new IllegalMethodCallException(line);
		
		name = match.group(1); args = match.group(2).split("\\s*,\\s*");
		Method theMethod = myParent.getMethod(name);
		if (theMethod == null) {
			throw new IllegalMethodCallException(line);
		}
		
		Variable[] methodArgs = theMethod.getArgs();
		if (methodArgs.length != args.length)
			throw new IllegalMethodCallException(line);
		
		Variable v;
		for (int i = 0; i < methodArgs.length ; i++) {
			v = methodArgs[i];
			if (SJavaFile.isMatch(v.getType().getValuePattern(), args[i]) != null) {
				continue;
			} else if (theMethod.getVariable(args[i]) != null) {
				continue;
			} else {
				throw new IllegalMethodCallException(line);
			}
		}
	}
	
	/**
	 * This method checks if the line is legal method call
	 * @param methodCall
	 * @return
	 */
	private boolean isLegalCall(String methodCall) {
		Matcher match = methodNamePattern.matcher(methodCall);
		if(match.find()){
			String methodName = match.group(1);
			Method theMethod = ((SJavaFile) myParent).getMethod(methodName); //looking for method
			if(theMethod != null){ // method found with such name
				Variable[] methodArgs = theMethod.getArgs();
				match = argsPattern.matcher(methodCall);
				if(match.find()){ // checking arguments
					String[] theArgs = match.group(1).split(",");
					if(theArgs.length == methodArgs.length){//the amount of arguments is legal
						for(int i = 0; i < theArgs.length ; i ++){
							Variable theVar = theMethod.getVariable(theArgs[i]);
							if(theVar != null){ //found variable with such name
								if(theVar.getType().equals(methodArgs[i].getType())){ //variable match1es the type of the argument
									continue;
								} else {
									return false; //type doesn't fit
								}
							}else{ //no variable with such name - consider it constant
								if(methodArgs[i].getType().getValuePattern().matcher(theArgs[i]).find()){
									continue; //constant matches the type of the argument
									
								}else{
									return false;
								}
							}
						}
						return true; //only of every condition was met for every parameter
					}else{
						return false;// wrong amount of arguments
					}
				}else{
					return false;// no arguments were found
				}
			}else{
				return false;//call for not existing method
			}
		}else{
			return false;
		}
		
	}
	

}
