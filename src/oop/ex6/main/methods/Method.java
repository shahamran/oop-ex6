package oop.ex6.main.methods;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.SJavaFile;
import oop.ex6.main.Scope;
import oop.ex6.main.variables.Variable;
import oop.ex6.main.variables.VariableException;
import oop.ex6.main.variables.VariableFactory;

public class Method extends Scope {
	protected String myName;
	protected Variable[] arguments;
	
	private static final String  METHOD_NAME ="\\b([A-Za-z]\\w*)\\b" ,
								 ARGS  = "\\(.*\\);";
	
	private static Pattern argsPattern = Pattern.compile(ARGS),
						   methodNamePattern = Pattern.compile(METHOD_NAME);
			
	enum ValidLine{SINGLE_LINE("^.*;$"),COND_START("{$"), COND_END("^\\s*}$"), METHOD_CALL(METHOD_NAME + ARGS),
					RETURN_LINE("^\\s*\\b(return)\b;$");
	Pattern myRegex;
	ValidLine(String regex) {
		myRegex = Pattern.compile(regex);
	}
	
	Pattern getPattern() {
		return myRegex;
	}
}
	
	protected Method(String newName, String[] newArguments, Scope newParent, List<String> newContent) 
																		throws IllegalMethodException{
		super(newParent, newContent);
		myName = newName;
		try{
			for(String arg : newArguments){
				Variable newVar = VariableFactory.createArgumentVariable(arg);
				this.myVariables.put(newVar.getName(),newVar); 
			}
		}catch(VariableException e){
			throw new IllegalMethodException();
		}
	}

	@Override //exactly as not elegant as the File.readScope()
	public void readScope() throws IllegalMethodException {
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
						
						// Condition newCondition = new Condition(this, myContent.subList(this.scopeStart,i));
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
						throw new IllegalMethodException();
					}
				}
			
				
			}else{ // there are no legal options left outside some scope- the line is illegal
				throw new IllegalMethodException();
			}
			
		}
		
		
		//Actually reading inner scopes
		try{
			if(mySubScopes.size() != 0){
				for(Scope subScope :this.mySubScopes){
					subScope.readScope();
				}
			}
		}catch(IllegalCodeException e){
			throw new IllegalMethodException();
		}

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
							if(theMethod.getVariable(theArgs[i]) != null){ //found variable with such name
								
							}else{ //no variable with such name - consider it constant
								
							}
						}
						return true; //only of every condition was met
					}
				}
			}
		}else{
			return false;
		}
		
	}

}
