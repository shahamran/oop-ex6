package oop.ex6.main;

import oop.ex6.main.parsing.*;

/**
 * The manager module for ex6. Runs all relevant modules created.
 */
public class Sjavac {
	private static final String LEGAL_CODE = "0", ILLEGAL_CODE = "1", ERROR_STR = "2";
	private static final int EXPECTED_NUM_OF_ARGS = 1, FILE_NAME_ARG = 0;
	
	/**
	 * The main file of this project. Runs all modules in perfect sinergy and handle errors.
	 * @param args Exactly one string argument which is the file-path of the sjava file.
	 * Accepts only files that end with .sjava
	 */
	public static void main(String[] args) {
		try {
			if (args.length != EXPECTED_NUM_OF_ARGS)
				throw new IllegalSJavaFileException(); // Must get one argument exactly.
			
			String fileName = args[FILE_NAME_ARG];
			// Parse the file given and create a new SJava object with the content parsed.
			SJavaFile mainFile = new SJavaFile(FileParser.getFileContent(fileName));
			mainFile.readScope();
			handleValidCode(); // If no exception was thrown, this is a valid code.
			
		} catch (IllegalCodeException badCode) { // Handle bad code exceptions.
			handleCodeException(badCode);
		} catch (IllegalSJavaFileException e) { // Handle unexpected errors - bad file, IO exeptions etc.
			handleError(e);
		} catch (Exception e) { // Other unknown weird errors
			e.printStackTrace();
		}
	}
	
	/**
	 * Does whatever needs to be done with valid code: prints 0 to System.out
	 */
	private static void handleValidCode() {
		System.out.println(LEGAL_CODE);
	}
	
	/**
	 * Prints 1 to the System.out and an informative error message to System.err
	 * @param e The code exception thrown
	 */
	private static void handleCodeException(IllegalCodeException e) {
		System.out.println(ILLEGAL_CODE);
		System.err.println(e.getMessage());
	}

	/**
	 * Prints 2 to the System.out and the message from the exception to System.err
	 * @param e The exception thrown
	 */
	private static void handleError(Exception e) {
		System.out.println(ERROR_STR);
		System.err.println(e.getMessage());
	}
}
