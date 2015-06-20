package oop.ex6.main;

import oop.ex6.main.parsing.*;


public class Sjavac {
	private static final String LEGAL_CODE = "0", ILLEGAL_CODE = "1", ERROR_STR = "2";
	
	public static void main(String[] args) {
		try {
			String fileName = args[0];
			FileParser parser = new FileParser(fileName);
			SJavaFile mainFile = new SJavaFile(parser.getFileContent());
			mainFile.readScope();
			handleValidCode();
		} catch (ArrayIndexOutOfBoundsException|IllegalSJavaFileException e) {
			handleIOException(e);
		} catch (IllegalCodeException e) {
			handleCodeException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void handleValidCode() {
		System.out.println(LEGAL_CODE);
	}
	
	private static void handleIOException(Exception e) {
		System.out.println(ERROR_STR);
		e.printStackTrace();
	}
	
	private static void handleCodeException(IllegalCodeException e) {
		System.out.println(ILLEGAL_CODE);
		e.printStackTrace();
	}

}
