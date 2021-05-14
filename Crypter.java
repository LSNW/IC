import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Crypter {
	private String temp;
	private String activeKey;
	private Keymanager km;
	private final int IVLENGTH = 16;

	public Crypter() {
		km = new Keymanager();
	}

	// Get and Set methods
	public String getActive() {
		return activeKey;
	}

	public void setActive(String newKey) {
		activeKey = newKey;
	}

	// encrypt(imgdata) generates an image by encrypting byte data
	byte[] encrypt(byte[] imgdata) {
		try {
			
			SecretKey key = km.retrieveKey(activeKey);

			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] sequence = new byte[IVLENGTH];
			sr.nextBytes(sequence);
			IvParameterSpec ivSpec = new IvParameterSpec(sequence);

			c.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			byte[] entd = c.doFinal(imgdata);

			// Combines encrypted bytes with byte sequence that forms IV
			ByteArrayOutputStream totalOut = new ByteArrayOutputStream();
			totalOut.write(sequence);
			totalOut.write(entd);
			byte finalEncrypted[] = totalOut.toByteArray();
			
			return finalEncrypted;
		} catch (Exception e) {
			return null;
		}
	}

	// decrypt(entd) generates an image by decrypting byte data
	byte[] decrypt(byte[] entd) throws Exception {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKey key = km.retrieveKey(activeKey);
			
			// Separate the IV from the Encryption
			byte[] sequence = new byte[IVLENGTH];
			byte[] detd = new byte[entd.length - IVLENGTH];
			System.arraycopy(entd, 0, sequence, 0, sequence.length);
			System.arraycopy(entd, sequence.length, detd, 0, detd.length);
			
			IvParameterSpec ivSpec = new IvParameterSpec(sequence);
			c.init(Cipher.DECRYPT_MODE, key, ivSpec);
			byte[] finalDecrypted = c.doFinal(detd);
			
			return finalDecrypted;
		} catch (NullPointerException npe) {
			return null;
		}
	}

}
