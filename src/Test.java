import java.util.*;

import oop.ex6.main.*;
import oop.ex6.main.variables.*;
import oop.ex6.main.methods.*;

public class Test {
	private static Scanner scanner = new Scanner(System.in);
	private static Scope s = new SJavaFile(new ArrayList<String>());
	
	public static void main(String[] args) {
		String userInput;
		
		System.out.println("String to search:");
		while (!(userInput = scanner.nextLine()).equals("end")) {
			foo(userInput);
		}
		
	}
	
	
	private static void foo(String line) {
		try {
			List<Variable> variables = VariableFactory.parseVariableLine(line, s);
			for (Variable v : variables) {
				String val = v.isInit() ? v.getValue() : null;
				System.out.println("Name:" + v.getName() + "\nValue:" + val);
				s.getVariables().put(v.getName(), v);
			}
		} catch (VariableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}