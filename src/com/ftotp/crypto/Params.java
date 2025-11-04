package com.ftotp.crypto;

public final class Params {
	private final int digits;
	private final long timestepSeconds;
	private final String hmac;
	private final int pbkdf2Iterations;
	private final int aesKeyBytes;

	public Params(int digits, long timestepSeconds, String hmac, int pbkdf2Iterations, int aesKeyBytes) {
		this.digits = digits;
		this.timestepSeconds = timestepSeconds;
		this.hmac = hmac;
		this.pbkdf2Iterations = pbkdf2Iterations;
		this.aesKeyBytes = aesKeyBytes;
	}

	public static Params defaultParams() {
		return new Params(6, 30, "HmacSHA1", 150_000, 32);
	}

	public int getDigits() {
		return digits;
	}

	public long getTimestepSeconds() {
		return timestepSeconds;
	}

	public String getHmac() {
		return hmac;
	}

	public int getPbkdf2Iterations() {
		return pbkdf2Iterations;
	}

	public int getAesKeyBytes() {
		return aesKeyBytes;
	}
}
