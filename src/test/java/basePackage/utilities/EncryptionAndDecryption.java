package basePackage.utilities;

import org.apache.commons.codec.binary.Base64;

public class EncryptionAndDecryption {

	public static String encryption(String text) {
		byte[] encodedBytes = Base64.encodeBase64(text.getBytes());
		System.out.println("encodedBytes " + new String(encodedBytes));
		return new String(encodedBytes);

	}

	public static String decryption(String encryptedtext) {
		byte[] decodedBytes = Base64.decodeBase64(encryptedtext);
		System.out.println("decodedBytes " + new String(decodedBytes));
		return new String(decodedBytes);
	}

	public static void main(String[] args) {
		String text = "National123!";
		String encryptedtext = encryption(text);
		System.out.println("The Encrypted text is " + encryptedtext);
		String decryptedtext = decryption(encryptedtext);
		System.out.println("The decrypted text is " + decryptedtext);

	}

}