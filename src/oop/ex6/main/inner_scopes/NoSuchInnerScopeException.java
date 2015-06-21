package oop.ex6.main.inner_scopes;

public class NoSuchInnerScopeException extends IllegalInnerScopeException {
	private static final long serialVersionUID = 1L;
	
	public NoSuchInnerScopeException(String innerScopeName) {
		super("No such inner scope name: " + innerScopeName);
	}

}
