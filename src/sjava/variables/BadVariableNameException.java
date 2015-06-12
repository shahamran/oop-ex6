package sjava.variables;

public class BadVariableNameException extends VariableException {
	private static final long serialVersionUID = 1L;

	public BadVariableNameException(String badName) {
		super("Bad variable name: " + badName);
	}
	
	public BadVariableNameException() {
		this("Bad variable name.");
	}
}
