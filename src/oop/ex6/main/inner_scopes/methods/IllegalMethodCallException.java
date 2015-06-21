package oop.ex6.main.inner_scopes.methods;

public class IllegalMethodCallException extends IllegalMethodException {

	private static final long serialVersionUID = 1L;

	public IllegalMethodCallException() {
		super("Illegal method call");
	}
	
	public IllegalMethodCallException(String msg) {
		super(msg);
	}
}
