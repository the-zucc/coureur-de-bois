package util;

public class IdMaker {
	private static int id;
	public static String next() {
		return "id_"+id++;
	}
}
