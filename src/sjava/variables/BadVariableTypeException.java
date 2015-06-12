package sjava.variables;

public class BadVariableTypeException extends VariableException {
	private static final long serialVersionUID = 1L;

	public BadVariableTypeException(String badVal) {
		super("No such type: " + badVal);
	}
	
	public BadVariableTypeException() {
		super("Bad variable type.");
	}

}
