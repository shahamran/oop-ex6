import java.util.*;
import java.util.regex.*;
import java.io.*;
import oop.ex6.main.Sjavac;

public class TestEX6 {
	//private static final String TEST_LOCATION = "C:\\Cygwin\\home\\Ran\\safe\\oop-ex6\\tests\\tests\\";
	//private static final String TESTS_FILE = "C:\\Cygwin\\home\\Ran\\safe\\oop-ex6\\tests\\sjavac_tests.txt";
	private static final String TEST_LOCATION = "/cs/stud/ransha/safe/oop-ex6/tests/tests/";
	private static final String TESTS_FILE = "/cs/stud/ransha/safe/oop-ex6/tests/sjavac_tests.txt";
	
	private static int failed = 0;
	private static ArrayList<String[]> tests = new ArrayList<String[]>();
	private static Pattern testAll = Pattern.compile("^(\\w+\\d+\\.sjava)\\s*(\\w+)\\s*(.*)");
	
	
	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		System.out.println("Enter a test number to run a specific test,\n" +
						   "or enter 'all' to run all tests:");
		String testNum = userInput.nextLine();
		userInput.close();
		
		Pattern myPattern;
		switch (testNum) {
		case "all":
			myPattern = testAll;
			break;
			default:
				while (testNum.length() < 3)
					testNum = "0" + testNum;
				myPattern = Pattern.compile("^(test" + testNum + "\\.sjava)\\s*(\\d)\\s*(.*)");
		}
		parseTestsFile(new File(TESTS_FILE),myPattern);
		System.out.print("Running " + tests.size() + " tests:");
		for (int i = 0; i < tests.size(); i++) {
			if (i % 15 == 0)
				System.out.println();
			String[] currTest = tests.get(i);
			System.out.print(".");
			runTest(currTest);
		}
		System.out.println("Failed " + failed + " tests");
		System.out.println("Finish.");
	}
	
	/**
	 * @param testsFile The tests file which contains what results are expected.
	 * @param testPattern Which test to pick.
	 */
	private static void parseTestsFile(File testsFile, Pattern testPattern) {
		tests = new ArrayList<String[]>();
		try (
			FileReader reader = new FileReader(testsFile);
			BufferedReader br = new BufferedReader(reader);
			Scanner fileScanner = new Scanner(br)
			 ) {
			String line;
			while (fileScanner.hasNext()) {
				line = fileScanner.nextLine();
				Matcher m = testPattern.matcher(line);
				if (m.matches()) {
					String[] newLine = {m.group(1),m.group(2),m.group(3)};
					tests.add(newLine);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void runTest(String[] testLine) {
		String fileName = testLine[0],expectedResult = testLine[1], msg = testLine[2],
			   actualResult = "", err = "";		
		PrintStream origOut = System.out;
		PrintStream origErr = System.err;
		try (final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
			 final ByteArrayOutputStream errContent = new ByteArrayOutputStream();) {
			System.setOut(new PrintStream(outContent));
			System.setErr(new PrintStream(errContent));		
			/////Start reading\\\\
			
			String[] pathName = {TEST_LOCATION + fileName};
			Sjavac.main(pathName);
			actualResult = outContent.toString();
			Matcher m = Pattern.compile("(\\S)").matcher(actualResult);
			actualResult = m.find() ? m.group(1) : actualResult;
			err = errContent.toString();

			/////Stop reading\\\\			
			System.setOut(origOut);
			System.setErr(origErr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if  (!expectedResult.equals(actualResult)) {
			System.out.println("\nTest:" + fileName + "\nDescription:" + msg);
			System.err.println("Expected: " + expectedResult +". Got: " + actualResult);
			System.err.println("Error message received:\n" + err);
			failed++;
			//System.exit(0);
		}
	}
}