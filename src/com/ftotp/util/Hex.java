package com.ftotp.util;

public final class Hex {
	private static final char[] HEX = "0123456789abcdef".toCharArray();

	private Hex() {

	}

	public static boolean isHex(String s) {
		if (s == null || (s.length() & 1) == 1)
			return false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			boolean ok = (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
			if (!ok)
				return false;
		}
		return true;
	}

	public static byte[] fromHex(String s) {
		int len = s.length();
		byte[] out = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			int hi = Character.digit(s.charAt(i), 16);
			int lo = Character.digit(s.charAt(i + 1), 16);
			out[i / 2] = (byte) ((hi << 4) + lo);
		}
		return out;
	}

	public static String toHex(byte[] data) {
		char[] out = new char[data.length * 2];
		for (int i = 0, j = 0; i < data.length; i++) {
			int b = data[i] & 0xff;
			out[j++] = HEX[b >>> 4];
			out[j++] = HEX[b & 0x0f];
		}
		return new String(out);
	}
}
