package util;

public class IdMaker {
	private static int currId = 0;
	public static String next() {
		return "id_"+String.valueOf(currId++);
	}
}
