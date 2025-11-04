package com.ftotp.crypto;

import com.ftotp.util.Constants;

// Class to hold cryptographic parameters
// digits - number of digits in the OTP
// timestepSeconds - time step in seconds for TOTP
// hmac - HMAC algorithm to use
// pbkdf2Iterations - number of iterations for PBKDF2
// aesKeyBytes - size of AES key in bytes
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
		return new Params(
			Constants.DEFAULT_OTP_DIGITS,
			Constants.DEFAULT_TIMESTEP_SECONDS,
			Constants.DEFAULT_HMAC_ALGORITHM,
			Constants.DEFAULT_PBKDF2_ITERATIONS,
			Constants.DEFAULT_AES_KEY_BYTES
		);
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
