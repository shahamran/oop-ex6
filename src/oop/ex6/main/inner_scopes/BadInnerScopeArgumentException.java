package oop.ex6.main.inner_scopes;

public class BadInnerScopeArgumentException extends IllegalInnerScopeException {
	private static final long serialVersionUID = 1L;

	public BadInnerScopeArgumentException(String badArg) {
		super("Can't define inner scope with argument: " + badArg);
	}
}
