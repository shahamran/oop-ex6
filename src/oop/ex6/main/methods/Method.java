package oop.ex6.main.methods;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.main.Scope;
import oop.ex6.main.variables.Variable;
import oop.ex6.main.variables.VariableException;
import oop.ex6.main.variables.VariableFactory;

public class Method extends Scope {
	protected String myName;
	protected String[] arguments;
	
	
	enum ValidLine{SINGLE_LINE("^.*;$"),COND_START("{$"), COND_END("^\\s*}$"), METHOD_CALL("^.*\\(.*\\);"),
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
				List<Variable> newVars = VariableFactory.parseVariableLine(arg, this);
				for(Variable newVar :newVars){ //this is quite useless, because there always will be only one
												//variable, but it is what the method returns, so...
					this.myVariables.put(newVar.getName(),newVar); //FIND A WAY TO MARK AS INITIALIZED!
				}
			}
		}catch(VariableException e){
			throw new IllegalMethodException();
		}
	}

	@Override //exactly not elegant as the File.readScope()
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
			
			if(this.bracketCount > 0){//Some inner scope open
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
					// the line is method call - check it
					
				}else{// not a method call - has to be variable operation
					try{
						VariableFactory.parseVariableLine(line, this);
						continue;
					}catch(VariableException e){
						throw new IllegalMethodException();
					}
				}
			
				
			}else{ // there are no legal options left - the line is illegal
				throw new IllegalMethodException();
			}
			
		}
		
		
		//Actually reading inner scopes
		try{
			for(Scope subScope :this.mySubScopes){
				subScope.readScope();
			}
		}catch(IllegalCodeException e){
			throw new IllegalMethodException();
		}

	}

	public String getName() {
		return myName;
	}
		

	

}
