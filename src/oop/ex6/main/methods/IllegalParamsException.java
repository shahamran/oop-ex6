package oop.ex6.main.methods;

import oop.ex6.main.IllegalCodeException;

public class IllegalParamsException extends IllegalMethodException {


	private static final long serialVersionUID = 1L;

	public IllegalParamsException() {
		super("Illegal Method Parametres");
	}
	
	public IllegalParamsException(String msg) {
		super(msg);
	}

}
