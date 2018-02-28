package util;

import java.util.concurrent.ThreadLocalRandom;

public class IdMaker {
	private static int id = 1;
	public static String next() {
		return "id_"+id++;
	}
	public static String randomId() {
		return "id_"+(ThreadLocalRandom.current().nextInt(id-2)+1);
	}
}
