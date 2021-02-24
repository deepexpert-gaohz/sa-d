package com.ideatech.ams.pbc.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 各种格式的编码加码工具类 集成Commons-Codec,Commons-Lang及JDK提供的编解码方法.
 * 
 * @author zoulang
 *
 */
public class EncodeUtils {

	/**
	 * 根据编译类型，编译字符串值
	 * 
	 * @param str 字符串值 @param encode 编译类型 @return 编译成功后的值 @throws
	 * UnsupportedEncodingException @exception
	 */
	public static String encodStr(String str, String encode) throws UnsupportedEncodingException {
		return StringUtils.isNotEmpty(str) ? URLEncoder.encode(str, encode) : "";
	}

	/**
	 * unicode编码到中文
	 * 
	 * @param str
	 * @return
	 */
	//没用到   代码扫描注释掉
//	public static String unicode2Chinese(String str) {
//		str = StringUtils.trim(str);
//		String result = "";
//		str = str.replace("\\u", "|");
//		String[] strs = str.split("\\|");
//
//		if (!strs[0].isEmpty()) {
//			result += strs[0];
//		}
//		for (int i = 1; i < strs.length; i++) {
//			String s = strs[i];
//			String ss = "";
//			String ssEnd = "";
//			if (s.length() > 4) {
//				ss = new String(s.substring(0, 4));
//				ssEnd = new String(s.substring(4));
//			} else
//				ss = s;
//			result += (char) ((int) Integer.valueOf(ss, 16)) + ssEnd;
//		}
//		return result;
//	}

	/**
	 * ascii码转str
	 * 
	 * @param theString @return @exception
	 */
	public static String ascii2native(String theString) {
		if (StringUtils.isEmpty(theString)) {
			return "";
		}
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				 } else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else{
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	/**
	 * Hex解码.
	 */
	public static byte[] hexDecode(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符�?,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码.
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String input, String formatCode) {
		try {
			return URLEncoder.encode(input, formatCode);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String input, String formatCode) {
		try {
			return URLDecoder.decode(input, formatCode);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * Html 转码.
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * Html 解码.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	public static String getUTF8StringFromGBKString(String gbkStr) {
		try {
			return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalError();
		}
	}

	public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
		int n = gbkStr.length();
		byte[] utfBytes = new byte[3 * n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			int m = gbkStr.charAt(i);
			if (m < 128) {
			//if (m < 128 && m >= 0) {
				utfBytes[k++] = (byte) m;
				continue;
			}
			utfBytes[k++] = (byte) (0xe0 | (m >> 12));
			utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
			utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
		}
		if (k < utfBytes.length) {
			byte[] tmp = new byte[k];
			System.arraycopy(utfBytes, 0, tmp, 0, k);
			return tmp;
		}
		return utfBytes;
	}

	public static String getEncoding(String str) {
		try {
			String iso8859 = new String(str.getBytes("iso8859-1"));
			String gbk = new String(str.getBytes("gbk"));
			String utf8 = new String(str.getBytes("utf-8"));
			if (iso8859.equals(str)) {
				return "iso8859-1";
			} else if (gbk.equals(str)) {
				return "gbk";
			} else if (utf8.equals(str)) {
				return "utf-8";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
