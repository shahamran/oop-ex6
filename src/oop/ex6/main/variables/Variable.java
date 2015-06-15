package oop.ex6.main.variables;

public class Variable<T> {
	private boolean isFinal = false, isInit = false;
	private String myName;
	private T myVal;
	
	
	public Variable(String newName) {
		myName = newName;
	}
	
	public Variable(String newName, T newValue) {
		this(newName);
		if (!(newValue == null)) {
			setValue(newValue);
		}
	}
	
	public String getName() {
		return myName;
	}
	
	public T getValue() {
		return myVal;
	}
	
	public boolean isInit() {
		return isInit;
	}
	
	public void setValue(T newVal) {
		if (isFinal)
			return;
		myVal = newVal;
		isInit = true;
	}
	
	public void setFinal() {
		isFinal = true;
	}
}
