package com.ftotp.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class Crypto {
	private Crypto() {

	}

	public static byte[] deriveKey(char[] pass, byte[] salt, int iterations, int keyLenBytes) {
		try {
			PBEKeySpec spec = new PBEKeySpec(pass, salt, iterations, keyLenBytes * 8);
			byte[] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
			spec.clearPassword();
			return key;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

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

	public static byte[] aesGcmDecrypt(byte[] key, byte[] iv, byte[] ciphertext, byte[] aad) throws Exception {
		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
		if (aad != null)
			c.updateAAD(aad);
		return c.doFinal(ciphertext);
	}
}
