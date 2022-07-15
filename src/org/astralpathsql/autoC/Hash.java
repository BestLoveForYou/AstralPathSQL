package org.astralpathsql.autoC;

import java.util.Base64;

public class Hash {
    private static final String SALT = "ascsacs" ; 			// 公共的盐值
    private static final int REPEAT = 20; 						// 加密次数
    /**
     * 加密处理
     * @param str 要加密的字符串，需要与盐值整合
     * @return 加密后的数据
     */
    public static String encode(String str) {					// 加密处理
        String temp = SALT + str ; 				// 盐值对外不公布
        byte data [] = temp.getBytes() ; 						// 将字符串变为字节数组
        for (int x = 0 ; x < REPEAT ; x ++) {
            data = Base64.getEncoder().encode(data) ; 			// 重复加密
        }
        return new String(data) ;								// 返回加密后的内容
    }
    /**
     * 进行解密处理
     * @param str 要解密的内容
     * @return 解密后的原始数据
     */
    public static String decode(String str) {
        byte data [] = str.getBytes() ;						// 获取加密内容
        for (int x = 0 ; x < REPEAT ; x ++) {
            data = Base64.getDecoder().decode(data) ;			// 多次解密
        }
        return new String(data).replaceFirst(SALT, "") ;	// 删除掉盐值格式
    }

}
