package com.mieuxcoder.rainbowtable;

public class HexUtils {

	private static final char[] HEXA = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static byte[] fromHexString(String s) {
		int length = s.length();
		int length2 = length / 2;
		byte[] result = new byte[length2];
		for (int i = 0; i < length2; i++) {
			byte x = HexUtils.fromHexChar(s.charAt(2 * i));
			x = (byte)(x << 4);
			x |= HexUtils.fromHexChar(s.charAt(2 * i + 1));
			result[i] = x;
		}
		return result;
	}

	public static byte fromHexChar(char c) {
		if (c >= '0' && c <= '9') {
			return (byte)(c - '0');
		} else if (c >= 'a' && c <= 'f') {
			return (byte)((c - 'a') + 0xA);
		} else if (c >= 'A' && c <= 'F') {
			return (byte)((c - 'A') + 0xA);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static String toHexString(byte[] raw) {
		String s = "";
		for (int i = 0; i < raw.length; i++) {
			s += HEXA[(raw[i] & 0xF0) >> 4];
			s += HEXA[raw[i] & 0x0F];
		}
		return s;
	}
}
