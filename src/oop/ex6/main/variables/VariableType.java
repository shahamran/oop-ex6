package oop.ex6.main.variables;

import java.util.regex.Pattern;

public enum VariableType {INT("int"),DOUBLE("double"),CHAR("char"),
						  STRING("String"),BOOLEAN("boolean"),FINAL("final");
	String varStr;
	static final Pattern typePattern = Pattern.compile("^[A-Za-z]+");

	VariableType(String newStr) {
		varStr = newStr;
	}
	@Override
	public String toString() {
		return varStr;
	}
	public static Pattern getPattern() {
		return typePattern;
	}
}
