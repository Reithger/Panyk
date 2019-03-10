package controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**	class for creating salt-hash encrypted strings
 * 
 * 
 * @author Regan Lynch
 *
 */
public class Encryptor {
	
	private static final int SALT_LENGTH = 10;

	
	/**	creates a salted-hash HEX representation of a password using SHA-256, using only a password, creating a unique salt
	 * 
	 * @param pass
	 * @return	a string array where index 0 is the hex representation of the salted-hashed password and index 1 is the hex representation of the salt
	 */
	public static String[] createSaltedHash(String pass) {
		String salt = createSalt(pass);
		String saltedPass = salt + pass;
		String hashedSaltedPass = createHash(saltedPass);
		return new String[] {hashedSaltedPass, salt};
	}
	
	/** creates a salted-hash HEX representation of a password using SHA-256, using a password and a given salt
	 * @param pass
	 * @param salt
	 * @return
	 */
	public static String createSaltedHash(String pass, String salt) {
		String saltedPass = salt + pass;
		String hashedSaltedPass = createHash(saltedPass);
		return hashedSaltedPass;
	}
	
	/**		creates the HEX salt for a given password 
	 * @param pass
	 * @return
	 */
	private static String createSalt(String pass) {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[SALT_LENGTH];
		random.nextBytes(bytes);
		return bytesToHex(bytes);

	}
	
	
	/**		creates a HEX value representation of a string using SHA-256 
	 * @param saltedPass
	 * @return
	 */
	private static String createHash(String saltedPass){
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(saltedPass.getBytes(StandardCharsets.UTF_8));		
			return bytesToHex(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	//------------ helper methods ----------------------------------------------------------
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**	converts a byte array to a string HEX representation
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
}
