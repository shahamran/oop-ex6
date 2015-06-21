package oop.ex6.main;

import oop.ex6.main.parsing.*;


public class Sjavac {
	private static final String LEGAL_CODE = "0", ILLEGAL_CODE = "1", ERROR_STR = "2";
	private static final int EXPECTED_NUM_OF_ARGS = 1;
	
	public static void main(String[] args) {
		try {
			if (args.length != EXPECTED_NUM_OF_ARGS)
				throw new IllegalSJavaFileException(); // Must get one argument exactly.
			String fileName = args[0];
			SJavaFile mainFile = new SJavaFile(FileParser.getFileContent(fileName));
			mainFile.readScope();
			handleValidCode();
		} catch (IllegalSJavaFileException e) {
			handleError(e);
		} catch (IllegalCodeException e) {
			handleCodeException(e);
		} catch (Exception e) { // Other unknown errors
			e.printStackTrace();
		}

	}
	
	private static void handleValidCode() {
		System.out.println(LEGAL_CODE);
	}
	
	private static void handleCodeException(IllegalCodeException e) {
		System.out.println(ILLEGAL_CODE);
		System.err.println(e.getMessage());
	}

	private static void handleError(Exception e) {
		System.out.println(ERROR_STR);
		System.err.println(e.getMessage());
	}
}
