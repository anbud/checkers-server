package rs.zx.checkers.server.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

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
	
	public static Object fromString(String s) throws IOException, ClassNotFoundException {
		byte[] data = Base64.getDecoder().decode(s);
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o = ois.readObject();
		ois.close();
		
		return o;
	}
	
	public static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		
		return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}

}
