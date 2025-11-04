package com.ftotp.hotp;

import com.ftotp.util.Constants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// HMAC-Based One-Time Password (HOTP) Algorithm
// this class implements RFC 4226
public final class HOTP {
	private HOTP() {

	}
	// Generate an HOTP value
	// it returns a binary value
	// key: secret key, counter: moving factor, hmacAlg: HMAC algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
	public static int generate(byte[] key, long counter, String hmacAlg) {
		try {
			// convert counter to byte array
			byte[] msg = new byte[Constants.HOTP_COUNTER_BYTES];
			for (int i = Constants.HOTP_COUNTER_BYTES - 1; i >= 0; i--) {
				// big-endian
				msg[i] = (byte) (counter & Constants.BYTE_MASK);
				// shift right by 8 bits
				counter >>>= 8;
			}
			// create HMAC hash
			Mac mac = Mac.getInstance(hmacAlg);
			// initialize with key
			mac.init(new SecretKeySpec(key, hmacAlg));
			// compute HMAC
			byte[] h = mac.doFinal(msg);
			// dynamic truncation
			int off = h[h.length - 1] & Constants.TRUNCATION_OFFSET_MASK;
			// extract 4 bytes starting at the offset
			int bin = ((h[off] & Constants.TRUNCATION_MSB_MASK) << 24)
					| ((h[off + 1] & Constants.BYTE_MASK) << 16)
					| ((h[off + 2] & Constants.BYTE_MASK) << 8)
					| (h[off + 3] & Constants.BYTE_MASK);
			return bin;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
