package oop.ex6.main.variables;

public class VariableNameOverloadException extends BadVariableNameException {
	private static final long serialVersionUID = 1L;

	public VariableNameOverloadException(String badName) {
		super("Can't create more that one variable with the name: " + badName);
	}
	
	public VariableNameOverloadException() {
		super("Can't create more that one variable with the same name.");
	}
}
