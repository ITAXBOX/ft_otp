package com.ftotp.uri;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// Builder class for constructing OTP URIs for TOTP
// Follows the otpauth URI format used by authenticator apps
public final class OtpUri {
	private String label;
	private String secret;
	private String issuer;
	private String algorithm;
	private int digits;
	private int period;

	private OtpUri() {
		this.algorithm = "SHA1";
		this.digits = 6;
		this.period = 30;
	}

	public static OtpUri builder() {
		return new OtpUri();
	}

	public OtpUri label(String label) {
		this.label = label;
		return this;
	}

	public OtpUri secret(byte[] secretBytes) {
		this.secret = base32Encode(secretBytes);
		return this;
	}

	public OtpUri issuer(String issuer) {
		this.issuer = issuer;
		return this;
	}

	public OtpUri algorithm(String algorithm) {
		this.algorithm = algorithm;
		return this;
	}

	public OtpUri digits(int digits) {
		this.digits = digits;
		return this;
	}

	public OtpUri period(int period) {
		this.period = period;
		return this;
	}

	public String build() {
		if (label == null || secret == null) {
			throw new IllegalStateException("Label and secret are required");
		}

		StringBuilder uri = new StringBuilder("otpauth://totp/");
		
		uri.append(urlEncode(label));
		
		uri.append("?secret=").append(secret);
		
		if (issuer != null) {
			uri.append("&issuer=").append(urlEncode(issuer));
		}
		
		uri.append("&algorithm=").append(algorithm);
		uri.append("&digits=").append(digits);
		uri.append("&period=").append(period);
		
		return uri.toString();
	}

	private static String base32Encode(byte[] data) {
		final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
		StringBuilder result = new StringBuilder();
		int buffer = 0;
		int bitsLeft = 0;
		
		for (byte b : data) {
			buffer = (buffer << 8) | (b & 0xFF);
			bitsLeft += 8;
			
			while (bitsLeft >= 5) {
				int index = (buffer >> (bitsLeft - 5)) & 0x1F;
				result.append(BASE32_CHARS.charAt(index));
				bitsLeft -= 5;
			}
		}
		
		if (bitsLeft > 0) {
			int index = (buffer << (5 - bitsLeft)) & 0x1F;
			result.append(BASE32_CHARS.charAt(index));
		}
		
		while (result.length() % 8 != 0) {
			result.append('=');
		}
		
		return result.toString();
	}

	private static String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (Exception e) {
			return value;
		}
	}
}
