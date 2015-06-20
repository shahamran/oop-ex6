package oop.ex6.main.methods;


public class IllegalMethodNameException extends IllegalMethodException {

	private static final long serialVersionUID = 1L;
	
	public IllegalMethodNameException() {
		super("Illegal Method Name");
	}
	
	public IllegalMethodNameException(String msg) {
		super(msg);
	}

}
