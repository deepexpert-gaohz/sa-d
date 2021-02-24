package com.ideatech.ams.pbc.utils;

import com.ideatech.common.utils.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * 数字处理工具类
 * 
 * @author zoulang
 *
 */

/**
 * @author wanghongjie
 *
 * @version 2018-06-13 13:43
 */
public class NumberUtils extends org.apache.commons.lang.math.NumberUtils {
	/**
	 * 判断当前值是否为整数
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isInteger(Object value) {
		if (value == null || StringUtils.isEmpty(value.toString())) {
			return false;
		}
		String mstr = value.toString();
		Pattern pattern = Pattern.compile("^-?\\d+{1}");
		return pattern.matcher(mstr).matches();
	}

	/**
	 * 判断当前值是否为数字(包括小数)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDigit(Object value) {
		if (value == null || StringUtils.isEmpty(value.toString())) {
			return false;
		}
		String mstr = value.toString();
		Pattern pattern = Pattern.compile("^-?[0-9]*.?[0-9]*{1}");
		return pattern.matcher(mstr).matches();
	}

	/**
	 * 将数字格式化输出
	 * 
	 * @param value
	 *            需要格式化的值
	 * @param precision
	 *            精度(小数点后的位数)
	 * @return
	 */
	public static String format(Object value, Integer precision) {
		Double number = 0.0;
		if (NumberUtils.isDigit(value)) {
			number = new Double(value.toString());
		}
		precision = (precision == null || precision < 0) ? 2 : precision;
		BigDecimal bigDecimal = new BigDecimal(number);
		return bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 将数字格式化输出
	 * 
	 * @param value
	 * @return
	 */
	public static String format(Object value) {
		return NumberUtils.format(value, 2);
	}

	/**
	 * 将值转成Integer型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @param replace
	 *            如果为0或者null，替换值
	 * @return
	 */
	public static Integer parseInteger(Object value, Integer replace) {
		if (!NumberUtils.isInteger(value)) {
			return replace;
		}
		return new Integer(value.toString());
	}

	/**
	 * 将值转成Integer型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @return
	 */
	public static Integer parseInteger(Object value) {
		return NumberUtils.parseInteger(value, 0);
	}

	/**
	 * 将值转成Long型
	 * 
	 * @param value
	 * @param replace
	 *            如果为0或者null，替换值
	 * @return
	 */
	public static Long parseLong(Object value, Long replace) {
		if (!NumberUtils.isInteger(value)) {
			return replace;
		}
		return new Long(value.toString());
	}

	/**
	 * 将值转成Long型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @return
	 */
	public static Long parseLong(Object value) {
		return NumberUtils.parseLong(value, 0L);
	}

	/**
	 * 将值转成Double型
	 * 
	 * @param value
	 * @param replace
	 *            replace 如果为0或者null，替换值
	 * @return
	 */
	public static Double parseDouble(Object value, Double replace) {
		if (!NumberUtils.isDigit(value)) {
			return replace;
		}
		return new Double(value.toString());
	}

	/**
	 * 将值转成Double型，如果不是整数，则返回0
	 * 
	 * @param value
	 * @return
	 */
	public static Double parseDouble(Object value) {
		return NumberUtils.parseDouble(value, 0.0);
	}

	/**
	 * 将char型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(char value) {
		byte[] bt = new byte[2];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将short型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(short value) {
		byte[] bt = new byte[2];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将int型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(int value) {
		byte[] bt = new byte[4];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将long型数据转成字节数组
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(long value) {
		byte[] bt = new byte[8];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = (byte) (value >>> (i * 8));
		}
		return bt;
	}

	/**
	 * 将short型数据插入到指定索引的字节数组中
	 * 
	 * @param index
	 *            索引
	 * @param values
	 *            字节数组
	 * @param value
	 *            需要插入的值
	 */
	public static void insert(int index, byte[] values, short value) {
		byte[] bt = NumberUtils.toBytes(value);
		System.arraycopy(bt, 0, values, index, 2);
	}

	/**
	 * 将int型数据插入到指定索引的字节数组中
	 * 
	 * @param index
	 *            索引
	 * @param values
	 *            字节数组
	 * @param value
	 *            需要插入的值
	 */
	public static void insert(int index, byte[] values, int value) {
		byte[] bt = NumberUtils.toBytes(value);
		System.arraycopy(bt, 0, values, index, 4);
	}

	/**
	 * 将long型数据插入到指定索引的字节数组中
	 * 
	 * @param index
	 *            索引
	 * @param values
	 *            字节数组
	 * @param value
	 *            需要插入的值
	 */
	public static void insert(int index, byte[] values, long value) {
		byte[] bt = NumberUtils.toBytes(value);
		System.arraycopy(bt, 0, values, index, 8);
	}

	/**
	 * 将字节转换成整型
	 * 
	 * @param value
	 *            字节类型值
	 * @return
	 */
	public static int byteToInt(byte value) {
		if (value < 0) {
			return value + 256;
		}
		return value;
	}

	/**
	 * 数字转换为字符串
	 * 
	 * @param number
	 * @return
	 */
	public static String numberToStr(double number) {
		try {
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(10);
			format.setMaximumIntegerDigits(20);
			format.setGroupingUsed(false);
			return String.valueOf(format.format(number));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	public static String changeRegisteredCapital2yuan(String registeredCapital){
		if(org.apache.commons.lang.StringUtils.isBlank(registeredCapital))
			return "";
		double regCapital = Double.valueOf(registeredCapital);
		regCapital = regCapital * 10000;
		registeredCapital = numberToStr(regCapital);
		return registeredCapital;
	}
	
	/**
	 * 统一金额小数
	 * @param numStr
	 * @return
	 */
	public static String unifiedDecimal(String numStr) {
		if (org.apache.commons.lang.StringUtils.isNotBlank(numStr)) {
			try {
				BigDecimal db = new BigDecimal(numStr);
				return db.stripTrailingZeros().toPlainString();
			} catch (Exception e) {
				//不做处理，返回原值
				return numStr;
			}
		}
		return "";
	}
	
	public static String str2Number(String numStr) {
		numStr = StringUtils.trim(numStr);
		StringBuffer sb = new StringBuffer();
		for (char c : numStr.toCharArray()) {
			String charStr = c + "";
			if (org.apache.commons.lang.math.NumberUtils.isNumber(charStr) || ".".equals(charStr)) {
				sb.append(charStr);
			}
		}
		return sb.toString();
	}

    /**
     * 注册资金的字符串(带币种)转化为BigDecimal
     * @param str
     * @return
     */
    public static BigDecimal convertCapital(String str) {
    	BigDecimal bd = new BigDecimal(0);
    	if(str !=null) {
    		long val=1;
    		int index=0;
    		if((index=str.indexOf("万"))>=0) {
    			str = new String(str.substring(0, index));
    			val = 10000;
    		}else if((index=str.indexOf("亿"))>=0) {
    			str = new String(str.substring(0, index));
    			val = 100000000;
    		}else if((index=str.indexOf("元"))>=0) {
				str = new String(str.substring(0, index));
				val = 1;
			}
    		if(NumberUtils.isDigit(str)) {
    			bd = (new BigDecimal(str)).multiply(new BigDecimal(val));
    		}
    	}
    	return bd;
    }

    public static BigDecimal formatCapital(BigDecimal bigDecimal){
    	if(bigDecimal !=null){
			BigDecimal newBigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
			if(new BigDecimal(newBigDecimal.longValue()).compareTo(newBigDecimal) ==0){//说明是整数
				BigDecimal newestBigDecimal = newBigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
				return newestBigDecimal;
			}else{
				return newBigDecimal;
			}
		}else{
    		return new BigDecimal(0);
		}
	}
}
