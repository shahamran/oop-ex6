package oop.ex6.main.methods;


public class IllegalInitLineException extends IllegalMethodException {


	private static final long serialVersionUID = 1L;
	
	public IllegalInitLineException() {
		super("Illegal method initialization line");
	}
	
	public IllegalInitLineException(String msg) {
		super(msg);
	}
	

}
