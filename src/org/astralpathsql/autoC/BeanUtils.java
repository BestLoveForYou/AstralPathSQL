package org.astralpathsql.autoC;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BeanUtils {											// 进行Bean处理的类
	private BeanUtils() {} 
	/**
	 * 实现指定对象的属性设置
	 * @param obj 要进行反射操作的实例化对象
	 * @param value 包含有指定内容的字符串，格式“属性:内容|属性:内容”
	 */
	public static void setValue(Object obj,String value) {
		String results [] = value.split("\\|") ; 					// 按照“|”进行拆分
		for (int x = 0 ; x < results.length ; x ++) {				// 循环设置属性内容
			// attval[0]保存的是属性名称、attval[1]保存的是属性内容
			String attval [] = results[x].split(":") ; 			// 获取“属性名称”与内容；
			try {
				if (attval[0].contains(".")) {					// 多级配置
					String temp [] = attval[0].split("\\.") ;
					Object currentObject = obj ;
					// 最后一位肯定是指定类中的属性名称，所以不在本次实例化处理的范畴之内
					for (int y = 0 ; y < temp.length - 1 ; y ++) {	// 实例化
						// 调用相应的getter方法，如果getter方法返回了null表示该对象未实例化
						Method getMethod = currentObject.getClass().getDeclaredMethod("get" + 
								StringUtils.initcap(temp[y])) ;
						Object tempObject = getMethod.invoke(currentObject) ;
						if (tempObject == null) {	// 该对象现在并没有实例化
							Field field = currentObject.getClass()
								.getDeclaredField(temp[y]) ;		// 获取属性类型
							Method method = currentObject.getClass().getDeclaredMethod("set" + 
								StringUtils.initcap(temp[y]), field.getType()) ;
							Object newObject = field.getType()
								.getDeclaredConstructor().newInstance() ;
							method.invoke(currentObject, newObject) ;
							currentObject = newObject ;
						} else {
							currentObject = tempObject ;
						}
					}
					// 进行属性内容的设置
					Field field = currentObject.getClass().getDeclaredField(
							temp[temp.length - 1]) ; 				// 获取成员
					Method setMethod = currentObject.getClass().getDeclaredMethod("set" + 
							StringUtils.initcap(temp[temp.length - 1]), field.getType()) ;
					Object convertValue = BeanUtils.convertAttributeValue(
							field.getType().getName(), attval[1]) ;
					setMethod.invoke(currentObject, convertValue) ; // 调用setter方法设置内容
				} else {
					Field field = obj.getClass().getDeclaredField(attval[0]) ; // 获取成员
					Method setMethod = obj.getClass().getDeclaredMethod("set" + 
						StringUtils.initcap(attval[0]), field.getType()) ;
					Object convertValue = BeanUtils.convertAttributeValue(
						field.getType().getName(), attval[1]) ;
					setMethod.invoke(obj, convertValue) ; 			// 调用setter方法设置内容
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 实现属性类型转换处理
	 * @param type 属性类型，通过Field获取的
	 * @param value 属性的内容，传入的都是字符串，需要将其变为指定类型
	 * @return 转换后的数据
	 */
	private static Object convertAttributeValue(String type, String value) {
		// 根据属性类型判断字符串需要转换的目标类型，所有的类型都可以通过Object保存
		if ("long".equals(type) || "java.lang.Long".equals(type)) { 		// 长整型
			return Long.parseLong(value);									// 转换
		} else if ("int".equals(type) || "java.lang.Integer".equals(type)) {
			return Integer.parseInt(value);
		} else if ("double".equals(type) || "java.lang.Double".equals(type)) {
			return Double.parseDouble(value);
		} else if ("java.util.Date".equals(type)) {
			SimpleDateFormat sdf = null;									// 日期、日期时间
			if (value.matches("\\d{4}-\\d{2}-\\d{2}")) { 
				sdf = new SimpleDateFormat("yyyy-MM-dd");
			} else if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			} else {
				return new Date(); 										// 当前日期
			}
			try {
				return sdf.parse(value);
			} catch (ParseException e) {
				return new Date(); 										// 当前日期
			}
		} else {
			return value;												// 返回数据
		}
	}
}
