package org.astralpathsql.autoC;

public class StringUtils {
	/**
	 * 实现字符串首字母大写，如果只有一个字母则直接将此字母大写
	 * @param str 要转换的字符串
	 * @return 大写处理结果，如果传入的字符串为空（包括空字符串）则返回null
	 */
	public static String initcap(String str) {
		if (str == null || "".equals(str)) {			// 字符串是否为空
			return str;								// 如果为空直接返回
		}
		if (str.length() == 1) {						// 判断字符串长度
			return str.toUpperCase();					// 单个字母直接大写
		} else {										// 首字母大写
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
	}
}
