import java.util.Scanner;
import java.util.regex.*;

public class RegexTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		//Pattern p = Pattern.compile("(?:\\|\\|)|(?:&&)");
		for (String s : scanner.nextLine().split("(?:\\|\\|)|(?:&&)")) {
			System.out.println(s);
		}
		scanner.close();
	}
}
