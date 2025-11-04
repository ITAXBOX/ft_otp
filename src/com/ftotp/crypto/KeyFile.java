package com.ftotp.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public final class KeyFile {
	private static final String MAGIC = "FTOTP1";

	private final byte[] salt;
	private final byte[] iv;
	private final byte[] ct;
	private final Params params;

	private KeyFile(byte[] salt, byte[] iv, byte[] ct, Params params) {
		this.salt = salt;
		this.iv = iv;
		this.ct = ct;
		this.params = params;
	}

	public static KeyFile encrypt(byte[] seed, char[] pass, Params params) {
		byte[] salt = rand(16);
		byte[] iv = rand(12);
		byte[] key = Crypto.deriveKey(pass, salt, params.getPbkdf2Iterations(), params.getAesKeyBytes());
		byte[] ct = Crypto.aesGcmEncrypt(key, iv, seed, MAGIC.getBytes(StandardCharsets.UTF_8));
		Arrays.fill(key, (byte) 0);
		return new KeyFile(salt, iv, ct, params);
	}

	public byte[] decrypt(char[] pass) throws Exception {
		byte[] key = Crypto.deriveKey(pass, salt, params.getPbkdf2Iterations(), params.getAesKeyBytes());
		try {
			return Crypto.aesGcmDecrypt(key, iv, ct, MAGIC.getBytes(StandardCharsets.UTF_8));
		} finally {
			Arrays.fill(key, (byte) 0);
		}
	}

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

	public static KeyFile deserialize(String text) throws Exception {
		String[] lines = text.replace("\r", "").split("\n");
		if (lines.length < 9 || !MAGIC.equals(lines[0]))
			throw new Exception("invalid key file");
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

	public static byte[] rand(int n) {
		byte[] b = new byte[n];
		new SecureRandom().nextBytes(b);
		return b;
	}

	public Params getParams() {
		return params;
	}
}
