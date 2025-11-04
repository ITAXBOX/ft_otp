package com.ftotp.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class Crypto {
	private Crypto() {

	}
	// Derives a key using PBKDF2 with HMAC-SHA256
	// pass: password, salt: salt, iterations: number of iterations, keyLenBytes: desired key length in bytes
	// Returns the derived key
	public static byte[] deriveKey(char[] pass, byte[] salt, int iterations, int keyLenBytes) {
		try {
			// Create PBE key specification, its used to derive the key
			PBEKeySpec spec = new PBEKeySpec(pass, salt, iterations, keyLenBytes * 8);
			// PBKDF2 with HMAC-SHA256
			// Generate the key
			byte[] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
			spec.clearPassword();
			return key;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// AES-GCM encryption
	// key: encryption key, iv: initialization vector, plaintext: data to encrypt, aad: additional authenticated data
	// Returns the ciphertext
	public static byte[] aesGcmEncrypt(byte[] key, byte[] iv, byte[] plaintext, byte[] aad) {
		try {
			Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
			if (aad != null)
				c.updateAAD(aad);
			return c.doFinal(plaintext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// AES-GCM decryption
	// key: decryption key, iv: initialization vector, ciphertext: data to decrypt, aad: additional authenticated data
	// Returns the plaintext
	public static byte[] aesGcmDecrypt(byte[] key, byte[] iv, byte[] ciphertext, byte[] aad) throws Exception {
		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
		if (aad != null)
			c.updateAAD(aad);
		return c.doFinal(ciphertext);
	}
}
