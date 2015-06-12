package sjava.variables;

import sjava.SJavaException;

public abstract class VariableException extends SJavaException {
	private static final long serialVersionUID = 1L;

	public VariableException(String msg) {
		super(msg);
	}

}
