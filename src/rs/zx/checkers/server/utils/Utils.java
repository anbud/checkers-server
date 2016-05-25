package rs.zx.checkers.server.utils;

import java.security.SecureRandom;

public class Utils {
	//taken from stackoverflow
	public static String randomId(int length) {
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom random = new SecureRandom();

		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++ ) 
			sb.append(AB.charAt(random.nextInt(AB.length())));
		
		return sb.toString();
	}
}
