package oop.ex6.main.inner_scopes.methods;


public class IllegalParamsException extends IllegalMethodException {


	private static final long serialVersionUID = 1L;

	public IllegalParamsException() {
		super("Illegal Method Parametres");
	}
	
	public IllegalParamsException(String msg) {
		super(msg);
	}

}
