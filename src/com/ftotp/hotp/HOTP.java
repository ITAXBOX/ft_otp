package com.ftotp.hotp;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HOTP {
	private HOTP() {

	}

	public static int generate(byte[] key, long counter, String hmacAlg) {
		try {
			byte[] msg = new byte[8];
			for (int i = 7; i >= 0; i--) {
				msg[i] = (byte) (counter & 0xff);
				counter >>>= 8;
			}
			Mac mac = Mac.getInstance(hmacAlg);
			mac.init(new SecretKeySpec(key, hmacAlg));
			byte[] h = mac.doFinal(msg);
			int off = h[h.length - 1] & 0x0f;
			int bin = ((h[off] & 0x7f) << 24)
					| ((h[off + 1] & 0xff) << 16)
					| ((h[off + 2] & 0xff) << 8)
					| (h[off + 3] & 0xff);
			return bin;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
