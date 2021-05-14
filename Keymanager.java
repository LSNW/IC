/*
Keymanager.java deals with keys in the keystore
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Keymanager {
	private final char[] defPass = "i".toCharArray();
	private char[] curPass;
	private final char[] storePass = "i".toCharArray();

	// checkIdentifiers() reads Identifers.txt and creates keys if they had not already existed
	ArrayList<String> checkIdentifiers() throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		try {
			java.io.FileInputStream fis = new FileInputStream("Keys");
			keyStore.load(fis, storePass);
		} catch (IOException iox) {
			return null;
		}

		ArrayList<String> idents = new ArrayList<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("Identifiers.txt"));
			String iden = in.readLine();
			while (iden != null) {
				idents.add(iden);
				if (!(keyStore.containsAlias(iden))) {
					generateKey(iden);
				}
				iden = in.readLine();
			}
			in.close();
		} catch (IOException iox) {
			File newId = new File("Identifiers.txt");
			newId.createNewFile();
		}
		
		return idents;
	}

	// generateKey(alias) generates a unique 256-bit key and gives it the name alias
	SecretKey generateKey(String alias) throws Exception {
		KeyGenerator kg;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(256);
			SecretKey key = kg.generateKey();
			storeKey(key, alias);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	// storeKey(key, alias) stores a key
	void storeKey(SecretKey key, String alias) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(storePass);
		java.io.FileInputStream fis = new FileInputStream("Keys");
		keyStore.load(fis, storePass);
		java.io.FileOutputStream fos = new java.io.FileOutputStream("Keys");
		KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
		keyStore.setEntry(alias, secretKeyEntry, protectionParam);
		keyStore.store(fos, defPass);
	}

	// retrieveKey(alias) retrieves the key with the name alias
	SecretKey retrieveKey(String alias) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		java.io.FileInputStream fis = new FileInputStream("Keys");
		keyStore.load(fis, storePass);
		SecretKey resKey;
		curPass = ImCrypter.getPass().toCharArray();
		
		while (true) {
			try {
				resKey = (SecretKey) keyStore.getKey(alias, curPass);
				return resKey;
			} catch (UnrecoverableKeyException e) {
				ImCrypter.setUpdate("Incorrect Key Password");
				curPass = ImCrypter.getPass().toCharArray();
			}
		}
	}
}
