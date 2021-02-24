package com.ideatech.ams.kyc.util;

public class HexBinary {

		/**
		 * Creates a clone of the given byte array.
		 */
		public static byte[] getClone(byte[] pHexBinary) {
			byte[] result = new byte[pHexBinary.length];
			System.arraycopy(pHexBinary, 0, result, 0, pHexBinary.length);
			return result;
		}

		/**
		 * Converts the string <code>pValue</code> into an array of hex bytes.
		 */
		public static byte[] decode(String hexString) {
			if ((hexString.length() % 2) != 0) {
				throw new IllegalArgumentException("A HexBinary string must have even length.");
			}
			byte[] result = new byte[hexString.length() / 2];
			int j = 0;
			for (int i = 0; i < hexString.length();) {
				byte b;
				char c = hexString.charAt(i++);
				char d = hexString.charAt(i++);
				if (c >= '0' && c <= '9') {
					b = (byte) ((c - '0') << 4);
				}
				else if (c >= 'A' && c <= 'F') {
					b = (byte) ((c - 'A' + 10) << 4);
				}
				else if (c >= 'a' && c <= 'f') {
					b = (byte) ((c - 'a' + 10) << 4);
				}
				else {
					throw new IllegalArgumentException("Invalid hex digit: " + c);
				}
				if (d >= '0' && d <= '9') {
					b += (byte) (d - '0');
				}
				else if (d >= 'A' && d <= 'F') {
					b += (byte) (d - 'A' + 10);
				}
				else if (d >= 'a' && d <= 'f') {
					b += (byte) (d - 'a' + 10);
				}
				else {
					throw new IllegalArgumentException("Invalid hex digit: " + d);
				}
				result[j++] = b;
			}
			return result;
		}

		/**
		 * Converts the byte array <code>binary</code> into a string.
		 */
		public static String encode(byte[] binary) {
			return encode(binary, 0, binary.length);
		}

		/**
		 * Converts the specified subarray of bytes <code>binary</code> into a
		 * string.
		 * 
		 * @param binary
		 * @param off
		 * @param len
		 * @return
		 */
		public static String encode(byte[] binary, int off, int len) {
			StringBuffer result = new StringBuffer();
			int end = off + len;
			for (int i = off; i < end; i++) {
				byte b = binary[i];
				byte c = (byte) ((b & 0xf0) >> 4);
				if (c <= 9) {
					result.append((char) ('0' + c));
				}
				else {
					result.append((char) ('A' + c - 10));
				}
				c = (byte) (b & 0x0f);
				if (c <= 9) {
					result.append((char) ('0' + c));
				}
				else {
					result.append((char) ('A' + c - 10));
				}
			}
			return result.toString();
		}

	

}
