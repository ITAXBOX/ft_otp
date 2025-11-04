package com.ftotp.crypto;

import com.ftotp.exception.InvalidKeyFileException;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

// Represents an encrypted key file containing a seed encrypted with a password.
public final class KeyFile {
	// Magic string to identify the file format and for AEAD associated data.
	private static final String MAGIC = "FTOTP1";
	// Salt for PBKDF2 key derivation.
	private final byte[] salt;
	// IV for AES-GCM encryption.
	private final byte[] iv;
	// Ciphertext of the encrypted seed.
	private final byte[] ct;
	// Parameters used for encryption/decryption.
	private final Params params;

	private KeyFile(byte[] salt, byte[] iv, byte[] ct, Params params) {
		this.salt = salt;
		this.iv = iv;
		this.ct = ct;
		this.params = params;
	}

	// Encrypts the given seed with the provided password and parameters, returning a KeyFile instance.
	// The seed is encrypted using AES-GCM with a key derived from the password using PBKDF2.
	// The salt and IV are randomly generated.
	// The MAGIC string is used as associated data for AEAD.
	// After encryption, the derived key is securely wiped from memory.
	public static KeyFile encrypt(byte[] seed, char[] pass, Params params) {
		byte[] salt = rand(16);
		byte[] iv = rand(12);
		byte[] key = Crypto.deriveKey(pass, salt, params.getPbkdf2Iterations(), params.getAesKeyBytes());
		byte[] ct = Crypto.aesGcmEncrypt(key, iv, seed, MAGIC.getBytes(StandardCharsets.UTF_8));
		Arrays.fill(key, (byte) 0);
		return new KeyFile(salt, iv, ct, params);
	}

	// Decrypts the stored ciphertext using the provided password, returning the original seed.
	public byte[] decrypt(char[] pass) throws Exception {
		byte[] key = Crypto.deriveKey(pass, salt, params.getPbkdf2Iterations(), params.getAesKeyBytes());
		try {
			return Crypto.aesGcmDecrypt(key, iv, ct, MAGIC.getBytes(StandardCharsets.UTF_8));
		} finally {
			Arrays.fill(key, (byte) 0);
		}
	}

	// Serializes the KeyFile to a string format for storage.
	public String serialize() {
		return String.join("\n",
				MAGIC,
				Integer.toString(params.getDigits()),
				Long.toString(params.getTimestepSeconds()),
				params.getHmac(),
				Integer.toString(params.getPbkdf2Iterations()),
				Integer.toString(params.getAesKeyBytes()),
				Base64.getEncoder().encodeToString(salt),
				Base64.getEncoder().encodeToString(iv),
				Base64.getEncoder().encodeToString(ct)) + "\n";
	}

	// Deserializes a KeyFile from the given string format.
	public static KeyFile deserialize(String text) throws InvalidKeyFileException {
		String[] lines = text.replace("\r", "").split("\n");
		if (lines.length < 9 || !MAGIC.equals(lines[0]))
			throw new InvalidKeyFileException("invalid key file format or corrupted data");
		Params p = new Params(
				Integer.parseInt(lines[1]),
				Long.parseLong(lines[2]),
				lines[3],
				Integer.parseInt(lines[4]),
				Integer.parseInt(lines[5]));
		byte[] salt = Base64.getDecoder().decode(lines[6]);
		byte[] iv = Base64.getDecoder().decode(lines[7]);
		byte[] ct = Base64.getDecoder().decode(lines[8]);
		return new KeyFile(salt, iv, ct, p);
	}

	// This method generates a byte array of length n filled with cryptographically secure random bytes.
	public static byte[] rand(int n) {
		byte[] b = new byte[n];
		new SecureRandom().nextBytes(b);
		return b;
	}

	public Params getParams() {
		return params;
	}
}
