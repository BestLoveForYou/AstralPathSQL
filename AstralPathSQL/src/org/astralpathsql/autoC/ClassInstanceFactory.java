package org.astralpathsql.autoC;

public class ClassInstanceFactory {
	private ClassInstanceFactory() {}
	public static <T> T create(Class<?> clazz,String value) {
		try {	// 如果要想采用反射进行简单Java类对象属性设置的时候，类中必须要有无参构造
			Object obj = clazz.getDeclaredConstructor().newInstance() ;
			BeanUtils.setValue(obj, value); 				// 通过反射设置属性
			return (T) obj ; 								// 返回对象
		} catch (Exception e) {
			return null ;								// 设置错误返回null
		}
	}
}
