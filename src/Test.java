import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oop.ex6.main.variables.*;

public class Test {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		String userInput;
		System.out.println("String to search:");
		while (!(userInput = scanner.nextLine()).equals("end")) {
			foo(userInput);
		}
		
	}
	
	
	private static void foo(String line) {
		try {
			List<Variable> variables = VariableFactory.parseVariableLine(line, null);
			for (Variable v : variables) {
				System.out.println("Name:" + v.getName() + "\nValue:" + v.getValue());
			}
		} catch (VariableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}