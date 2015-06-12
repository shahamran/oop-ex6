public class Test {
	public static void main(String[] args) {
		double val;
		try {
			val = Double.parseDouble(args[0]);
			System.out.println("Success: " + val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}