package sjava.variables;

public class BadVariableAssignmentException extends VariableException {
	private static final long serialVersionUID = 1L;

	public BadVariableAssignmentException(String typeName, String badVal) {
		super("Can't assign " + typeName + " type with value: " + badVal);
	}

}
